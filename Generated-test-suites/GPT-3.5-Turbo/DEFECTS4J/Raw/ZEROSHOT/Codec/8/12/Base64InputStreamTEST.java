import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Test
    public void testRead() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        assertEquals(72, result);
        result = base64InputStream.read();
        assertEquals(101, result);
        result = base64InputStream.read();
        assertEquals(108, result);
        result = base64InputStream.read();
        assertEquals(108, result);
        result = base64InputStream.read();
        assertEquals(111, result);
        result = base64InputStream.read();
        assertEquals(32, result);
        result = base64InputStream.read();
        assertEquals(87, result);
        result = base64InputStream.read();
        assertEquals(111, result);
        result = base64InputStream.read();
        assertEquals(114, result);
        result = base64InputStream.read();
        assertEquals(108, result);
        result = base64InputStream.read();
        assertEquals(100, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        int result = base64InputStream.read(buffer, 0, 4);
        assertArrayEquals(new byte[] { 72, 101, 108, 108 }, buffer);
        assertEquals(4, result);
    }

    @Test
    public void testReadWithNullBuffer() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = null;
        try {
            base64InputStream.read(buffer, 0, 4);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithNegativeOffset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        try {
            base64InputStream.read(buffer, -1, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithNegativeLength() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithInvalidOffset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        try {
            base64InputStream.read(buffer, 10, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithInvalidLength() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        try {
            base64InputStream.read(buffer, 0, 10);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        base64InputStream = new Base64InputStream(inputStream);
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}