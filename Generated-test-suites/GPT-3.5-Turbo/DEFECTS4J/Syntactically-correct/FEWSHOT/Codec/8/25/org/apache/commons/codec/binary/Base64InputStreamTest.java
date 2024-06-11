import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] testData = Base64.encodeBase64("Hello, World!".getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int bytesRead = 0;
        int totalBytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            totalBytesRead += bytesRead;
        }
        
        assertEquals(testData.length, totalBytesRead);
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] testData = Base64.encodeBase64("Hello, World!".getBytes());
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int bytesRead = 0;
        int totalBytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = base64InputStream.read(buffer, 5, 5)) != -1) {
            totalBytesRead += bytesRead;
        }
        
        assertEquals(10, totalBytesRead);
    }
    
    @Test
    public void testReadEmpty() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int bytesRead = base64InputStream.read();
        assertEquals(-1, bytesRead);
        
        bytesRead = base64InputStream.read(new byte[10]);
        assertEquals(-1, bytesRead);
        
        bytesRead = base64InputStream.read(new byte[10], 0, 10);
        assertEquals(-1, bytesRead);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[10], -1, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[10], 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[10], 5, 6);
    }
    
    @Test(expected = IOException.class)
    public void testReadIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read(byte[] b) throws IOException {
                throw new IOException();
            }
        };
        
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[10]);
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        assertFalse(base64InputStream.markSupported());
    }
}