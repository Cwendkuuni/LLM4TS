import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamRead() throws IOException {
        byte[] testData = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int expected = 'H'; // ASCII value of 'H'
        int result = base64InputStream.read();
        assertEquals(expected, result);
    }

    @Test
    public void testBase64InputStreamReadArray() throws IOException {
        byte[] testData = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 5);

        assertEquals(5, bytesRead);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamReadArrayNull() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadArrayInvalidOffset() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        base64InputStream.read(new byte[5], 6, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadArrayInvalidLength() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        base64InputStream.read(new byte[5], 0, 6);
    }

    @Test
    public void testBase64InputStreamMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(base64InputStream.markSupported());
    }
}