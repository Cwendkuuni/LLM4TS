import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Create a Base64InputStream with a sample input stream
        byte[] input = new byte[] {65, 66, 67, 68}; // ASCII values for 'A', 'B', 'C', 'D'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            // Read a single byte from the input stream
            int result = base64InputStream.read();
            assertEquals(65, result); // 'A'
        } catch (IOException e) {
            fail("IOException was thrown");
        }

        // Test reading multiple bytes
        byte[] buffer = new byte[4];
        try {
            int bytesRead = base64InputStream.read(buffer, 0, 4);
            assertEquals(3, bytesRead); // Read 3 bytes ('B', 'C', 'D')
            assertEquals(66, buffer[0]); // 'B'
            assertEquals(67, buffer[1]); // 'C'
            assertEquals(68, buffer[2]); // 'D'
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testReadEdgeCases() {
        // Test reading an empty input stream
        ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(emptyStream);
        try {
            int result = base64InputStream.read();
            assertEquals(-1, result); // End of stream
        } catch (IOException e) {
            fail("IOException was thrown");
        }

        // Test reading more bytes than available
        byte[] input = new byte[] {65}; // ASCII value for 'A'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        try {
            int bytesRead = base64InputStream.read(buffer, 0, 2);
            assertEquals(1, bytesRead); // Read 1 byte
            assertEquals(65, buffer[0]); // 'A'
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        // Create a Base64InputStream with a sample input stream
        byte[] input = new byte[] {65, 66, 67, 68}; // ASCII values for 'A', 'B', 'C', 'D'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Assert that mark is not supported
        assertFalse(base64InputStream.markSupported());
    }

}