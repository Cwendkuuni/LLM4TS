import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testConstructor1() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructor2() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructor3() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, "\r\n".getBytes());
        assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() throws IOException {
        byte[] encodedData = Base64.encodeBase64("test".getBytes());
        InputStream in = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int b = base64InputStream.read();
        while (b != -1) {
            assertEquals("test".charAt(b), 't');
            b = base64InputStream.read();
        }
    }

    @Test
    public void testRead2() throws IOException {
        byte[] encodedData = Base64.encodeBase64("test".getBytes());
        InputStream in = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[4];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, 4);
        assertEquals(new String(buffer), "test");
    }
    
    @Test
    public void testRead3() throws IOException {
        byte[] encodedData = Base64.encodeBase64("test".getBytes());
        InputStream in = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[2];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, 2);
        assertEquals(new String(buffer), "te");
        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(bytesRead, 2);
        assertEquals(new String(buffer), "st");
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }
}