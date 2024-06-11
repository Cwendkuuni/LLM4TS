import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        int result = stream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadByteArray() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        int result = stream.read(buffer, 0, 10);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayNullBuffer() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        stream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffset() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        stream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidLength() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        stream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidBufferLength() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        byte[] buffer = new byte[10];
        stream.read(buffer, 0, 11);
    }

    @Test
    public void testReadByteArrayEmptyBuffer() throws Exception {
        Base64InputStream stream = new Base64InputStream(null);
        byte[] buffer = new byte[0];
        int result = stream.read(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream stream = new Base64InputStream(null);
        boolean result = stream.markSupported();
        assertFalse(result);
    }
}