import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[inputBytes.length];
        int bytesRead = base64InputStream.read(outputBytes);

        assertEquals(inputBytes, outputBytes);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int offset = 5;
        int length = 10;

        byte[] outputBytes = new byte[length];
        int bytesRead = base64InputStream.read(outputBytes, offset, length);

        byte[] expectedOutputBytes = new byte[length];
        System.arraycopy(inputBytes, offset, expectedOutputBytes, 0, length);

        assertEquals(expectedOutputBytes, outputBytes);
        assertEquals(length, bytesRead);
    }

    @Test
    public void testReadWithZeroLength() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int length = 0;

        byte[] outputBytes = new byte[length];
        int bytesRead = base64InputStream.read(outputBytes);

        assertEquals(length, bytesRead);
    }

    @Test
    public void testReadWithNullBArray() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            base64InputStream.read(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Success
        }
    }

    @Test
    public void testReadWithInvalidOffset() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int offset = -1;
        int length = 10;

        byte[] outputBytes = new byte[length];
        try {
            base64InputStream.read(outputBytes, offset, length);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Success
        }
    }

    @Test
    public void testReadWithInvalidLength() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int offset = 5;
        int length = -1;

        byte[] outputBytes = new byte[length];
        try {
            base64InputStream.read(outputBytes, offset, length);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Success
        }
    }

    @Test
    public void testReadWithInvalidBufferSize() throws Exception {
        String inputString = "This is a test string";
        byte[] inputBytes = inputString.getBytes();
        byte[] encodedBytes = Base64.encodeBase64(inputBytes);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[5]); // Read only 5 bytes

        byte[] outputBytes = new byte[inputBytes.length];
        int bytesRead = base64InputStream.read(outputBytes);

        assertEquals(inputBytes, outputBytes);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testMarkSupported() throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }
}