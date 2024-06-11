import org.junit.*;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    // Test Case 1
    @Test
    public void testRead_Success() {
        byte[] input = {84, 69, 83, 84, 32, 83, 84, 82, 73, 78, 71}; // "TEST STRING"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        try {
            int result = base64InputStream.read();
            assertEquals(84, result); // 'T'
            result = base64InputStream.read();
            assertEquals(69, result); // 'E'
            result = base64InputStream.read();
            assertEquals(83, result); // 'S'
            result = base64InputStream.read();
            assertEquals(84, result); // 'T'
            result = base64InputStream.read();
            assertEquals(32, result); // ' '
            result = base64InputStream.read();
            assertEquals(83, result); // 'S'
            result = base64InputStream.read();
            assertEquals(84, result); // 'T'
            result = base64InputStream.read();
            assertEquals(82, result); // 'R'
            result = base64InputStream.read();
            assertEquals(73, result); // 'I'
            result = base64InputStream.read();
            assertEquals(78, result); // 'N'
            result = base64InputStream.read();
            assertEquals(71, result); // 'G'
            result = base64InputStream.read();
            assertEquals(-1, result); // EOF
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test Case 2
    @Test
    public void testRead_MultipleBytes_Success() {
        byte[] input = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // 10 bytes of all zeros
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        try {
            byte[] output = new byte[10];
            int bytesRead = base64InputStream.read(output, 0, 10);
            assertEquals(10, bytesRead); // we should read all 10 bytes
            for (int i = 0; i < 10; i++) {
                assertEquals(0, output[i]); // all bytes should be zero
            }
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test Case 3
    @Test
    public void testRead_NullInput_Exception() {
        ByteArrayInputStream bais = new ByteArrayInputStream(null);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        try {
            base64InputStream.read();
            fail("Expected an exception to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Byte array parameter is null", e.getMessage());
        } catch (Exception e) {
            fail("Expected a NullPointerException to be thrown");
        }
    }

    // Test Case 4
    @Test
    public void testRead_InvalidOffset_Exception() {
        byte[] input = {84, 69, 83, 84, 32, 83, 84, 82, 73, 78, 71}; // "TEST STRING"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        try {
            byte[] output = new byte[10];
            base64InputStream.read(output, -1, 10);
            fail("Expected an exception to be thrown");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Offset, len or buffer size are invalid", e.getMessage());
        } catch (Exception e) {
            fail("Expected an IndexOutOfBoundsException to be thrown");
        }
    }

    // Test Case 5
    @Test
    public void testRead_BufferTooSmall_Success() {
        byte[] input = {84, 69, 83, 84, 32, 83, 84, 82, 73, 78, 71}; // "TEST STRING"
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        try {
            byte[] output = new byte[5]; // buffer size is smaller than the input
            int bytesRead = base64InputStream.read(output, 0, 5);
            assertEquals(5, bytesRead); // only 5 bytes should be read
            assertEquals(84, output[0]); // 'T'
            assertEquals(69, output[1]); // 'E'
            assertEquals(83, output[2]); // 'S'
            assertEquals(84, output[3]); // 'T'
            assertEquals(32, output[4]); // ' '
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test Case 6
    @Test
    public void testRead_EOF_Success() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]); // empty input stream
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        try {
            int result = base64InputStream.read();
            assertEquals(-1, result); // should return EOF immediately
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    // Test Case 7
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]); // empty input stream
        Base64InputStream base64InputStream = new Base64InputStream(bais);
        assertFalse(base64InputStream.markSupported()); // mark should not be supported
    }
}