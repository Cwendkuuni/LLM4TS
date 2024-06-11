import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    
    @Test
    public void testConstructorWithInputStream() {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Test if Base64InputStream is created successfully
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testConstructorWithEncodeFlag() {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        
        // Create Base64InputStream using constructor with InputStream and encode flag
        Base64InputStream base64InputStream = new Base64InputStream(in, true);
        
        // Test if Base64InputStream is created successfully
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testConstructorWithEncodeFlagAndLineLength() {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        
        // Create Base64InputStream using constructor with InputStream, encode flag and line length
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, "\r\n".getBytes());
        
        // Test if Base64InputStream is created successfully
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testReadOneByte() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Read one byte from Base64InputStream
        int b = base64InputStream.read();
        
        // Test if the byte is read correctly
        assertEquals('H', (char)b);
    }
    
    @Test
    public void testReadBytes() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[4];
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Read bytes from Base64InputStream
        int len = base64InputStream.read(buffer, 0, buffer.length);
        
        // Test if the bytes are read correctly
        assertEquals(4, len);
        assertArrayEquals("Hell".getBytes(), buffer);
    }
    
    @Test
    public void testReadBytesWithOffset() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[8];
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Read bytes from Base64InputStream with offset
        int len = base64InputStream.read(buffer, 2, 4);
        
        // Test if the bytes are read correctly with offset
        assertEquals(4, len);
        assertArrayEquals(new byte[]{0, 0, 'H', 'e', 'l', 'l', 'o', 0}, buffer);
    }
    
    @Test
    public void testReadBytesWithInvalidOffset() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[8];
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Test if IndexOutOfBoundsException is thrown when offset is invalid
        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, 10, 4);
        });
    }
    
    @Test
    public void testReadBytesWithInvalidLen() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[8];
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Test if IndexOutOfBoundsException is thrown when len is invalid
        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, 0, 10);
        });
    }
    
    @Test
    public void testReadBytesWithZeroLen() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[8];
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Read bytes from Base64InputStream with zero len
        int len = base64InputStream.read(buffer, 0, 0);
        
        // Test if zero bytes are read
        assertEquals(0, len);
    }
    
    @Test
    public void testReadBytesWithNoData() throws IOException {
        // Create input stream
        InputStream in = new ByteArrayInputStream("".getBytes());
        byte[] buffer = new byte[4];
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Read bytes from Base64InputStream
        int len = base64InputStream.read(buffer, 0, buffer.length);
        
        // Test if -1 is returned when there is no data to read
        assertEquals(-1, len);
    }
    
    @Test
    public void testMarkSupported() {
        // Create input stream
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        
        // Create Base64InputStream using constructor with InputStream
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Test if markSupported returns false
        assertFalse(base64InputStream.markSupported());
    }
    
}