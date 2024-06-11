import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadByteArrayIntInt() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
        byte[] result = new byte[10];
        int readLen = base64InputStream.read(result, 0, 10);
        assertEquals(0, readLen);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
        boolean markSupported = base64InputStream.markSupported();
        assertFalse(markSupported);
    }
}