import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testReadMethod() throws IOException {
        byte[] testData = { 65, 66, 67 }; // ASCII values for A, B, C
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertEquals(65, base64InputStream.read()); // Reading first byte
        assertEquals(66, base64InputStream.read()); // Reading second byte
        assertEquals(67, base64InputStream.read()); // Reading third byte
        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test
    public void testReadMethodWithEmptyInput() throws IOException {
        byte[] testData = {}; // Empty input data
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test(expected = NullPointerException.class)
    public void testReadMethodWithNullByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadMethodWithInvalidOffset() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(new byte[]{}, -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadMethodWithInvalidLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        Base64InputStream base64InputStream = new Base64InputStream(input);
        base64InputStream.read(new byte[]{}, 0, -1);
    }

    @Test
    public void testReadMethodWithLargeInput() throws IOException {
        byte[] testData = new byte[4096]; // Large input data
        for (int i = 0; i < testData.length; i++) {
            testData[i] = (byte) i;
        }
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] output = new byte[2048];
        assertEquals(2048, base64InputStream.read(output, 0, 2048)); // Reading first half of the data
        assertArrayEquals(testData, output); // Input data matches output data
    }

}