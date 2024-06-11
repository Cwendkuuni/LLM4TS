import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = { 72, 101, 108, 108, 111 }; // "Hello"
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64Input = new Base64InputStream(in);
        
        int result = base64Input.read();
        assertEquals(72, result);
        
        result = base64Input.read();
        assertEquals(101, result);
        
        result = base64Input.read();
        assertEquals(108, result);
        
        result = base64Input.read();
        assertEquals(108, result);
        
        result = base64Input.read();
        assertEquals(111, result);
        
        result = base64Input.read(); // should return -1 for EOF
        assertEquals(-1, result);
        
        base64Input.close();
    }

    @Test
    public void testReadWithOffsetAndLen() throws IOException {
        byte[] data = { 72, 101, 108, 108, 111 }; // "Hello"
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64Input = new Base64InputStream(in);
        
        byte[] buffer = new byte[3];
        int result = base64Input.read(buffer, 0, 3);
        assertEquals(3, result);
        assertArrayEquals(new byte[]{ 72, 101, 108 }, buffer);
        
        result = base64Input.read(buffer, 0, 3);
        assertEquals(2, result);
        assertArrayEquals(new byte[]{ 108, 111 }, buffer);
        
        result = base64Input.read(buffer, 0, 3); // should return -1 for EOF
        assertEquals(-1, result);
        
        base64Input.close();
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(in);
        
        boolean result = base64Input.markSupported();
        assertEquals(false, result);
        
        base64Input.close();
    }
}