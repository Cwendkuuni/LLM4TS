import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class DeflateCompressorInputStreamTest {

    // Test cases for read method
    @Test
    public void testRead() throws IOException {
        byte[] input = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream);
        
        int value = deflateInput.read();
        assertEquals(0x01, value);
        
        value = deflateInput.read();
        assertEquals(0x02, value);
        
        value = deflateInput.read();
        assertEquals(0x03, value);
        
        value = deflateInput.read();
        assertEquals(0x04, value);
        
        value = deflateInput.read();
        assertEquals(-1, value);
        
        deflateInput.close();
    }
    
    // Test cases for read method with byte array
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] input = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream);
        
        byte[] buffer = new byte[2];
        int bytesRead = deflateInput.read(buffer, 0, 2);
        assertEquals(2, bytesRead);
        assertEquals(0x01, buffer[0]);
        assertEquals(0x02, buffer[1]);
        
        bytesRead = deflateInput.read(buffer, 0, 2);
        assertEquals(2, bytesRead);
        assertEquals(0x03, buffer[0]);
        assertEquals(0x04, buffer[1]);
        
        bytesRead = deflateInput.read(buffer, 0, 2);
        assertEquals(-1, bytesRead);
        
        deflateInput.close();
    }
    
    // Test case for skip method
    @Test
    public void testSkip() throws IOException {
        byte[] input = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream);
        
        long skipped = deflateInput.skip(2);
        assertEquals(2, skipped);
        
        int value = deflateInput.read();
        assertEquals(0x03, value);
        
        skipped = deflateInput.skip(2);
        assertEquals(1, skipped);
        
        value = deflateInput.read();
        assertEquals(-1, value);
        
        deflateInput.close();
    }
    
    // Test case for available method
    @Test
    public void testAvailable() throws IOException {
        byte[] input = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream);
        
        int available = deflateInput.available();
        assertEquals(4, available);
        
        deflateInput.read();
        available = deflateInput.available();
        assertEquals(3, available);
        
        deflateInput.close();
    }
    
    // Test case for close method
    @Test
    public void testClose() throws IOException {
        byte[] input = { 0x01, 0x02, 0x03, 0x04 };
        InputStream inputStream = new ByteArrayInputStream(input);
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream);
        
        deflateInput.read();
        deflateInput.close();
        
        // Verify that the input stream is closed
        assertTrue(deflateInput.isClosed());
    }
    
    // Test case for zlib / deflated file signature
    @Test
    public void testCheckSignature() {
        byte[] signature = { (byte) 0x78, (byte) 0x9C };
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(null);
        
        boolean result = deflateInput.checkSignature(signature, 2);
        assertTrue(result);
        
        signature[0] = (byte) 0x00;
        result = deflateInput.checkSignature(signature, 2);
        assertFalse(result);
        
        deflateInput.close();
    }
}