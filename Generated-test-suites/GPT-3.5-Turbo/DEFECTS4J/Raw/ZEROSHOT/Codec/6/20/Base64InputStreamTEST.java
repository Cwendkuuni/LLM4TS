import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        base64InputStream = new Base64InputStream(null);
    }

    @Test
    public void testRead() throws Exception {
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws Exception {
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws Exception {
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws Exception {
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws Exception {
        byte[] byteArray = new byte[10];
        base64InputStream.read(byteArray, 0, 11);
    }

    @Test
    public void testReadWithEmptyBuffer() throws Exception {
        byte[] byteArray = new byte[0];
        int result = base64InputStream.read(byteArray, 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testReadResults() throws Exception {
        byte[] byteArray = new byte[10];
        int result = base64InputStream.read(byteArray, 0, 10);
        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}