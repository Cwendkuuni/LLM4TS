import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {
    
    // Test read() method
    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int expected;
        int actual = base64InputStream.read();
        
        assertEquals(72, actual); // 'H' in ASCII
        
        base64InputStream.close();
    }

    // Test read(byte[], int, int) method
    @Test
    public void testReadBuffer() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int read = base64InputStream.read(buffer, 0, 5);
        
        assertEquals("Hello", new String(buffer));
        
        base64InputStream.close();
    }

    // Test read with empty buffer
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadEmptyBuffer() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        byte[] buffer = new byte[0];
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(buffer, 0, 0);
        
        base64InputStream.close();
    }

    // Test read with null buffer
    @Test(expected = NullPointerException.class)
    public void testReadNullBuffer() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        base64InputStream.read(null, 0, 5);
        
        base64InputStream.close();
    }

    // Test markSupported() method
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        boolean result = base64InputStream.markSupported();
        
        assertFalse(result);
        
        base64InputStream.close();
    }
}