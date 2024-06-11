
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private byte[] testData = "Test Data".getBytes();

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(testData);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(84, result); // ASCII value for 'T'
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] result = new byte[4];
        int bytesRead = base64InputStream.read(result, 0, 4);
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{84, 101, 115, 116}, result);
    }

    @Test
    public void testReadWithNullByte() throws IOException {
        InputStream nullInputStream = new ByteArrayInputStream(new byte[]{127});
        Base64InputStream nullByteStream = new Base64InputStream(nullInputStream);
        int result = nullByteStream.read();
        assertEquals(127, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] result = new byte[4];
        base64InputStream.read(result, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] result = new byte[4];
        base64InputStream.read(result, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferLength() throws IOException {
        byte[] result = new byte[4];
        base64InputStream.read(result, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferLengthAndOffset() throws IOException {
        byte[] result = new byte[4];
        base64InputStream.read(result, 1, 4);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}
