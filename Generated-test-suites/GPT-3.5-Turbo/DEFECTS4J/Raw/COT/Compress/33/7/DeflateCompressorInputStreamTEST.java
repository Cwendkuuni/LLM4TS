import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {
    
    private DeflateCompressorInputStream deflateCompressorInputStream;
    
    @Before
    public void setUp() {
        byte[] data = new byte[]{0x78, 0x01, 0x00, 0x00, 0x00, 0x00, 0x90, 0x34};
        deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
    }
    
    @Test
    public void testRead() throws IOException {
        int result = deflateCompressorInputStream.read();
        assertEquals(120, result);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[5];
        int result = deflateCompressorInputStream.read(buffer, 0, 5);
        assertEquals(5, result);
    }
    
    @Test
    public void testSkip() throws IOException {
        long result = deflateCompressorInputStream.skip(5);
        assertEquals(5, result);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int result = deflateCompressorInputStream.available();
        assertEquals(1, result);
    }
    
    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        // No exception expected, test passes if no exception is thrown
    }
}