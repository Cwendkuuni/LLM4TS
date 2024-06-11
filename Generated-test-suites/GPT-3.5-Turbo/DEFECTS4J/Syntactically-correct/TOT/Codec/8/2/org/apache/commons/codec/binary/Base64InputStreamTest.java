import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private byte[] inputBytes;

    @Before
    public void setUp() {
        // Create a sample byte array for testing
        inputBytes = "Hello, World!".getBytes();
    }

    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        // Read the data from the Base64 input stream and decode it
        byte[] decodedBytes = new byte[inputBytes.length];
        int bytesRead = base64InputStream.read(decodedBytes);

        assertArrayEquals(inputBytes, decodedBytes);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        int offset = 3;
        int length = 5;
        byte[] decodedBytes = new byte[length];
        int bytesRead = base64InputStream.read(decodedBytes, offset, length);

        byte[] expectedBytes = new byte[length];
        System.arraycopy(inputBytes, 0, expectedBytes, offset, length);

        assertArrayEquals(expectedBytes, decodedBytes);
        assertEquals(length, bytesRead);
    }

    @Test
    public void testReadWithEmptyInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        base64InputStream = new Base64InputStream(inputStream);

        byte[] decodedBytes = new byte[inputBytes.length];
        int bytesRead = base64InputStream.read(decodedBytes);

        assertArrayEquals(new byte[inputBytes.length], decodedBytes);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWithPartialReads() throws IOException {
        byte[] base64Data = Base64.encodeBase64Chunked(inputBytes, 2, 6, Base64.CRLF_CHUNK_SEPARATOR);
        InputStream inputStream = new ByteArrayInputStream(base64Data);
        base64InputStream = new Base64InputStream(inputStream);

        byte[] decodedBytes = new byte[inputBytes.length];
        int totalBytesRead = 0;
        int bytesRead;

        while (totalBytesRead < inputBytes.length && (bytesRead = base64InputStream.read(decodedBytes, totalBytesRead, inputBytes.length - totalBytesRead)) > 0) {
            totalBytesRead += bytesRead;
        }

        assertArrayEquals(inputBytes, decodedBytes);
        assertEquals(inputBytes.length, totalBytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        byte[] decodedBytes = new byte[inputBytes.length];
        base64InputStream.read(decodedBytes, -1, inputBytes.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        byte[] decodedBytes = new byte[inputBytes.length];
        base64InputStream.read(decodedBytes, decodedBytes.length, decodedBytes.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        int offset = 0;
        int length = inputBytes.length + 1;
        byte[] decodedBytes = new byte[length];
        base64InputStream.read(decodedBytes, offset, length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLengthAndOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        int offset = 1;
        int length = inputBytes.length;
        byte[] decodedBytes = new byte[length];
        base64InputStream.read(decodedBytes, offset, length);
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }
}