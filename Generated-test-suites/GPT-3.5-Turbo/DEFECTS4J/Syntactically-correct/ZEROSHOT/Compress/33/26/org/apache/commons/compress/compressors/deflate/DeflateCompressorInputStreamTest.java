import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateInputStream;
    
    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        deflateInputStream = new DeflateCompressorInputStream(inputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        int data = deflateInputStream.read();
        assertEquals('T', data);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        int length = deflateInputStream.read(buffer, 0, 5);
        assertEquals(5, length);
        assertEquals("Test ", new String(buffer, 0, length));
    }
    
    @Test
    public void testSkip() throws IOException {
        long skipped = deflateInputStream.skip(5);
        assertEquals(5, skipped);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int available = deflateInputStream.available();
        assertTrue(available > 0);
    }
    
    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
    }
    
    @Test
    public void testDeflateParameters() {
        DeflateParameters parameters = new DeflateParameters();
        InputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);
    }
}