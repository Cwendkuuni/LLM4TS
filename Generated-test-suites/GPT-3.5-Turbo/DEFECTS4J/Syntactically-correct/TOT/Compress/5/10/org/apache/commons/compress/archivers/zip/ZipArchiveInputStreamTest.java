import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInputStream;

    @Before
    public void setUp() {
        // Use ByteArrayInputStream to simulate a zip file content
        byte[] zipFileContent = {0x50, 0x4B, 0x03, 0x04, 0x14, 0x00, 0x00, 0x00, 0x08, 0x00, 0x21, 0x1E, 0xB4, 0xF5, 0x95,
                0x4A, 0x6B, 0xDF, 0x01, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00};
        zipInputStream = new ZipArchiveInputStream(new ByteArrayInputStream(zipFileContent));
    }

    @Test
    public void testGetNextZipEntryNotNull() throws IOException {
        assertNotNull(zipInputStream.getNextZipEntry());
    }

    @Test
    public void testGetNextZipEntryEOF() throws IOException {
        // Attempt to get next entry until the end of the file
        while (zipInputStream.getNextZipEntry() != null) {
            assertNotNull(zipInputStream.getNextZipEntry());
        }
        assertNull(zipInputStream.getNextZipEntry());
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[8];
        assertEquals(8, zipInputStream.read(buffer));
    }

    @Test
    public void testReadEOF() throws IOException {
        // Attempt to read until the end of the file
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = zipInputStream.read(buffer)) != -1) {
            assertNotEquals(-1, bytesRead);
        }
        assertEquals(-1, zipInputStream.read(buffer));
    }

    @Test(expected = IOException.class)
    public void testReadClosedStream() throws IOException {
        zipInputStream.close();
        byte[] buffer = new byte[8];
        zipInputStream.read(buffer);
    }

    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        assertTrue(zipInputStream.isClosed());
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(4, zipInputStream.skip(4));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeValue() throws IOException {
        zipInputStream.skip(-2);
    }

    @Test(expected = IOException.class)
    public void testSkipClosedStream() throws IOException {
        zipInputStream.close();
        zipInputStream.skip(4);
    }

    @Test
    public void testMatchesLFH() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x04};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesEOCD() {
        byte[] signature = {0x50, 0x4B, 0x05, 0x06};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesInvalidLength() {
        byte[] signature = {0x50, 0x4B, 0x05};
        assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testMatchesInvalidSignature() {
        byte[] signature = {0x50, 0x4B, 0x03, 0x05};
        assertFalse(ZipArchiveInputStream.matches(signature, signature.length));
    }

}