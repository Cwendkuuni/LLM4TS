import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

public class Base64InputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] data = Base64.encodeBase64("test".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals('t', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('s', base64InputStream.read());
        assertEquals('t', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = Base64.encodeBase64("test".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 4);
        
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] data = Base64.encodeBase64("test".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[4];
        
        try {
            base64InputStream.read(buffer, -1, 4);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Invalid offset", e.getMessage());
        }
        
        try {
            base64InputStream.read(buffer, 0, 5);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Invalid length", e.getMessage());
        }
        
        try {
            base64InputStream.read(buffer, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Invalid length", e.getMessage());
        }
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithNullBuffer() throws IOException {
        byte[] data = Base64.encodeBase64("test".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        try {
            base64InputStream.read(null, 0, 4);
        } catch (NullPointerException e) {
            assertEquals("Buffer cannot be null", e.getMessage());
        }
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] data = Base64.encodeBase64("test".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[0];
        assertEquals(0, base64InputStream.read(buffer, 0, 0));
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithEndOfFile() throws IOException {
        byte[] data = Base64.encodeBase64("test".getBytes());
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 4);
        
        assertEquals(-1, base64InputStream.read(buffer, 0, 4));
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithEmptyInput() throws IOException {
        byte[] data = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[4];
        assertEquals(-1, base64InputStream.read(buffer, 0, 4));
        
        base64InputStream.close();
    }
    
    @Test
    public void testReadWithInvalidBase64Input() throws IOException {
        byte[] data = "invalid".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] buffer = new byte[4];
        assertEquals(-1, base64InputStream.read(buffer, 0, 4));
        
        base64InputStream.close();
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        assertEquals(false, base64InputStream.markSupported());
        
        base64InputStream.close();
    }
}