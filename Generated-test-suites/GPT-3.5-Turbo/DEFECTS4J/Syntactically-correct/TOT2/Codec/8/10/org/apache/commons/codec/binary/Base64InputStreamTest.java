import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        int actual = base64InputStream.read();
        assertEquals('H', actual);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] testData = "Hello, World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[5];
        int actual = base64InputStream.read(buffer, 0, 5);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(new byte[5], 6, 5);
    }

    @Test(expected = IOException.class)
    public void testReadWithIOException() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.read()).thenThrow(new IOException());
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        base64InputStream.read();
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        assertFalse(base64InputStream.markSupported());
    }
}