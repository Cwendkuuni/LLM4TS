import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byte[] testData = "Test Data".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(testData);
        base64InputStream = new Base64InputStream(byteArrayInputStream, false);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(84, result);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 3, 5);
        assertArrayEquals(new byte[]{0, 0, 0, 84, 101, 115, 116, 0, 0, 0}, buffer);
        assertEquals(5, result);
    }

    @Test
    public void testReadWithDataEncode() throws IOException {
        base64InputStream = new Base64InputStream(byteArrayInputStream, true);
        int result = base64InputStream.read();
        assertEquals(47, result);
    }

    @Test
    public void testReadWithDataEncodeAndOffset() throws IOException {
        base64InputStream = new Base64InputStream(byteArrayInputStream, true);
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 3, 5);
        assertArrayEquals(new byte[]{0, 0, 0, 47, 53, 86, 90, 91, 92, 0}, buffer);
        assertEquals(5, result);
    }

    @Test
    public void testReadWithDataEncodeLineLength() throws IOException {
        byte[] testData = "Test Data".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(testData);
        base64InputStream = new Base64InputStream(byteArrayInputStream, true, 10, null);
        int result = base64InputStream.read();
        assertEquals(47, result);
    }

    @Test
    public void testReadWithDataDecodeLineLength() throws IOException {
        byte[] testData = "Test Data".getBytes();
        byteArrayInputStream = new ByteArrayInputStream(testData);
        base64InputStream = new Base64InputStream(byteArrayInputStream, false, 10, null);
        int result = base64InputStream.read();
        assertEquals(61, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithExceededBuffer() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 5, 10);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

}