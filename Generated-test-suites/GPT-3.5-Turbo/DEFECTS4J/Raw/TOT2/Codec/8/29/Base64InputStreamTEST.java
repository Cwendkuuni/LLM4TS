import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    // Test for read() method - Reads one byte at a time from the input stream
    @Test
    public void testRead() {
        // Create a Base64InputStream with a byte array input stream
        byte[] data = { 97, 98, 99, 100 }; // ASCII values for 'a', 'b', 'c', 'd'
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        try {
            assertEquals(97, base64InputStream.read()); // Read 'a'
            assertEquals(98, base64InputStream.read()); // Read 'b'
            assertEquals(99, base64InputStream.read()); // Read 'c'
            assertEquals(100, base64InputStream.read()); // Read 'd'
            assertEquals(-1, base64InputStream.read()); // EOF, should return -1
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for read(byte[] b, int offset, int len) method - Reads multiple bytes into the specified byte array
    @Test
    public void testReadByteArrayOffsetLen() {
        byte[] data = { 97, 98, 99, 100 }; // ASCII values for 'a', 'b', 'c', 'd'
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[4];
        try {
            int bytesRead = base64InputStream.read(buffer, 0, 4);
            assertEquals(4, bytesRead); // Ensure all 4 bytes are read
            assertArrayEquals(data, buffer); // Ensure data read matches original data
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    // Test for read(byte[] b, int offset, int len) method - Testing exceptions
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadArrayInvalidOffset() throws IOException {
        byte[] data = { 97, 98, 99, 100 }; // ASCII values for 'a', 'b', 'c', 'd'
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 5, 1); // Invalid offset, should throw IndexOutOfBoundsException
    }

    // Test for markSupported() method - Check if mark is supported
    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(base64InputStream.markSupported());
    }
}