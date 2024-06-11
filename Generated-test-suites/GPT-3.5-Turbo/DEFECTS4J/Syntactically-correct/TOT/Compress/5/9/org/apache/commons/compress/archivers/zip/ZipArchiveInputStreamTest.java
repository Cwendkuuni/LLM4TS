import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ByteArrayInputStream byteArrayInputStream;
    private ZipArchiveInputStream zipInputStream;
    
    @Before
    public void setUp() {
        String testFileContent = "This is a test file.";
        byteArrayInputStream = new ByteArrayInputStream(testFileContent.getBytes());
        zipInputStream = new ZipArchiveInputStream(byteArrayInputStream);
    }
    
    @Test
    public void testGetNextZipEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextZipEntry();
        assertNull(entry);
    }
    
    @Test(expected = IOException.class)
    public void testReadClosedStream() throws IOException {
        zipInputStream.close();
        zipInputStream.read(new byte[10]);
    }
    
    @Test
    public void testReadEmptyStream() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = zipInputStream.read(buffer);
        assertEquals(-1, bytesRead);
    }
    
    @Test
    public void testReadPartialData() throws IOException {
        byte[] buffer = new byte[10];
        zipInputStream.getNextZipEntry();
        int bytesRead = zipInputStream.read(buffer);
        assertEquals(10, bytesRead);
        String data = new String(buffer);
        assertEquals("This is a ", data);
    }
    
    @Test(expected = EOFException.class)
    public void testReadEndOfStream() throws IOException {
        byte[] buffer = new byte[20];
        zipInputStream.getNextZipEntry();
        zipInputStream.read(buffer);
        zipInputStream.read(buffer);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadInvalidBuffer() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.read(new byte[10], 11, 5);
    }
    
    @Test
    public void testSkipBytes() throws IOException {
        zipInputStream.getNextZipEntry();
        long skippedBytes = zipInputStream.skip(5);
        assertEquals(5, skippedBytes);
        int bytesRead = zipInputStream.read(new byte[10]);
        assertEquals(10, bytesRead);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeBytes() throws IOException {
        zipInputStream.getNextZipEntry();
        zipInputStream.skip(-5);
    }
    
    @Test
    public void testMatches() {
        byte[] lfhSignature = { 0x50, 0x4b, 0x03, 0x04 };

        assertTrue(ZipArchiveInputStream.matches(lfhSignature, 4));

        byte[] eocdSignature = { 0x50, 0x4b, 0x05, 0x06 };

        assertFalse(ZipArchiveInputStream.matches(eocdSignature, 4));
    }
    
    @Test
    public void testClose() throws IOException {
        zipInputStream.close();
        assertTrue(zipInputStream.isClosed());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = zipInputStream.getNextEntry();
        assertNull(entry);
    }
}