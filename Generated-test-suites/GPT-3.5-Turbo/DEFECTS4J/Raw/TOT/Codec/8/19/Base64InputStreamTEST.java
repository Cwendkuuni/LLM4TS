import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {
    
    private Base64InputStream base64InputStream;
    
    @Before
    public void setup() {
        InputStream inputStream = new FileInputStream("test.txt");
        base64InputStream = new Base64InputStream(inputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(116, result);
    }
    
    @Test
    public void testReadWithLargeBuffer() throws IOException {
        byte[] buffer = new byte[1000];
        int result = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(1000, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 5, 10);
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        byte[] buffer = new byte[10];
        int result =  base64InputStream.read(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
    
    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[]{});
        Base64InputStream emptyBase64InputStream = new Base64InputStream(emptyInputStream);
        int result = emptyBase64InputStream.read();
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadWithBase64Encoding() throws IOException {
        byte[] encodedBytes = Base64.encodeBase64("test".getBytes());
        InputStream encodedInputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream encodedBase64InputStream = new Base64InputStream(encodedInputStream, true);
        int result = encodedBase64InputStream.read();
        assertEquals(116, result);
    }
    
    @Test
    public void testReadWithInvalidBase64Encoding() throws IOException {
        byte[] encodedBytes = Base64.encodeBase64("test$".getBytes());
        InputStream encodedInputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream encodedBase64InputStream = new Base64InputStream(encodedInputStream, true);
        int result = encodedBase64InputStream.read();
        assertEquals(45, result);
    }
    
}