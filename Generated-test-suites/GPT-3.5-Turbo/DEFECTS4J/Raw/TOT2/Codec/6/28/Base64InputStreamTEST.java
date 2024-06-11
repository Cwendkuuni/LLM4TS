import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private ByteArrayInputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] testData = "Hello, World!".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() {
        try {
            assertEquals('H', base64InputStream.read());
            assertEquals('e', base64InputStream.read());
            assertEquals('l', base64InputStream.read());
            assertEquals('l', base64InputStream.read());
            assertEquals('o', base64InputStream.read());
            assertEquals(',', base64InputStream.read());
            assertEquals(' ', base64InputStream.read());
            assertEquals('W', base64InputStream.read());
            assertEquals('o', base64InputStream.read());
            assertEquals('r', base64InputStream.read());
            assertEquals('l', base64InputStream.read());
            assertEquals('d', base64InputStream.read());
            assertEquals('!', base64InputStream.read());
            assertEquals(-1, base64InputStream.read());
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test
    public void testReadWithParameters() {
        try {
            byte[] buffer = new byte[5];
            int readBytes = base64InputStream.read(buffer, 0, buffer.length);
            assertEquals('H', buffer[0]);
            assertEquals('e', buffer[1]);
            assertEquals('l', buffer[2]);
            assertEquals('l', buffer[3]);
            assertEquals('o', buffer[4]);
            assertEquals(5, readBytes);
        } catch (IOException e) {
            fail("Should not throw IOException");
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullBufferRead() throws IOException {
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidOffsetRead() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 15, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidLengthRead() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 15);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

    // Additional test cases for edge cases and error scenarios can be added here

}