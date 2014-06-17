/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.myc.ctt;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 将整个项目的java代码合并为一份txt文档.
 *
 * @author mayc
 */
public class CodeToTxt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args == null) {
            showHelp();
            return;
        }
        long t = System.currentTimeMillis();

        try {
            System.out.println("开始扫描.");
            //确定扫描的位置.
            String rootPath = args.length == 0 ? null : args[0];     //扫描根路径
            File rootDir;   //扫描的根目录.
            if (rootPath == null) {
                System.out.println("请传入要扫描目录.");
                showHelp();
                return;
                //以当前目录为根目录.
//                rootPath = CodeToTxt.class.getResource(".").getFile();
            }
            System.out.println("扫描路径:" + rootPath);
            rootDir = new File(rootPath);
//        File outFile = File.createTempFile("ctt", ".txt");
            //扫描目录
            File outFile = scanDir(rootDir);
            //输出文件.
            File dest = new File(rootDir, outFile.getName());
            outFile.renameTo(dest);
            System.out.println("输出到:" + dest.getAbsolutePath());
            //计算用时
            t = System.currentTimeMillis() - t;
            t = t < 1000 ? 1 : t / 1000;

            System.out.println("扫描完成.共用时" + (t > 60 ? (t > 360 ? (t / 360f + "小时") : t / 60f + "分钟") : t + "秒"));
        } catch (Exception e) {
            System.out.println("扫描失败.请查看日志.");
            e.printStackTrace();
        }
    }

    /**
     * 显示帮助.
     */
    private static void showHelp() {
        System.out.println("用法:CodeToTxt [scan dir path]");
    }

    /**
     * 扫描文件.
     *
     * @param rootDir
     * @return
     */
    private static File scanDir(File rootDir) throws IOException {
        File outFile = File.createTempFile("ctt", ".txt");

        FileOutputStream out = new FileOutputStream(outFile);
        try {

            scanDir(rootDir, out);

        } finally {
            out.close();
        }

        return outFile;
    }

    /**
     * 扫描目录.
     *
     * @param dir
     * @param out
     */
    private static void scanDir(File dir, FileOutputStream out) throws FileNotFoundException, IOException {
        System.out.println(dir.getAbsolutePath());
        File[] files = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".java") || pathname.isDirectory();
            }
        });

        for (File file : files) {
            if (file.isDirectory()) {
                scanDir(file, out);
            } else {
                scanFile(file, out);
            }
        }
    }

    private static void scanFile(File file, FileOutputStream out) throws FileNotFoundException, IOException {
        System.out.println("\t" + file.getAbsolutePath());
        FileInputStream in = new FileInputStream(file);
        try {
            byte[] b = new byte[1024];
            while (in.read(b) > 0) {
                out.write(b);
            }
        } finally {
            in.close();
        }
    }

}
