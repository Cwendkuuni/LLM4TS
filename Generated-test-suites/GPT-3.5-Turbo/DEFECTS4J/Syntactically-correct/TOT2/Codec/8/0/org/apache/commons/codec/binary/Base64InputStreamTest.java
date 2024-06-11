import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testConstructorWithoutEncoding() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        
        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Then
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testConstructorWithEncoding() {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        
        // When
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        
        // Then
        assertNotNull(base64InputStream);
    }
    
    @Test
    public void testReadMethod() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // When
        int data = base64InputStream.read();
        
        // Then
        assertEquals('T', data);
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        
        // When
        int bytesRead = base64InputStream.read(buffer, 0, 4);
        
        // Then
        assertEquals(4, bytesRead);
        assertArrayEquals("Test".getBytes(), buffer);
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = null;
        
        // When
        base64InputStream.read(buffer, 0, 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        
        // When
        base64InputStream.read(buffer, 0, 5);
    }
    
}