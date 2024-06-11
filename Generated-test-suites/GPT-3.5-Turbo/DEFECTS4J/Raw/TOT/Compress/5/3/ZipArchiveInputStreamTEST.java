import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipException;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() {
        byte[] zipData = { 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x38, 0x41, 0xE4, 0x08, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xE5, 0x81, (byte) 0x82, 0x00, 0x00,
                0x00, 0x00 };
        ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
        zipInputStream = new ZipArchiveInputStream(bais);
    }

    @Test
    public void testGetNextZipEntry() throws IOException {
        ZipArchiveEntry entry = zipInputStream.getNextZipEntry();

        assertNotNull(entry);
        assertEquals("ą", entry.getName());
        assertEquals(0x08E44138L, entry.getSize());
        assertEquals(2, entry.getMethod());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[4];
        int bytesRead = zipInputStream.read(buffer);

        assertEquals(4, bytesRead);
        assertEquals(0x50, buffer[0]); // First byte of zip file signature
        assertEquals(0x4B, buffer[1]); // Second byte of zip file signature
        assertEquals(0x03, buffer[2]); // Third byte of zip file signature
        assertEquals(0x04, buffer[3]); // Fourth byte of zip file signature
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        zipInputStream.close();
        zipInputStream.getNextZipEntry();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipWithNegativeValue() throws IOException {
        zipInputStream.skip(-1);
    }

    @Test
    public void testSkip() throws IOException {
        long skipBytes = zipInputStream.skip(4);
        assertEquals(4, skipBytes);

        byte[] buffer = new byte[4];
        int bytesRead = zipInputStream.read(buffer);

        assertEquals(4, bytesRead);
        assertEquals(0x41, buffer[0]); // Third byte of zip file signature
        assertEquals(0xE4, buffer[1]); // Fourth byte of zip file signature
        assertEquals(0x08, buffer[2]); // Fifth byte of compressed file size
        assertEquals(0x00, buffer[3]); // Sixth byte of compressed file size
    }

    @Test
    public void testMatchesWithValidSignature() {
        byte[] signature = { 0x50, 0x4B, 0x03, 0x04 };

        boolean matches = ZipArchiveInputStream.matches(signature, 4);

        assertTrue(matches);
    }

    @Test
    public void testMatchesWithInvalidSignature() {
        byte[] signature = { 0x00, 0x00, 0x00, 0x00 };

        boolean matches = ZipArchiveInputStream.matches(signature, 4);

        assertFalse(matches);
    }

    @Test(expected = ZipException.class)
    public void testReadThrowsZipExceptionWhenDataFormatExceptionOccurs() throws IOException {
        byte[] zipData = { 0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x00, 0x00, 0x38, 0x41, 0xE4, 0x08, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xE5, 0x81, (byte) 0x82, 0x00, 0x00,
                0x00, 0x00, 0x00 };
        ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
        ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream(bais);

        byte[] buffer = new byte[4];
        zipInputStream.readFully(buffer);
    }

    @Test
    public void testCloseEntry() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.read(new byte[10]);

        zipInputStream.closeEntry();

        byte[] buffer = new byte[4];
        int bytesRead = zipInputStream.read(buffer);

        assertEquals(4, bytesRead);
        assertEquals(0x41, buffer[0]); // Third byte of zip file signature
        assertEquals(0xE4, buffer[1]); // Fourth byte of zip file signature
        assertEquals(0x08, buffer[2]); // Fifth byte of compressed file size
        assertEquals(0x00, buffer[3]); // Sixth byte of compressed file size
    }
}