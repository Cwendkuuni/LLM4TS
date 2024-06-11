import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private static final byte[] testData = {0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99, 0xAA};

    // Test read() method with valid input and make sure it returns the correct byte
    @Test
    public void testReadSuccess() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        int result = base64InputStream.read();
        
        assertEquals(0x11, result);
    }

    // Test read() method when reaching end of stream and make sure it returns -1
    @Test
    public void testReadEndOfStream() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Read all bytes from the stream
        for (int i = 0; i < testData.length; i++) {
            base64InputStream.read();
        }
        
        int result = base64InputStream.read();
        
        assertEquals(-1, result);
    }

    // Test read(byte[] b, int offset, int len) method with valid input and make sure it reads the correct bytes
    @Test
    public void testReadArraySuccess() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] result = new byte[4];
        base64InputStream.read(result, 0, 4);
        
        assertArrayEquals(new byte[]{0x11, 0x22, 0x33, 0x44}, result);
    }

    // Test read(byte[] b, int offset, int len) method with invalid offset and make sure it throws IndexOutOfBoundsException
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidOffset() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] result = new byte[4];
        base64InputStream.read(result, -1, 4);
    }

    // Test read(byte[] b, int offset, int len) method with invalid length and make sure it throws IndexOutOfBoundsException
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidLength() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] result = new byte[4];
        base64InputStream.read(result, 0, -1);
    }

    // Test read(byte[] b, int offset, int len) method with invalid buffer size and make sure it throws IndexOutOfBoundsException
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidBufferSize() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] result = new byte[5];
        base64InputStream.read(result, 0, 5);
    }

    // Test read(byte[] b, int offset, int len) method when reaching end of stream and make sure it returns -1
    @Test
    public void testReadArrayEndOfStream() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        byte[] result = new byte[10];
        int bytesRead = base64InputStream.read(result, 0, 10);
        
        assertEquals(-1, bytesRead);
    }

    // Test markSupported() method and make sure it returns false
    @Test
    public void testMarkSupported() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        boolean result = base64InputStream.markSupported();
        
        assertFalse(result);
    }
}