package org.apache.commons.compress.archivers.cpio;

import static org.junit.Assert.*;

import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    private OutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new FileOutputStream(File.createTempFile("test", ".cpio"));
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        String testFileContent = "test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testFileContentBytes = testFileContent.getBytes();
        cpioArchiveOutputStream.write(testFileContentBytes, 0, testFileContentBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithClosedStream() throws Exception {
        String testFileContent = "test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.write(testFileContent.getBytes(), 0, testFileContent.length());
        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithWrongFormat() throws Exception {
        String testFileContent = "test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        entry.setFormat(CpioConstants.FORMAT_OLD_ASCII); // wrong format

        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws Exception {
        String testFileContent = "test content";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(testFileContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testFileContent.getBytes(), 0, testFileContent.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testFinish() throws Exception {
        cpioArchiveOutputStream.finish();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ArchiveUtils.copy(cpioArchiveOutputStream.getOutputStream(), byteArrayOutputStream);
        String expectedMagic = ArchiveUtils.toAsciiBytes(CpioConstants.MAGIC_NEW);
        String actualMagic = new String(byteArrayOutputStream.toByteArray(), 0, expectedMagic.length());
        assertEquals(expectedMagic, actualMagic);
    }

    @Test
    public void testClose() throws Exception {
        cpioArchiveOutputStream.close();
        File file = new File(cpioArchiveOutputStream.getFile());
        assertTrue(file.delete());
    }

    @Test
    public void testCreateArchiveEntry() throws Exception {
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(new FileOutputStream(new File("test.cpio")), CpioConstants.FORMAT_NEW);
        File testFile = new File("testfile.txt");
        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(testFile, "testfile");
        cpioArchiveOutputStream.putNextEntry(entry);
        cpioArchiveOutputStream.write("test content".getBytes(), 0, "test content".getBytes().length);
        cpioArchiveOutputStream.closeArchiveEntry();
        cpioArchiveOutputStream.close();
        File resultFile = new File("test.cpio");
        byte[] expectedContent = "test content".getBytes();
        byte[] actualContent = ArchiveUtils.toByteArray(resultFile);
        assertArrayEquals(expectedContent, actualContent);
    }
}