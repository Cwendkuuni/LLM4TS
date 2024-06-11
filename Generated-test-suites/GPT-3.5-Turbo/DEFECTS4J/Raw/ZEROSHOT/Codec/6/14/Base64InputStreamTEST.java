import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test when there is data to read
        byte[] data = {32, 64, 128};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            int result = base64InputStream.read();

            assertEquals(data[0], result, "Read returned incorrect byte.");

            result = base64InputStream.read();

            assertEquals(data[1], result, "Read returned incorrect byte.");

            result = base64InputStream.read();

            assertEquals(data[2], result, "Read returned incorrect byte.");

            result = base64InputStream.read();

            assertEquals(-1, result, "Read did not return -1 at end of stream.");

            base64InputStream.close();

        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        // Test when there is data to read
        byte[] data = {32, 64, 128, 255};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[5];

        try {
            int result = base64InputStream.read(output, 1, 3);

            assertEquals(3, result, "Read returned incorrect number of bytes.");

            assertEquals(0, output[0], "First element of output array should not be modified.");

            assertEquals(data[0], output[1], "Read returned incorrect byte.");

            assertEquals(data[1], output[2], "Read returned incorrect byte.");

            assertEquals(data[2], output[3], "Read returned incorrect byte.");

            base64InputStream.close();

        } catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
    }
}