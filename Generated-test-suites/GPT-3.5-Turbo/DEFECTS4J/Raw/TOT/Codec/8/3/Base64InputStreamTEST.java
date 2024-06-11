import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {
    
    private Base64InputStream base64InputStream;
    
    @Before
    public void setUp() {
        // Create a Base64InputStream with a ByteArrayInputStream
        // containing some Base64-encoded data
        byte[] data = "SGVsbG8gd29ybGQh".getBytes(); // "Hello world!"
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(bais, false);
    }
    
    @Test
    public void testRead() throws IOException {
        // Test reading a single byte
        int result = base64InputStream.read();
        assertEquals(72, result); // 'H' in ASCII
        
        // Test reading multiple bytes
        byte[] buffer = new byte[5];
        int numBytes = base64InputStream.read(buffer, 0, 5);
        
        assertEquals(4, numBytes);
        assertArrayEquals("ello ".getBytes(), buffer);
        
        // Test reading when EOF has been reached
        result = base64InputStream.read();
        assertEquals(-1, result);
    }
    
    @Test
    public void testReadNullByteArray() throws IOException {
        // Test reading into a null byte array
        byte[] buffer = null;
        try {
            base64InputStream.read(buffer, 0, 5);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testReadNegativeOffset() throws IOException {
        // Test reading with a negative offset
        byte[] buffer = new byte[5];
        try {
            base64InputStream.read(buffer, -1, 5);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testReadOverlappingOffsets() throws IOException {
        // Test reading with overlapping offsets
        byte[] buffer = new byte[5];
        try {
            base64InputStream.read(buffer, 2, 4);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testReadOutOfBoundsOffset() throws IOException {
        // Test reading with an offset greater than the length of the buffer
        byte[] buffer = new byte[5];
        try {
            base64InputStream.read(buffer, 6, 3);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testReadZeroLength() throws IOException {
        // Test reading with a zero length
        byte[] buffer = new byte[5];
        int numBytes = base64InputStream.read(buffer, 0, 0);
        
        assertEquals(0, numBytes);
        assertArrayEquals(new byte[5], buffer);
    }
    
    @Test
    public void testBase64Encoding() throws IOException {
        // Create a Base64InputStream with a ByteArrayInputStream
        // containing some data to be Base64-encoded
        byte[] data = "Hello world!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(bais, true);
        
        byte[] buffer = new byte[20];
        int numBytes = base64InputStream.read(buffer, 0, 20);
        
        assertEquals(16, numBytes);
        assertEquals("SGVsbG8gd29ybGQhAAAA".getBytes(), buffer);
    }
    
    @Test
    public void testMarkSupported() {
        // Test that markSupported always returns false
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}