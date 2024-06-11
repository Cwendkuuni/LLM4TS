package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {

    private File testFile;
    private ZipFile zipFile;
    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() throws Exception {
        testFile = Files.createTempFile("testZipArchiveInputStream", ".zip").toFile();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(testFile))) {
            // Add a test entry
            ZipEntry zipEntry = new ZipEntry("test.txt");
            zipEntry.setSize(-1); // Set the size to -1 for DEFLATE
            zipEntry.setMethod(ZipEntry.DEFLATED);
            zos.putNextEntry(zipEntry);
            zos.write("Test data".getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
        }
        zipFile = new ZipFile(testFile);
        zipInputStream = new ZipArchiveInputStream(zipFile.getInputStream(new ZipEntry("test.txt")));
    }

    @After
    public void tearDown() throws Exception {
        if (zipFile != null) {
            zipFile.close();
        }
        if (testFile != null) {
            Files.deleteIfExists(testFile.toPath());
        }
    }

    @Test
    public void testReadEntryData() throws IOException {
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        int length;
        while ((length = zipInputStream.read(buffer)) != -1) {
            Assert.assertEquals("Test data", new String(buffer, 0, length, StandardCharsets.UTF_8));
        }
        Assert.assertFalse("End of stream reached", zipInputStream.read(buffer) != -1);
    }

    @Test(expected = EOFException.class)
    public void testReachEndOfStream() throws IOException {
        byte[] buffer = new byte[ZipArchiveInputStream.BUFFER_SIZE];
        while (zipInputStream.read(buffer) != -1) {
            // Empty
        }
        zipInputStream.read(buffer);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadAfterClose() throws IOException {
        zipInputStream.close();
        zipInputStream.read(new byte[1]);
    }

    @Test(expected = ZipException.class)
    public void testInvalidSignature() throws IOException {
        try (FileInputStream inputStream = new FileInputStream(testFile)) {
            byte[] signature = new byte[ZipArchiveOutputStream.LFH_SIG.length];
            inputStream.read(signature);
            if (!ZipArchiveInputStream.matches(signature, signature.length)) {
                throw new ZipException("Invalid signature");
            }
        }
        try (ZipArchiveInputStream badInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(new byte[1]))){
            Assert.assertFalse("Should not be able to create a valid stream from an empty byte array",
                    badInputStream.getNextZipEntry() != null);
        }
    }

    @Test
    public void testReadEmptyEntry() throws IOException {
        try (ZipEntry zipEntry = zipFile.getInputStream(new ZipEntry(""));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            while (zipInputStream.read(buffer) != -1) {
                outputStream.write(buffer, 0, buffer.length);
            }
            Assert.assertEquals("", new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        }
    }

    @Test
    public void testReadTrailingGarbage() throws IOException {
        try (ZipEntry zipEntry = zipFile.getInputStream(new ZipEntry("test.txt"));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            while (zipInputStream.read(buffer) != -1) {
                outputStream.write(buffer, 0, buffer.length);
            }
            Assert.assertEquals("Test data", new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
        }
        try (ZipEntry zipEntry = zipFile.getInputStream(new ZipEntry("test.txt"));
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = zipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            // Add some trailing garbage
            outputStream.write(new byte[512]);
            byte[] actualBytes = outputStream.toByteArray();
            byte[] expectedBytes = IOUtils.toByteArray(zipFile.getInputStream(new ZipEntry("test.txt")));
            Assert.assertArrayEquals("Unexpected trailing bytes", expectedBytes, actualBytes);
        }
    }
}