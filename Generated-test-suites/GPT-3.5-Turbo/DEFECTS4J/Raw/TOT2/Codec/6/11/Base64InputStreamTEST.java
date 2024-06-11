import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = new byte[] { 65, 66, 67 }; // ASCII values for 'A', 'B', 'C'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        assertEquals(65, result); // 'A' should be read as the first byte
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();

        assertEquals(-1, result); // EOF should be returned for an empty input stream
    }

    @Test
    public void testReadWithNullByteArray() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[3]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertThrows(NullPointerException.class, () -> {
            base64InputStream.read(null, 0, 3);
        });
    }

    @Test
    public void testReadWithInvalidOffset() {
        byte[] buffer = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[3]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, -1, 3);
        });
    }

    @Test
    public void testReadWithInvalidLength() {
        byte[] buffer = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[3]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, 0, -1);
        });
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[3]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        boolean result = base64InputStream.markSupported();

        assertFalse(result); // mark should not be supported
    }
}