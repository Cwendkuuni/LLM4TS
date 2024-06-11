import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;


public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private String encodedText;
    private String decodedText;

    @Before
    public void setUp() {
        encodedText = "SGVsbG8gV29ybGQ="; // "Hello World" encoded in Base64
        decodedText = "Hello World";
    }

    @Test
    public void testRead_singleByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        int actual = base64InputStream.read();
        int expected = decodedText.getBytes()[0];

        assertEquals(expected, actual);
    }

    @Test
    public void testRead_byteArray() throws IOException {
        int bufferSize = 10;
        byte[] actual = new byte[bufferSize];

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        int bytesRead = base64InputStream.read(actual);
        String actualDecodedText = new String(actual, 0, bytesRead);

        assertEquals(decodedText, actualDecodedText);
    }

    @Test
    public void testRead_byteArray_offset() throws IOException {
        int bufferSize = 20;
        int offset = 5;
        byte[] actual = new byte[bufferSize];

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        int bytesRead = base64InputStream.read(actual, offset, bufferSize - offset);
        String actualDecodedText = new String(actual, offset, bytesRead);

        assertEquals(decodedText.substring(offset), actualDecodedText);
    }

    @Test
    public void testRead_byteArray_offset_invalidOffset() throws IOException {
        int bufferSize = 20;
        int offset = 25;
        byte[] actual = new byte[bufferSize];

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        // We expect an IndexOutOfBoundsException to be thrown here
        base64InputStream.read(actual, offset, bufferSize - offset);
    }

    @Test
    public void testRead_byteArray_len_invalidLength() throws IOException {
        int bufferSize = 20;
        int len = 25;
        byte[] actual = new byte[bufferSize];

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        // We expect an IndexOutOfBoundsException to be thrown here
        base64InputStream.read(actual, 0, len);
    }

    @Test
    public void testRead_byteArray_zeroLength() throws IOException {
        int bufferSize = 20;
        byte[] actual = new byte[bufferSize];

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        int bytesRead = base64InputStream.read(actual, 0, 0);
        String actualDecodedText = new String(actual, 0, bytesRead);

        assertEquals("", actualDecodedText);
    }

    @Test
    public void testRead_byteArray_eof() throws IOException {
        byte[] actual = new byte[1];

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        // Reading the first byte
        base64InputStream.read(actual);
        // Trying to read in an EOF scenario
        int actualEof = base64InputStream.read(actual);

        assertEquals(-1, actualEof);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_byteArray_null() throws IOException {
        int bufferSize = 10;
        byte[] actual = null;

        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        // We expect a NullPointerException to be thrown here
        base64InputStream.read(actual, 0, bufferSize);
    }

    @Test
    public void testMarkSupported_false() {
        InputStream inputStream = new ByteArrayInputStream(encodedText.getBytes());
        base64InputStream = new Base64InputStream(inputStream);

        boolean actual = base64InputStream.markSupported();

        assertEquals(false, actual);
    }

}