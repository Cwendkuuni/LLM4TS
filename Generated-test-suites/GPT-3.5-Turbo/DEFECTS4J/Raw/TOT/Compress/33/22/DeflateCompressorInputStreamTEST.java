import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.utils.IOUtils;

import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        String content = "This is a test input stream.";
        byte[] compressed = compress(content.getBytes());
        InputStream input = new ByteArrayInputStream(compressed);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(input);
        
        byte[] output = new byte[content.length()];
        int bytesRead = decompressor.read(output, 0, output.length);
        decompressor.close();
        
        String decompressed = new String(output);
        
        assertEquals(content, decompressed);
        assertEquals(content.length(), bytesRead);
    }
    
    @Test
    public void testReadEmpty() throws IOException {
        byte[] compressed = new byte[0];
        InputStream input = new ByteArrayInputStream(compressed);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(input);
        
        byte[] output = new byte[10];
        int bytesRead = decompressor.read(output, 0, output.length);
        decompressor.close();
        
        assertEquals(-1, bytesRead);
    }
    
    @Test
    public void testSkip() throws IOException {
        String content = "This is a test input stream.";
        byte[] compressed = compress(content.getBytes());
        InputStream input = new ByteArrayInputStream(compressed);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(input);
        
        long skipped = decompressor.skip(10);
        
        byte[] output = new byte[content.length() - 10];
        int bytesRead = decompressor.read(output, 0, output.length);
        decompressor.close();
        
        String decompressed = new String(output);
        
        assertEquals(content.substring(10), decompressed);
        assertEquals(content.length() - 10, bytesRead);
        assertEquals(10, skipped);
    }
    
    @Test
    public void testAvailable() throws IOException {
        String content = "This is a test input stream.";
        byte[] compressed = compress(content.getBytes());
        InputStream input = new ByteArrayInputStream(compressed);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(input);
        
        int available = decompressor.available();
        decompressor.close();
        
        assertEquals(content.length(), available);
    }
    
    @Test
    public void testClose() throws IOException {
        String content = "This is a test input stream.";
        byte[] compressed = compress(content.getBytes());
        InputStream input = new ByteArrayInputStream(compressed);
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(input);
        
        decompressor.close();
        
        try {
            decompressor.read();
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected exception
        }
    }
    
    private byte[] compress(byte[] input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorOutputStream compressor = new DeflateCompressorOutputStream(output, parameters);
        compressor.write(input);
        compressor.close();
        return output.toByteArray();
    }
}