import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        byte[] testData = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            int result = base64InputStream.read();

            assertEquals(0, result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] testData = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[5];
        try {
            int result = base64InputStream.read(output, 0, 5);

            assertEquals(5, result);
            assertArrayEquals(testData, output);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        byte[] testData = {0, 1, 2, 3, 4};
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }
}