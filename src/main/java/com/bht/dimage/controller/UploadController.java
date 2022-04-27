package com.bht.dimage.controller;

import com.bht.dimage.common.RestResult;
import com.bht.dimage.util.FileUtil;
import com.bht.dimage.vo.UploadImageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Api(tags = "上传文件接口")
@RestController
public class UploadController {

    @ApiOperation(value = "上传文件", notes = "上传缩略图，并以其sha3作为文件名")
    @PostMapping(value = "/uploadImage" )
    @ResponseBody
    public RestResult<String> upload(HttpServletRequest httpServletRequest,
                                     @RequestParam("file") MultipartFile multipartFile,
                                     @RequestParam("sha3") String sha3) {
//        System.out.println(multipartFile.getOriginalFilename());
        String fileName = multipartFile.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf('.'));
        StringBuilder tempName = new StringBuilder();
        tempName.append(sha3).append(suffixName);
        String newFileName = tempName.toString();
        //创建文件
        File destFile;
        try {
            String dirPath = FileUtil.getFilePath();
            System.out.println("Dir path:" + dirPath);
            String fullPath = dirPath + newFileName;
            destFile = new File(fullPath);
            multipartFile.transferTo(destFile);
            UploadImageVo uploadImageVo = new UploadImageVo();
            uploadImageVo.setThumbnailPath(fullPath);
            return RestResult.Success().data(uploadImageVo);
        } catch (IOException e) {
            e.printStackTrace();
            return RestResult.Fail();
        }
    }
}