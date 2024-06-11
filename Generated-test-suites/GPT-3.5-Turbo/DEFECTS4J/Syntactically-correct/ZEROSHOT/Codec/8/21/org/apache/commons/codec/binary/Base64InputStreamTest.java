import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Create Base64InputStream object with test input stream
        InputStream in = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        try {
            // Read one byte from the input stream
            int result = base64InputStream.read();
            // Verify that the byte is read correctly
            assertEquals(1, result);

            // Read bytes into a byte array
            byte[] buffer = new byte[2];
            int bytesRead = base64InputStream.read(buffer);
            // Verify that the bytes are read correctly
            assertEquals(2, bytesRead);
            assertArrayEquals(new byte[]{2, 3}, buffer);

            // Read bytes into a byte array at a specified offset and length
            byte[] buffer2 = new byte[3];
            int bytesRead2 = base64InputStream.read(buffer2, 1, 2);
            // Verify that the bytes are read correctly
            assertEquals(2, bytesRead2);
            assertArrayEquals(new byte[]{0, 2, 3}, buffer2);
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }

        // Close the Base64InputStream
        try {
            base64InputStream.close();
        } catch (IOException e) {
            fail("IOException occurred while closing Base64InputStream: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        // Create Base64InputStream object with test input stream
        InputStream in = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(in);

        // Verify that mark is not supported
        assertFalse(base64InputStream.markSupported());

        // Close the Base64InputStream
        try {
            base64InputStream.close();
        } catch (IOException e) {
            fail("IOException occurred while closing Base64InputStream: " + e.getMessage());
        }
    }
}