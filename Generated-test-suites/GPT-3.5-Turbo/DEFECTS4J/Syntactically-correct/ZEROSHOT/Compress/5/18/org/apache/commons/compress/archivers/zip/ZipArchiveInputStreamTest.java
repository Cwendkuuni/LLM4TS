import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.ZipException;

public class ZipArchiveInputStreamTest {
    
    @Test
    public void testGetNextZipEntry() {
        try {
            InputStream inputStream = new FileInputStream("test.zip");
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry entry = zipInput.getNextZipEntry();
            
            assertNotNull(entry);
        } catch(IOException e) {
            fail("An unexpected IOException occurred.");
        }
    }
    
    @Test
    public void testRead() {
        try {
            InputStream inputStream = new FileInputStream("test.zip");
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry entry = zipInput.getNextZipEntry();
            
            byte[] buffer = new byte[1024];
            int bytesRead = zipInput.read(buffer, 0, buffer.length);
            
            assertEquals(entry.getSize(), bytesRead);
        } catch(IOException e) {
            fail("An unexpected IOException occurred.");
        }
    }
    
    @Test
    public void testSkip() {
        try {
            InputStream inputStream = new FileInputStream("test.zip");
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream);
            ZipArchiveEntry entry = zipInput.getNextZipEntry();
            
            long size = entry.getSize();
            long skippedBytes = zipInput.skip(size);
            
            assertEquals(size, skippedBytes);
        } catch(IOException e) {
            fail("An unexpected IOException occurred.");
        }
    }
    
    @Test
    public void testClose() {
        try {
            InputStream inputStream = new FileInputStream("test.zip");
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream);
            
            zipInput.close();
            
            assertTrue(zipInput.isClosed());
        } catch(IOException e) {
            fail("An unexpected IOException occurred.");
        }
    }
    
    @Test
    public void testIsClosed() {
        try {
            InputStream inputStream = new FileInputStream("test.zip");
            ZipArchiveInputStream zipInput = new ZipArchiveInputStream(inputStream);
            
            assertFalse(zipInput.isClosed());
        } catch(IOException e) {
            fail("An unexpected IOException occurred.");
        }
    }
    
}