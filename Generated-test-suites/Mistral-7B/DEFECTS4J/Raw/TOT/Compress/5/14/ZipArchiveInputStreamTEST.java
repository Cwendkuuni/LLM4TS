package org.apache.commons.compress.test.archivers.zip;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zis;
    private ByteArrayOutputStream baos;

    @BeforeEach
    void setUp() throws IOException {
        String testFile = "TestData.zip";
        InputStream is = getClass().getResourceAsStream(testFile);
        baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        // Copy the test data to a ByteArrayOutputStream
        int len;
        while ((len = is.read(buffer)) > 0) {
            baos.write(buffer, 0, len);
        }
        baos.close();
        is.close();

        // Create test input stream
        InputStream testStream = new ByteArrayInputStream(baos.toByteArray());
        zis = new ZipArchiveInputStream(testStream);
    }

    @Test
    void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry;
        while ((entry = zis.getNextZipEntry()) != null) {
            Assertions.assertNotNull(entry);
            Assertions.assertEquals(entry.getSize(), entry.getCompressedSize());
            Assertions.assertEquals(entry.getName(), getExpectedName(entry.getName()));
        }
        Assertions.assertNull(zis.getNextZipEntry());
    }

    @Test
    void testGetNextEntry() throws IOException {
        ZipArchiveEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            Assertions.assertNotNull(entry);
            Assertions.assertEquals(entry.getSize(), entry.getCompressedSize());
            Assertions.assertEquals(entry.getName(), getExpectedName(entry.getName()));
        }
        Assertions.assertNull(zis.getNextEntry());
    }

    @Test
    void testRead() throws IOException {
        byte[] buffer = new byte[1024];

        // Read from an entry with no compression
        int read = zis.read(buffer, 0, buffer.length);
        Assertions.assertGreaterEq(read, 0);
        Assertions.assertArrayEquals(getExpectedData("TestData1.txt"), buffer, 0, read);

        // Read from a deflated entry
        zis.getNextZipEntry();
        read = zis.read(buffer, 0, buffer.length);
        Assertions.assertGreaterEq(read, 0);
        Assertions.assertArrayEquals(getExpectedData("TestData2.txt"), buffer, 0, read);

        zis.close();
    }

    @Test
    void testReadAllBytes() throws IOException {
        byte[] testData = zis.readAllBytes();
        Assertions.assertNotNull(testData);
        Assertions.assertEquals(getExpectedSize(), testData.length);
    }

    @Test
    void testClose() throws IOException {
        zis.close();
        Assertions.assertFalse(zis.isOpen());
    }

    @Test
    void testSkip() throws IOException {
        long length = getExpectedSize();
        zis.skip(length);
        Assertions.assertEquals(0, zis.read(new byte[1024], 0, 1024));
    }

    @Test
    void testReadFully() throws IOException {
        byte[] expectedData = getExpectedData("TestData3.txt");
        byte[] buffer = new byte[expectedData.length];
        zis.readFully(buffer);
        Assertions.assertArrayEquals(expectedData, buffer);
    }

    @Test
    void testCloseEntry() throws IOException {
        // Test reading an entry with no compression
        ZipArchiveEntry entry = zis.getNextZipEntry();
        Assertions.assertNotNull(entry);
        byte[] data = new byte[entry.getSize()];
        int read = zis.read(data, 0, data.length);
        Assertions.assertGreaterEq(read, 0);
        zis.closeEntry();

        // Test reading a deflated entry
        entry = zis.getNextZipEntry();
        Assertions.assertNotNull(entry);
        InfuseAndRead(entry);
        zis.closeEntry();
    }

    private void InfuseAndRead(ZipArchiveEntry entry) throws IOException {
        infuse();
        int read = zis.read(new byte[1024], 0, 1024);
        Assertions.assertGreaterEq(read, 0);
    }

    private void infuse() throws IOException {
        if (zis.inf.needsInput()) {
            byte[] buffer = new byte[1024];
            zis.read(buffer, 0, buffer.length);
            zis.infuse(buffer, 0, buffer.length);
        }
    }

    private byte[] getExpectedData(String fileName) throws IOException {
        InputStream is = getClass().getResourceAsStream(fileName);
        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();
        return buffer;
    }

    private String getExpectedName(String name) {
        return name.replaceAll("/", "\\\\");
    }

    private long getExpectedSize() {
        long size = 0;
        try (InputStream is = getClass().getResourceAsStream("/TestData.zip")) {
            size = is.available();
        } catch (IOException e) {
            System.err.println("Failed to find test data file: " + e.getMessage());
        }
        return size;
    }
}