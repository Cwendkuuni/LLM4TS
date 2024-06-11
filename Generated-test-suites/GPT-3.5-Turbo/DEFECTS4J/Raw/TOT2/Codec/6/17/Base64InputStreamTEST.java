import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    // Test read method for typical case
    @Test
    public void testReadTypical() {
        // Create base64 encoded inputStream with "Hello World" encoded
        byte[] encodedBytes = "SGVsbG8gV29ybGQ=".getBytes();
        InputStream in = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        try {
            assertEquals('H', base64InputStream.read());
            assertEquals('e', base64InputStream.read());
            assertEquals('l', base64InputStream.read());
            assertEquals('l', base64InputStream.read());
            assertEquals('o', base64InputStream.read());
            assertEquals(' ', base64InputStream.read());
            assertEquals('W', base64InputStream.read());
            assertEquals('o', base64InputStream.read());
            assertEquals('r', base64InputStream.read());
            assertEquals('l', base64InputStream.read());
            assertEquals('d', base64InputStream.read());
            assertEquals(-1, base64InputStream.read()); // EOF
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Test read method for edge case of empty inputStream
    @Test
    public void testReadEmptyInput() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        try {
            assertEquals(-1, base64InputStream.read()); // EOF
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Test read method for edge case of very short input stream
    @Test
    public void testReadShortInput() {
        byte[] encodedBytes = "AAAA".getBytes(); // A is 0 in base64
        InputStream in = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        try {
            assertEquals(0, base64InputStream.read());
            assertEquals(0, base64InputStream.read());
            assertEquals(0, base64InputStream.read());
            assertEquals(0, base64InputStream.read());
            assertEquals(-1, base64InputStream.read()); // EOF
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Test read method for error scenario with incorrect bytes
    @Test
    public void testReadInvalidInput() {
        byte[] invalidData = "InvalidData".getBytes();
        InputStream in = new ByteArrayInputStream(invalidData);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        try {
            assertEquals(-1, base64InputStream.read()); // EOF
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Add more test cases for other methods if needed

}