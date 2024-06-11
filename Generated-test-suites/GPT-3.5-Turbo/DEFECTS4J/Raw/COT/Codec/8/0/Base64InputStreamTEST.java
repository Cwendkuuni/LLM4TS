import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        // Given
        byte[] data = "SGVsbG8gd29ybGQ=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int result = base64InputStream.read();

        // Then
        assertEquals(72, result);
    }

    @Test
    public void testReadWithEmptyData() throws IOException {
        // Given
        byte[] data = "".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int result = base64InputStream.read();

        // Then
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithNullByteArray() throws IOException {
        // Given
        byte[] data = "SGVsbG8gd29ybGQ=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int result = base64InputStream.read(null, 0, 10);

        // Then
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullInputStream() throws IOException {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(null);

        // When
        base64InputStream.read();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        // Given
        byte[] data = "SGVsbG8gd29ybGQ=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        base64InputStream.read(data, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        // Given
        byte[] data = "SGVsbG8gd29ybGQ=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        base64InputStream.read(data, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        // Given
        byte[] data = "SGVsbG8gd29ybGQ=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        base64InputStream.read(new byte[20], 0, 30);
    }

    @Test
    public void testReadWithEncode() throws IOException {
        // Given
        byte[] data = "Hello world".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // When
        int result = base64InputStream.read();

        // Then
        assertEquals(72, result);
    }

    @Test
    public void testReadWithDecode() throws IOException {
        // Given
        byte[] data = "72 101 108 108 111 32 119 111 114 108 100".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // When
        int result = base64InputStream.read();

        // Then
        assertEquals(72, result);
    }

    @Test
    public void testMarkSupported() {
        // Given
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));

        // When
        boolean result = base64InputStream.markSupported();

        // Then
        assertFalse(result);
    }
}