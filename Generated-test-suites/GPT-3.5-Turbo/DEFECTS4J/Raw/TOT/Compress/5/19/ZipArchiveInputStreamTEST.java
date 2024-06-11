package org.apache.commons.compress.archivers.zip;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipArchiveInputStreamTest {

    private byte[] zipData;

    @Before
    public void setup() {
        // Create a sample zip data
        zipData = createZipData();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Test case for a normal zip file entry
        ZipArchiveEntry entry1 = zipInputStream.getNextZipEntry();
        Assert.assertEquals("file1.txt", entry1.getName());

        // Test case for empty zip file
        ZipArchiveEntry entry2 = zipInputStream.getNextZipEntry();
        Assert.assertNull(entry2);

        zipInputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Test case for a normal zip file entry
        ArchiveEntry entry1 = zipInputStream.getNextEntry();
        Assert.assertTrue(entry1 instanceof ZipArchiveEntry);
        Assert.assertEquals("file1.txt", entry1.getName());

        // Test case for empty zip file
        ArchiveEntry entry2 = zipInputStream.getNextEntry();
        Assert.assertNull(entry2);

        zipInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Skip the first entry
        zipInputStream.getNextZipEntry();

        // Test reading from a zip file entry with stored compression method
        byte[] buffer1 = new byte[20];
        int bytesRead1 = zipInputStream.read(buffer1);
        Assert.assertEquals(10, bytesRead1);
        Assert.assertEquals("Hello World", new String(buffer1));

        // Test reading from a zip file entry with deflated compression method
        byte[] buffer2 = new byte[30];
        int bytesRead2 = zipInputStream.read(buffer2);
        Assert.assertEquals(16, bytesRead2);
        Assert.assertEquals("This is a test file", new String(buffer2));

        zipInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        zipInputStream.close();

        try {
            // Try to read from a closed stream
            byte[] buffer = new byte[20];
            zipInputStream.read(buffer);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("The stream is closed", e.getMessage());
        }
    }

    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Skip the first entry
        zipInputStream.getNextEntry();

        // Skip one byte from the input stream
        long skippedBytes = zipInputStream.skip(1);
        Assert.assertEquals(1, skippedBytes);

        // Read the next bytes from the input stream
        byte[] buffer = new byte[20];
        int bytesRead = zipInputStream.read(buffer);
        Assert.assertEquals(9, bytesRead);
        Assert.assertEquals("ello World", new String(buffer));

        zipInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature1 = {0x50, 0x4B, 0x07, 0x08};
        byte[] signature2 = {0x50, 0x4B, 0x05, 0x06};

        // Test case for a zip file entry
        boolean matches1 = ZipArchiveInputStream.matches(signature1, signature1.length);
        Assert.assertTrue(matches1);

        // Test case for an empty zip file
        boolean matches2 = ZipArchiveInputStream.matches(signature2, signature2.length);
        Assert.assertTrue(matches2);
    }

    @Test
    public void testCloseEntry() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(inputStream);

        // Skip the first entry
        zipInputStream.getNextEntry();

        // Close the current entry
        zipInputStream.closeEntry();

        try {
            // Try to read from a closed entry
            byte[] buffer = new byte[20];
            zipInputStream.read(buffer);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            Assert.assertEquals("The stream is closed", e.getMessage());
        }

        zipInputStream.close();
    }

    private byte[] createZipData() {
        byte[] data1 = "Hello World".getBytes();
        byte[] data2 = "This is a test file".getBytes();

        byte[] header1 = createEntryHeader("file1.txt", data1.length);
        byte[] header2 = createEntryHeader("file2.txt", data2.length);

        byte[] zipData = new byte[header1.length + data1.length + header2.length + data2.length];

        System.arraycopy(header1, 0, zipData, 0, header1.length);
        System.arraycopy(data1, 0, zipData, header1.length, data1.length);
        System.arraycopy(header2, 0, zipData, header1.length + data1.length, header2.length);
        System.arraycopy(data2, 0, zipData, header1.length + data1.length + header2.length, data2.length);

        return zipData;
    }

    private byte[] createEntryHeader(String name, int size) {
        byte[] header = new byte[30];
        header[0] = 0x50; // LFH signature (0x50 0x4B 0x03 0x04)
        header[1] = 0x4B;
        header[2] = 0x03;
        header[3] = 0x04;
        header[26] = (byte) size; // Compressed size
        header[27] = (byte) (size >> 8);
        header[28] = (byte) size; // Uncompressed size
        header[29] = (byte) (size >> 8);

        byte[] nameBytes = name.getBytes();
        header[10] = (byte) nameBytes.length; // File name length
        header[11] = (byte) (nameBytes.length >> 8);

        System.arraycopy(nameBytes, 0, header, 30 - nameBytes.length, nameBytes.length);

        return header;
    }
}