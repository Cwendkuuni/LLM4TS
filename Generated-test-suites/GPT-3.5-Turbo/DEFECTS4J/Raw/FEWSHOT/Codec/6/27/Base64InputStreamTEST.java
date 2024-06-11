import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    private Base64InputStream base64InputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68, 69 };
        byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        base64InputStream = new Base64InputStream(byteArrayInputStream, true);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(65, base64InputStream.read());
        assertEquals(66, base64InputStream.read());
        assertEquals(67, base64InputStream.read());
        assertEquals(68, base64InputStream.read());
        assertEquals(69, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[3];
        assertEquals(3, base64InputStream.read(buffer, 0, 3));
        assertArrayEquals(new byte[] { 65, 66, 67 }, buffer);
        assertEquals(2, base64InputStream.read(buffer, 0, 3));
        assertArrayEquals(new byte[] { 68, 69, 67 }, buffer);
        assertEquals(-1, base64InputStream.read(buffer, 0, 3));
        assertEquals(0, base64InputStream.read(buffer, 0, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] buffer = new byte[3];
        base64InputStream.read(buffer, 1, 3);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}