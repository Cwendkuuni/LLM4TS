package org.apache.commons.compress.archivers.tar;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_LONG_FILE_NAME = "longTestFileNameWithLotsOfChars.txt";
    private static final int RECORD_SIZE = 512;
    private static final int BLOCK_SIZE = 256;

    @org.junit.Test
    public void testNormalFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, RECORD_SIZE, BLOCK_SIZE);

        File fileToWrite = new File(TEST_FILE_NAME);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileToWrite, TEST_FILE_NAME);
        tarOutputStream.putArchiveEntry(archiveEntry);

        String testString = "Test string to write to file.";
        byte[] testData = testString.getBytes();

        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveInput = new TarArchiveOutputStream(inputStream);

        ArchiveEntry readEntry = archiveInput.getNextEntry();
        assertEquals(TEST_FILE_NAME, readEntry.getName());

        byte[] readData = new byte[testData.length];
        int length = inputStream.read(readData, 0, readData.length);
        assertArrayEquals(testData, readData);

        archiveInput.close();
        inputStream.close();
        outputStream.close();
    }

    @org.junit.Test
    public void testLongFileName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, RECORD_SIZE, BLOCK_SIZE);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        File fileToWrite = new File(TEST_LONG_FILE_NAME);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileToWrite, TEST_LONG_FILE_NAME);
        tarOutputStream.putArchiveEntry(archiveEntry);

        String testString = "Test string to write to file.";
        byte[] testData = testString.getBytes();

        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveOutputStream archiveInput = new TarArchiveOutputStream(inputStream);

        ArchiveEntry readEntry = archiveInput.getNextEntry();
        assertEquals(TEST_LONG_FILE_NAME, readEntry.getName());

        byte[] readData = new byte[testData.length];
        int length = inputStream.read(readData, 0, readData.length);
        assertArrayEquals(testData, readData);

        archiveInput.close();
        inputStream.close();
        outputStream.close();
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testLongFileNameWithTruncation() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(outputStream, RECORD_SIZE, BLOCK_SIZE);

        File fileToWrite = new File(TEST_LONG_FILE_NAME);
        ArchiveEntry archiveEntry = new TarArchiveEntry(fileToWrite, TEST_LONG_FILE_NAME);
        tarOutputStream.putArchiveEntry(archiveEntry);

        String testString = "Test string to write to file.";
        byte[] testData = testString.getBytes();

        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        // This will fail because the file name is too long
        tarOutputStream.finish();

        archiveInput.close();
        inputStream.close();
        outputStream.close();
    }
}