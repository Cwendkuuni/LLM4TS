import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int data = base64InputStream.read();
        assertEquals('T', data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = "Test Data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[4];

        int bytesRead = base64InputStream.read(data, 0, 4);
        String result = new String(data);
        assertEquals("Test", result);
    }

    @Test(expected = IOException.class)
    public void tesReadWithNullArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void tesReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[4];
        base64InputStream.read(data, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void tesReadWithInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[4];
        base64InputStream.read(data, 0, 6);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("Test Data".getBytes()));
        assertFalse(base64InputStream.markSupported());
    }
}