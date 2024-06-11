import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    // Testing the read method
    @Test
    public void testRead() throws Exception {
        // Create a base64 encoded string
        String encodedString = "SGVsbG8gd29ybGQh"; // "Hello world!"
        byte[] encodedBytes = encodedString.getBytes("UTF-8");
        
        // Create an input stream with the encoded bytes
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(encodedBytes), false);
        
        // Create a byte array to hold the decoded bytes
        byte[] decodedBytes = new byte[encodedBytes.length];
        
        // Read the decoded bytes from the input stream
        int bytesRead = inputStream.read(decodedBytes);
        
        // Assert that the bytes read is equal to the encoded bytes length
        assertEquals(encodedBytes.length, bytesRead);
        
        // Assert that the decoded bytes are equal to the original string bytes
        assertArrayEquals(encodedString.getBytes("UTF-8"), decodedBytes);
        
        // Close the input stream
        inputStream.close();
    }

    // Testing the read method with offset and length
    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        // Create a base64 encoded string
        String encodedString = "SGVsbG8gd29ybGQh"; // "Hello world!"
        byte[] encodedBytes = encodedString.getBytes("UTF-8");
        
        // Create an input stream with the encoded bytes
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(encodedBytes), false);
        
        // Create a byte array to hold the decoded bytes
        byte[] decodedBytes = new byte[encodedBytes.length];
        
        // Set the offset and length
        int offset = 3;
        int length = 5;
        
        // Read the decoded bytes from the input stream with offset and length
        int bytesRead = inputStream.read(decodedBytes, offset, length);
        
        // Assert that the bytes read is equal to the length
        assertEquals(length, bytesRead);
        
        // Assert that the decoded bytes at the offset are equal to the substring of the original string bytes
        assertArrayEquals(encodedString.substring(0, length).getBytes("UTF-8"), 
                          java.util.Arrays.copyOfRange(decodedBytes, offset, offset + length));
        
        // Close the input stream
        inputStream.close();
    }

    // Testing the read method with larger buffer size
    @Test
    public void testReadWithLargerBufferSize() throws Exception {
        // Create a base64 encoded string
        String encodedString = "SGVsbG8gd29ybGQh"; // "Hello world!"
        byte[] encodedBytes = encodedString.getBytes("UTF-8");
        
        // Create an input stream with the encoded bytes and larger buffer size
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(encodedBytes), false, 8192, null);
        
        // Create a byte array to hold the decoded bytes
        byte[] decodedBytes = new byte[encodedBytes.length];
        
        // Read the decoded bytes from the input stream
        int bytesRead = inputStream.read(decodedBytes);
        
        // Assert that the bytes read is equal to the encoded bytes length
        assertEquals(encodedBytes.length, bytesRead);
        
        // Assert that the decoded bytes are equal to the original string bytes
        assertArrayEquals(encodedString.getBytes("UTF-8"), decodedBytes);
        
        // Close the input stream
        inputStream.close();
    }

    // Testing the markSupported method
    @Test
    public void testMarkSupported() {
        // Create an input stream
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        
        // Assert that mark is not supported
        assertFalse(inputStream.markSupported());
        
        // Close the input stream
        inputStream.close();
    }
}