
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        int actual = compressorInputStream.read();
        int expected = 1;
        
        assertEquals(expected, actual);
    }

    @Test
    public void testRead_withBuffer() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[3];
        int bytesRead = compressorInputStream.read(buffer, 0, buffer.length);
        
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        assertEquals(3, bytesRead);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        long skipped = compressorInputStream.skip(2);
        assertEquals(2, skipped);
        
        int bytesRead = compressorInputStream.read();
        assertEquals(3, bytesRead);
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        int available = compressorInputStream.available();
        assertEquals(5, available);
        
        compressorInputStream.read();
        
        available = compressorInputStream.available();
        assertEquals(4, available);
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream compressorInputStream = new DeflateCompressorInputStream(inputStream);
        
        compressorInputStream.close();
        
        try {
            int bytesRead = compressorInputStream.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test
    public void testCheckSignature_withValidSignature() {
        byte[] input = {31, -117, 8};
        boolean isZlib = DeflateCompressorInputStream.checkSignature(input, input.length);
        assertTrue(isZlib);
    }
    
    @Test
    public void testCheckSignature_withInvalidSignature() {
        byte[] input = {1, 2, 3};
        boolean isZlib = DeflateCompressorInputStream.checkSignature(input, input.length);
        assertFalse(isZlib);
    }
}

