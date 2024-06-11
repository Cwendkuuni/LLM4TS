import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {0x78, 0x01, 0x00}; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        int result = input.read();
        
        assertEquals(0, result);
        input.close();
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {0x78, 0x9c, 0x03, 0x00, 0x00}; // Deflate compressed data
        byte[] buffer = new byte[1024];
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        int result = input.read(buffer, 0, 3);
        
        assertEquals(3, result);
        assertEquals((byte)0x00, buffer[0]);
        assertEquals((byte)0x00, buffer[1]);
        assertEquals((byte)0x00, buffer[2]);
        input.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x78, 0x01, 0x00}; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        long result = input.skip(2);
        
        assertEquals(2, result);
        input.close();
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = {0x78, 0x01, 0x00}; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        int result = input.available();
        
        assertTrue(result > 0);
        input.close();
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] data = {0x78, 0x01, 0x00}; // Deflate compressed data
        DeflateCompressorInputStream input = new DeflateCompressorInputStream(new ByteArrayInputStream(data));
        
        input.close();
        
        assertFalse(input.read() > 0);
    }
    
}