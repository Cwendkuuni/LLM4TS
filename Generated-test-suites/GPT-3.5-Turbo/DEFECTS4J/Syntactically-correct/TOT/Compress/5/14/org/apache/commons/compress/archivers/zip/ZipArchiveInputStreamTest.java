import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipArchiveInputStream;
    private byte[] zipData;

    @Before
    public void setUp() {
        // Prepare zip data
        zipData = generateZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        zipArchiveInputStream.close();
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());
        assertFalse(entry.isDirectory());
        assertEquals(10, entry.getSize());

        entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("folder1/", entry.getName());
        assertTrue(entry.isDirectory());
        assertEquals(0, entry.getSize());
    }

    @Test
    public void testRead() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);

        byte[] buffer = new byte[10];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(10, bytesRead);
        assertArrayEquals("File Content".getBytes(), buffer);
    }

    @Test
    public void testClose() throws IOException {
        zipArchiveInputStream.close();
        try {
            zipArchiveInputStream.getNextZipEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected exception
        }
    }

    @Test
    public void testSkip() throws IOException {
        ZipArchiveEntry entry = zipArchiveInputStream.getNextZipEntry();
        assertNotNull(entry);
        assertEquals("file1.txt", entry.getName());

        long bytesSkipped = zipArchiveInputStream.skip(5);
        assertEquals(5, bytesSkipped);

        byte[] buffer = new byte[5];
        int bytesRead = zipArchiveInputStream.read(buffer, 0, buffer.length);
        assertEquals(5, bytesRead);
        assertArrayEquals("Content".getBytes(), buffer);
    }

    @Test
    public void testMatchesWithValidSignature() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x02, 0x00};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x02, 0x01};
        assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }

    private byte[] generateZipData() {
        // Generate zip with one file and one folder
        byte[] fileContent = "File Content".getBytes();
        byte[] zipData = new byte[30 + fileContent.length];

        // File Entry
        zipData[0] = 0x50; // LFH_Sig
        zipData[1] = 0x4B;
        zipData[2] = 0x03;
        zipData[3] = 0x04;

        zipData[14] = 0x0A; // file name length
        zipData[16] = (byte) fileContent.length; // compressed size
        zipData[18] = 0x0A; // uncompressed size

        System.arraycopy(fileContent, 0, zipData, 30, fileContent.length);

        // Folder Entry
        zipData[30 + fileContent.length] = 0x50; // LFH_Sig
        zipData[31 + fileContent.length] = 0x4B;
        zipData[32 + fileContent.length] = 0x03;
        zipData[33 + fileContent.length] = 0x04;

        zipData[46 + fileContent.length] = 0x08; // file name length

        return zipData;
    }
}