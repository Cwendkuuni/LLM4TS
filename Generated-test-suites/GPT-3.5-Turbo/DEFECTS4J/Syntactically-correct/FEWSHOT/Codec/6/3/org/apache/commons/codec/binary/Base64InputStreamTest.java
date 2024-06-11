import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Initialize test data
        byte[] testData = { 65, 66, 67, 68, 69 };  // ABCDE
        Base64InputStream inputStream = new Base64InputStream(testData, false);
        
        try {
            // Read one byte from the input stream
            assertEquals(65, inputStream.read());
            
            // Read multiple bytes from the input stream
            byte[] buffer = new byte[3];
            int bytesRead = inputStream.read(buffer, 0, 3);
            assertEquals(3, bytesRead);
            assertArrayEquals(new byte[] { 66, 67, 68 }, buffer);
            
            // Read with offset and length
            bytesRead = inputStream.read(buffer, 1, 2);
            assertEquals(2, bytesRead);
            assertArrayEquals(new byte[] { 0, 67, 68 }, buffer);
            
            // Read when reaching EOF
            bytesRead = inputStream.read(buffer, 1, 2);
            assertEquals(-1, bytesRead);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(null, false);
        assertFalse(inputStream.markSupported());
    }
}