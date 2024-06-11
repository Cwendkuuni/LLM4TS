
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    // Test that the constructor initializes the inputStream correctly
    @Test
    public void testConstructor() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertNotNull(base64InputStream);
        assertEquals(inputStream, base64InputStream.in);
    }

    // Test that the read method returns the correct byte from the inputStream
    @Test
    public void testRead() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        assertEquals(116, result);
    }

    // Test that the read method reads the correct number of bytes into the buffer
    @Test
    public void testReadBuffer() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];

        int result = base64InputStream.read(buffer, 0, 4);

        assertEquals(4, result);
    }

    // Test that the read method throws an IOException when an I/O error occurs
    @Test(expected = IOException.class)
    public void testReadIOException() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Close the input stream to force an I/O error
        inputStream.close();

        base64InputStream.read();
    }

    // Test that the markSupported method returns false
    @Test
    public void testMarkSupported() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        boolean result = base64InputStream.markSupported();

        assertFalse(result);
    }
}

