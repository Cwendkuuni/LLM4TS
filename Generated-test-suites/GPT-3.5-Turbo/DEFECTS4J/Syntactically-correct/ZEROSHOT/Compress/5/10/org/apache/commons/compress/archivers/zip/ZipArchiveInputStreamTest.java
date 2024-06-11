import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.junit.Before;
import org.junit.Test;

public class ZipArchiveInputStreamTest {

    private ZipArchiveInputStream zipInput;

    @Before
    public void setUp() throws Exception {
        byte[] zipData = getTestZipData();
        InputStream inputStream = new ByteArrayInputStream(zipData);
        zipInput = new ZipArchiveInputStream(inputStream);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = zipInput.getNextEntry();
        assertEquals("test.txt", entry.getName());
        assertTrue(entry.isDirectory());
    }
    
    @Test
    public void testRead() throws IOException {
        zipInput.getNextEntry();
        byte[] buffer = new byte[5];
        int bytesRead = zipInput.read(buffer, 0, buffer.length);
        assertEquals(5, bytesRead);
        assertEquals(0x74, buffer[0]);
        assertEquals(0x65, buffer[1]);
        assertEquals(0x73, buffer[2]);
        assertEquals(0x74, buffer[3]);
        assertEquals(0x20, buffer[4]);
    }
    
    @Test
    public void testClose() throws IOException {
        zipInput.close();
        assertNull(zipInput.getNextEntry());
    }
    
    @Test(expected = IOException.class)
    public void testSkip_InvalidValue() throws IOException {
        zipInput.getNextEntry();
        zipInput.skip(-1);
    }
    
    @Test
    public void testSkip() throws IOException {
        zipInput.getNextEntry();
        long skippedBytes = zipInput.skip(5);
        assertEquals(5, skippedBytes);
        byte[] buffer = new byte[10];
        int bytesRead = zipInput.read(buffer, 0, buffer.length);
        assertEquals(5, bytesRead);
        assertEquals(0x20, buffer[0]);
        assertEquals(0x66, buffer[1]);
        assertEquals(0x69, buffer[2]);
        assertEquals(0x6c, buffer[3]);
        assertEquals(0x65, buffer[4]);
        assertEquals(0x20, buffer[5]);
        assertEquals(0x74, buffer[6]);
        assertEquals(0x6f, buffer[7]);
        assertEquals(0x20, buffer[8]);
        assertEquals(0x73, buffer[9]);
    }
    
    @Test
    public void testMatches_NormalFile() {
        byte[] signature = new byte[] {0x50, 0x4b, 0x03, 0x04};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test
    public void testMatches_EmptyZip() {
        byte[] signature = new byte[] {0x50, 0x4b, 0x05, 0x06};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    private byte[] getTestZipData() {
        // Create a zip file with a single directory entry
        byte[] zipData = new byte[] {
            0x50, 0x4b, 0x03, 0x04, 0x0a, 0x00, 0x00, 0x00, 0x00, 0x00, 
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
            0x00, 0x00, 0x1b, 0x00, 0x00, 0x00, 0x07, 0x00, 0x00, 0x00, 
            0x74, 0x65, 0x73, 0x74, 0x2e, 0x74, 0x78, 0x74, 0x5a, 0x69, 
            0x70, 0x45, 0x64, 0x69, 0x74, 0x65, 0x64, 0x20, 0x6c, 0x65, 
            0x61, 0x64, 0x44, 0x61, 0x74, 0x61, 0x2e, 0x74, 0x78, 0x74, 
            0xe0, 0x31, 0x32, 0x35, 0x2a, 0xa7, 0x31, 0x53, 0x10, 0xb3, 
            0x10, 0x0c, 0x37, 0x3b, 0xa4, 0x50, 0x02, 0x11, 0x42, 0xa9, 
            0x64, 0x50, 0x02, 0x11, 0x42, 0xa9, 0x64, 0x6c, 0x82, 0x31, 
            0x80, 0x31, 0x50, 0x4b, 0x05, 0x06, 0x00, 0x00, 0x00, 0x00, 
            0x01, 0x00, 0x01, 0x00, 0x45, 0x00, 0x00, 0x00, 0x00, 0x00
        };
        return zipData;
    }
}