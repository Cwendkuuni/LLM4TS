import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws Exception {
        int result = base64InputStream.read();
        
        assertEquals(72, result);
    }

    @Test
    public void testReadByteOffsetLen() throws Exception {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 5);
        
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[] { 72, 101, 108, 108, 111 }, buffer);
    }

    @Test
    public void testReadByteNullBuffer() throws Exception {
        try {
            base64InputStream.read(null, 0, 5);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testReadByteOffsetNegative() throws Exception {
        try {
            byte[] buffer = new byte[5];
            base64InputStream.read(buffer, -1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadByteLenNegative() throws Exception {
        try {
            byte[] buffer = new byte[5];
            base64InputStream.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadByteOffsetGreaterThanBufferSize() throws Exception {
        try {
            byte[] buffer = new byte[5];
            base64InputStream.read(buffer, 10, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadLenZero() throws Exception {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 0);

        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[5], buffer);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

}