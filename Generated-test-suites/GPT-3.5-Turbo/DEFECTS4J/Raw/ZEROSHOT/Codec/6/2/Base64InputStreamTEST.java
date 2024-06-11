import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test case 1: Test reading a single byte
        byte[] testBytes1 = {0x61};
        Base64InputStream inputStream1 = new Base64InputStream(new ByteArrayInputStream(testBytes1));
        try {
            int readByte1 = inputStream1.read();
            assertEquals(97, readByte1);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        // Test case 2: Test reading multiple bytes
        byte[] testBytes2 = {0x61, 0x62};
        Base64InputStream inputStream2 = new Base64InputStream(new ByteArrayInputStream(testBytes2));
        byte[] readBytes2 = new byte[2];
        try {
            int bytesRead2 = inputStream2.read(readBytes2, 0, 2);
            assertEquals(2, bytesRead2);
            assertArrayEquals(testBytes2, readBytes2);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }

        // Test case 3: Test reading more bytes than available
        byte[] testBytes3 = {0x61};
        Base64InputStream inputStream3 = new Base64InputStream(new ByteArrayInputStream(testBytes3));
        byte[] readBytes3 = new byte[2];
        try {
            int bytesRead3 = inputStream3.read(readBytes3, 0, 2);
            assertEquals(1, bytesRead3);
            assertEquals(97, readBytes3[0]);
            assertEquals(0, readBytes3[1]);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}));
        assertFalse(inputStream.markSupported());
    }
}