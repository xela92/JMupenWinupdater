/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmupenwinupdater;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author aflorenti
 */
public class JMupenWinupdater {

    private static final File updatePackage = new File(getConfigDir().concat(File.separator).concat("jmupen-update.jar"));
    private static File currentJar;
    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir") + "\\jmupen");
    private static final File currentJarTxtFile = new File(tmpDir.getAbsolutePath() + File.separator + "bin" + File.separator + "currentjar.txt");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe";

        currentJar = JMupenWinupdater.getCurrentJarFromTxt();
        try {
            FileUtils.moveFile(updatePackage, currentJar);
        } catch (IOException ex) {
            System.err.println("Error moving update file. " + ex.getLocalizedMessage());
        }

        /* Build command: java -jar application.jar */
        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException ex) {
            System.err.println("Error restarting app. " + ex.getLocalizedMessage());
        }
    }

    public static File getCurrentJarFromTxt() {
        FileInputStream f;
        String path = null;
        try {
            f = new FileInputStream(currentJarTxtFile);
            path = IOUtils.toString(f);
        } catch (Exception ex) {
            System.err.println("File not found or can't read file " + ex.getLocalizedMessage());
        }
        if (path == null) {
            path = "";
        }
        return new File(path);
    }

    public static String getConfigDir() {
        return System.getProperty("java.io.tmpdir");
    }

}