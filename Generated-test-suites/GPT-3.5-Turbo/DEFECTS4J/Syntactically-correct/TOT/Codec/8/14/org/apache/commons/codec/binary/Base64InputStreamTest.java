import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    // Test cases for the read() method
    
    @Test
    public void testRead_whenInputStreamHasData_shouldReturnCorrectByteValue() {
        // Arrange 
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        int result1 = base64InputStream.read();
        int result2 = base64InputStream.read();
        int result3 = base64InputStream.read();
        
        // Assert
        assertEquals(65, result1);
        assertEquals(66, result2);
        assertEquals(67, result3);
    }
    
    @Test
    public void testRead_whenInputStreamHasNoData_shouldReturnNegativeOne() {
        // Arrange 
        InputStream in = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        int result = base64InputStream.read();
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testRead_whenInputStreamHasPartialData_shouldReturnCorrectByteValues() {
        // Arrange 
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[2];
        
        // Act
        int result1 = base64InputStream.read(buffer, 0, 2);
        
        // Assert
        assertEquals(2, result1);
        assertEquals(65, buffer[0]);
        assertEquals(66, buffer[1]);
    }
    
    @Test(expected = NullPointerException.class)
    public void testRead_whenNullBufferPassed_shouldThrowNullPointerException() throws IOException {
        // Arrange 
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        
        // Act
        base64InputStream.read(null, 0, 2);
        
        // Assert
        // NullPointerException is expected
    }
    
    // Rest of the test cases for the other methods
    
    // ...

}