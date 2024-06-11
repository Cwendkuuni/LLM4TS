import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test reading from an input stream with single byte
        InputStream in = new ByteArrayInputStream(new byte[]{65});
        Base64InputStream base64Input = new Base64InputStream(in);
        try {
            int result = base64Input.read();
            assertEquals(65, result);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test reading from an input stream with multiple bytes
        InputStream in2 = new ByteArrayInputStream(new byte[]{66, 67, 68, 69});
        Base64InputStream base64Input2 = new Base64InputStream(in2);
        try {
            int result1 = base64Input2.read();
            int result2 = base64Input2.read();
            int result3 = base64Input2.read();
            int result4 = base64Input2.read();
            int result5 = base64Input2.read();
            assertEquals(66, result1);
            assertEquals(67, result2);
            assertEquals(68, result3);
            assertEquals(69, result4);
            assertEquals(-1, result5);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testRead_withOffsetAndLength() {
        byte[] buffer = new byte[4];

        // Test reading with offset and length
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67, 68, 69});
        Base64InputStream base64Input = new Base64InputStream(in);
        try {
            int bytesRead = base64Input.read(buffer, 1, 3);
            assertEquals(3, bytesRead);
            assertEquals(0, buffer[0]);
            assertEquals(65, buffer[1]);
            assertEquals(66, buffer[2]);
            assertEquals(67, buffer[3]);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67, 68, 69});
        Base64InputStream base64Input = new Base64InputStream(in);
        boolean result = base64Input.markSupported();
        assertFalse(result);
    }
}