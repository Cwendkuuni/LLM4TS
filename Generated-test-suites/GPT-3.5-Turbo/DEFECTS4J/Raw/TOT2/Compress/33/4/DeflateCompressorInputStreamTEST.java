import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(input));
        
        assertEquals(0, input.read());
        assertEquals(1, input.read());
        assertEquals(2, input.read());
        assertEquals(3, input.read());
        assertEquals(4, input.read());
        
        assertEquals(-1, input.read());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        byte[] buffer = new byte[3];
        
        int bytesRead = input.read(buffer, 0, 3);
        
        assertEquals(3, bytesRead);
        
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        long skipped = input.skip(3);
        
        assertEquals(3, skipped);
        
        assertEquals(3, input.read());
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0, 1, 2, 3, 4};
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        assertEquals(data.length, input.available());
        
        input.read();
        
        assertEquals(data.length - 1, input.available());
    }
    
    @Test
    public void testClose() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[5]);
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(stream);
        
        try {
            input.close();
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        
        try {
            stream.read();
            fail("Stream should be closed");
        } catch (IOException e) {
            // Expected
        }
    }
    
    @Test
    public void testZlibHeader() {
        byte[] data = {0x78, -116, 67, 34}; // zlib header
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        assertTrue(input.zlibHeader());
    }
    
    @Test
    public void testNonZlibHeader() {
        byte[] data = {1, 2, 3, 4}; // non zlib header
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        assertFalse(input.zlibHeader());
    }
}