
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int result = base64InputStream.read();
        
        assertEquals(84, result);  // ASCII value of 'T'
    }
  
    @Test
    public void testRead_WithEmptyStream() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        int result = base64InputStream.read();
        
        assertEquals(-1, result);  // EOF reached
    }
  
    @Test
    public void testRead_WithInvalidOffsetAndLen_ThrowsIndexOutOfBoundsException() throws Exception {
        byte[] data = "Test data".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[8];
        
        try {
            base64InputStream.read(buffer, -1, 5);  // Invalid offset
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            base64InputStream.read(buffer, 0, 15);  // Invalid length
            fail("Exception not thrown");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    // More test cases for other methods
    
}

