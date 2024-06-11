import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64 base64;
    private InputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        base64 = new Base64();
        byte[] data = "Hello, World!".getBytes();
        inputStream = new ByteArrayInputStream(base64.encode(data));
    }

    @Test
    public void testConstructorWithoutEncode() {
        base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructorWithEncode() {
        base64InputStream = new Base64InputStream(inputStream, true);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testConstructorWithEncodeAndLineLength() {
        base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        assertNotNull(base64InputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullInputStream() {
        base64InputStream = new Base64InputStream(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testConstructorNegativeLineLength() {
        base64InputStream = new Base64InputStream(inputStream, true, -1, "\r\n".getBytes());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullLineSeparator() {
        base64InputStream = new Base64InputStream(inputStream, true, 76, null);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorEmptyLineSeparator() {
        base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[]{});
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[10];
        base64InputStream.read(data, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[10];
        base64InputStream.read(data, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferLength() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);
        byte[] data = new byte[10];
        base64InputStream.read(data, 0, 20);
    }

    // Additional test cases for the remaining methods

    @Test
    public void testMarkSupported() {
        base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}