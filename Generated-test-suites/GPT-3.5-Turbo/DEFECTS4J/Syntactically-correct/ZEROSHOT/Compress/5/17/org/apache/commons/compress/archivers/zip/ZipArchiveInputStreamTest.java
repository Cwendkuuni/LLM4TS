import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;

public class ZipArchiveInputStreamTest {
    
    private ZipArchiveInputStream zipInput;
    
    @Before
    public void setUp() {
        InputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("test.zip");
            zipInput = new ZipArchiveInputStream(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetNextZipEntry() throws IOException {
        assertNotNull(zipInput.getNextZipEntry());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        assertNotNull(zipInput.getNextEntry());
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[1024];
        assertEquals(1024, zipInput.read(buffer, 0, 1024));
    }
    
    @Test
    public void testClose() throws IOException {
        zipInput.close();
        assertTrue(zipInput.isClosed());
    }
    
    @Test
    public void testSkip() throws IOException {
        assertEquals(10, zipInput.skip(10));
    }
    
    @Test
    public void testMatches() {
        byte[] signature = {80, 75, 3, 4};
        assertTrue(ZipArchiveInputStream.matches(signature, signature.length));
    }
    
    @Test
    public void testCloseEntry() throws IOException {
        zipInput.getNextZipEntry();
        zipInput.closeEntry();
        assertNull(zipInput.getCurrentEntry());
    }
    
    @Test
    public void testFill() throws IOException {
        assertEquals(1024, zipInput.fill());
    }
    
    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[1024];
        zipInput.readFully(buffer);
        assertArrayEquals(buffer, zipInput.getBuffer());
    }
    
}