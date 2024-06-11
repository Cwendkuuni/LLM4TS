import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        int result = base64InputStream.read();

        assertEquals('T', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[4];
        int bytesRead = base64InputStream.read(buffer, 0, 4);

        assertArrayEquals("Test".getBytes(), buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        base64InputStream.read(new byte[4], -1, 4);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Test Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        assertFalse(base64InputStream.markSupported());
    }
}