import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        int result = base64InputStream.read();
        assertEquals(72, result);
    }

    @Test
    public void testReadByteArray() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        int result = base64InputStream.read(buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test
    public void testReadByteArrayWithOffset() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        int result = base64InputStream.read(buffer, 5, 5);
        assertEquals(5, result);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 72, 101, 108, 108, 111}, buffer);
    }

    @Test
    public void testReadByteArrayLengthZero() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        int result = base64InputStream.read(buffer, 5, 0);
        assertEquals(0, result);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0, 0, 72, 101, 108, 108}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNullBuffer() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        base64InputStream.read(null, 0, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeOffset() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        base64InputStream.read(buffer, -1, 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws Exception {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        byte[] buffer = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        base64InputStream.read(buffer, 0, 6);
    }
    
    @Test
    public void testReadWithEmptyInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadByteArrayWithEmptyInputStream() throws Exception {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[5];
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        int result = base64InputStream.read(buffer, 0, 5);
        assertEquals(-1, result);
    }
    
    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in, false);
        assertFalse(base64InputStream.markSupported());
    }
}