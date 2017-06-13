package net.thomaspreis.tools.fc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileCopyMain {

	Logger logger = Logger.getLogger(FileCopyMain.class);

	public static void main(String[] args) {
		String srcFolder = "/Users/Temp/source";
		String targetFolder = "/Users/Temp/target";
		try {
			new FileCopyMain().run(srcFolder, targetFolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void run(String srcFolder, String targetFolder) throws IOException {
		long t0 = System.currentTimeMillis();
		logger.info("Starting copy files, from: '" + srcFolder + "' to '" + targetFolder + "'");
		List<String> listSrcFiles = new ArrayList<String>();
		listFiles(listSrcFiles, new File(srcFolder));
		logger.info("Total files to be copied: " + listSrcFiles.size());
		copyFiles(listSrcFiles, srcFolder, targetFolder);
		logger.info("Finishing copy files, Time spent: " + (System.currentTimeMillis() - t0) + " ms");
	}

	void copyFiles(List<String> listSrcFiles, String srcFolder, String targetFolder) throws IOException {
		for (String srcFilePath : listSrcFiles) {
			String targetFilePath = getTargetFile(srcFilePath, srcFolder, targetFolder);
			File srcFile = new File(srcFilePath);
			File targetFile = new File(targetFilePath);
			if (!targetFile.exists()) {
				logger.info("Copying file from '" + srcFilePath + "' to file '" + targetFilePath + "'");
				FileUtils.copyFile(srcFile, targetFile);
			} else if (FileUtils.sizeOf(srcFile) != FileUtils.sizeOf(targetFile)) {
				logger.info("Files sizes diverged, removing file '" + targetFilePath + "'");
				targetFile.delete();
				FileUtils.copyFile(srcFile, targetFile);
			} else {
				logger.info("File already exists, skipping '" + targetFilePath + "'");
			}
		}
	}

	String getTargetFile(String srcFile, String srcFolder, String targetFolder) {
		String targetFile = srcFile;
		targetFile = targetFile.replace(srcFolder, targetFolder);
		return targetFile;
	}

	void listFiles(List<String> listSrcFiles, File file) {
		if (file.isFile()) {
			listSrcFiles.add(file.getAbsolutePath());
		} else {
			for (File f : file.listFiles()) {
				listFiles(listSrcFiles, f);
			}
		}
	}

}
