
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        int byteRead = base64InputStream.read();
        
        assertEquals(72, byteRead);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[10];
        
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        
        assertEquals(10, bytesRead);
        assertEquals(72, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(108, buffer[2]);
        assertEquals(108, buffer[3]);
        assertEquals(111, buffer[4]);
        assertEquals(44, buffer[5]);
        assertEquals(32, buffer[6]);
        assertEquals(87, buffer[7]);
        assertEquals(111, buffer[8]);
        assertEquals(114, buffer[9]);
    }
    
    @Test
    public void testReadWithZeroLengthBuffer() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[0];
        
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        
        assertEquals(0, bytesRead);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        base64InputStream.read(null, 0, 10);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[10];
        
        base64InputStream.read(buffer, -1, buffer.length);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[10];
        
        base64InputStream.read(buffer, 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[5];
        
        base64InputStream.read(buffer, 0, buffer.length);
    }
    
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.encodeBase64("Hello, World!".getBytes()));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        
        boolean markSupported = base64InputStream.markSupported();
        
        assertFalse(markSupported);
    }
}
