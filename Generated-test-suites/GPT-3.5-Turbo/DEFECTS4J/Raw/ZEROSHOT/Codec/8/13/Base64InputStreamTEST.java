import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    // Test read() method

    @Test
    public void testRead_WhenDataIsAvailable_ShouldReturnByteValue() {
        // Arrange
        byte data = 65;
        byte[] buffer = new byte[1];
        InputStream inputStream = new ByteArrayInputStream(new byte[] { data });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        int result = base64InputStream.read(buffer, 0, 1);
        
        // Assert
        assertEquals(65, buffer[0]);
    }
    
    @Test
    public void testRead_WhenDataIsNotAvailable_ShouldReturnMinusOne() {
        // Arrange
        byte[] buffer = new byte[1];
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        int result = base64InputStream.read(buffer, 0, 1);
        
        // Assert
        assertEquals(-1, result);
    }

    // Test markSupported() method
    
    @Test
    public void testMarkSupported_ShouldReturnFalse() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        // Act
        boolean result = base64InputStream.markSupported();
        
        // Assert
        assertFalse(result);
    }
}