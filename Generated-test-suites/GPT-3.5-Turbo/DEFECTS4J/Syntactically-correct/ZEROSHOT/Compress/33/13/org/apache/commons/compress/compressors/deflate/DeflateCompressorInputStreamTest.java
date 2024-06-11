import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(input);
        
        int byteRead = inputStream.read();
        
        assertEquals(1, byteRead);
        
        inputStream.close();
    }
    
    @Test
    public void testReadBuffer() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(input);
        
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer, 0, buffer.length);
        
        assertEquals(3, bytesRead);
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        
        inputStream.close();
    }
    
    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(input);
        
        long bytesSkipped = inputStream.skip(3);
        
        assertEquals(3, bytesSkipped);
        
        inputStream.close();
    }
    
    @Test
    public void testAvailable() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(input);
        
        int available = inputStream.available();
        
        assertEquals(5, available);
        
        inputStream.close();
    }
    
    @Test
    public void testClose() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(data);
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(input);
        
        inputStream.close();
        
        assertTrue(inputStream.in.isClosed());
    }
    
}