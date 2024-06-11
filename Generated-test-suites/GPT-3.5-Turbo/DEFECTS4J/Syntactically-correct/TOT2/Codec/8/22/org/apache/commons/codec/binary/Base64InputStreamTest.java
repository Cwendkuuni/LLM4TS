import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;

public class Base64InputStreamTest {

    @Test
    public void testConstructorWithoutEncode() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructorWithEncode() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testRead() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int result = base64InputStream.read();
        assertEquals(116, result);
    }

    @Test
    public void testReadArray() {
        byte[] buffer = new byte[4];
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int result = base64InputStream.read(buffer, 0, 4);
        assertEquals(4, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadArrayWithNullBuffer() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayWithInvalidOffset() throws Exception {
        byte[] buffer = new byte[4];
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(buffer, 5, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayWithInvalidLength() throws Exception {
        byte[] buffer = new byte[4];
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(buffer, 0, 5);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}