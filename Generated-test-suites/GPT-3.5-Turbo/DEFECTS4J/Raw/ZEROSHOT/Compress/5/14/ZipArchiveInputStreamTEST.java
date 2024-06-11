import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ZipArchiveInputStreamTest {
    
    private ZipArchiveInputStream zipArchiveInputStream;
    
    @Before
    public void setUp() throws Exception {
        byte[] testData = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(testData);
        zipArchiveInputStream = new ZipArchiveInputStream(inputStream);
    }
    
    @After
    public void tearDown() throws Exception {
        zipArchiveInputStream.close();
    }
    
    @Test
    public void getNextZipEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextZipEntry());
    }
    
    @Test
    public void getNextEntry() throws IOException {
        assertNull(zipArchiveInputStream.getNextEntry());
    }
    
    @Test
    public void read() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(-1, zipArchiveInputStream.read(buffer, 0, 5));
    }
    
    @Test
    public void close() throws IOException {
        zipArchiveInputStream.close();
        assertTrue(zipArchiveInputStream.closed);
    }
    
    @Test
    public void skip() throws IOException {
        assertEquals(0, zipArchiveInputStream.skip(10));
    }
    
    @Test
    public void matches() {
        byte[] signature = new byte[5];
        assertFalse(zipArchiveInputStream.matches(signature, 5));
    }
    
    @Test
    public void checksig() {
        byte[] signature = new byte[5];
        byte[] expected = new byte[5];
        assertFalse(zipArchiveInputStream.checksig(signature, expected));
    }
    
    @Test
    public void closeEntry() throws IOException {
        zipArchiveInputStream.closeEntry();
        assertNull(zipArchiveInputStream.current);
    }
    
    @Test
    public void fill() throws IOException {
        zipArchiveInputStream.fill();
        assertEquals(0, zipArchiveInputStream.lengthOfLastRead);
    }
    
    @Test
    public void readFully() throws IOException {
        byte[] b = new byte[10];
        zipArchiveInputStream.readFully(b);
        assertArrayEquals(new byte[10], b);
    }
}