import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testConstructor() {
        InputStream inputStream = new InputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() {
        InputStream inputStream = new InputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadBytes() {
        InputStream inputStream = new InputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 0, 10);
        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new InputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}