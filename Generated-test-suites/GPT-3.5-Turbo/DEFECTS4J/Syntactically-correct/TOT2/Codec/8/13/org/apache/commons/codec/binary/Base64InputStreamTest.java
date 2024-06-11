import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values of 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertEquals(65, base64InputStream.read());
        assertEquals(66, base64InputStream.read());
        assertEquals(67, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = {65, 66, 67}; // ASCII values of 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[5];
        int bytesRead = base64InputStream.read(output, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 65, 66, 67, 0}, output);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(new byte[5], -1, 1);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}));

        assertFalse(base64InputStream.markSupported());
    }
}