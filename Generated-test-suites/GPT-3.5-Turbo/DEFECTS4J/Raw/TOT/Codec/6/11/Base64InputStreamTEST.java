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
    public void testRead() {
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullBuffer() throws Exception {
        byte[] buffer = null;
        base64InputStream.read(buffer, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 20, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLen() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 20);
    }

    @Test
    public void testReadEmptyBuffer() {
        byte[] buffer = new byte[0];
        int result = base64InputStream.read(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullBufferWithOffset() throws Exception {
        byte[] buffer = null;
        base64InputStream.read(buffer, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffsetWithLength() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffsetWithLength() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 20, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 20);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullBufferWithInvalidOffsetLength() throws Exception {
        byte[] buffer = null;
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffsetWithInvalidLength() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffsetWithInvalidLength() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 20, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLengthWithInvalidOffset() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 20);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffsetWithInvalidLengthAndBufferLength() throws Exception {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 20, 20);
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}