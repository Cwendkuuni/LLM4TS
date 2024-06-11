import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{65});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int result = base64InputStream.read();
        assertEquals(65, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{65, 66, 67, 68});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 4);
        assertArrayEquals(new byte[]{65, 66, 67, 68}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{65}), false);
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{65}), false);
        base64InputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{65}), false);
        base64InputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferLength() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{65}), false);
        base64InputStream.read(new byte[1], 0, 2);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{65}), false);
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testEndOfFile() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{65});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read();
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testEmptyByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertEquals(-1, base64InputStream.read());
    }
}