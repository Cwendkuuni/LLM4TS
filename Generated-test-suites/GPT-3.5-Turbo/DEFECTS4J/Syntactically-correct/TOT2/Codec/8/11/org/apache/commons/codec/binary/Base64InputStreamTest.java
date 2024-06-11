import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = "Test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        assertEquals('T', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = "Test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[4];
        int result = base64InputStream.read(buffer, 0, 4);

        assertArrayEquals(data, buffer);
        assertEquals(4, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        byte[] data = "Test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        byte[] data = "Test".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, -1, 4);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(new Base64InputStream(new ByteArrayInputStream(new byte[0])).markSupported());
    }
}