
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        int result = base64InputStream.read();
        assertEquals(1, result);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] input = new byte[]{1, 2, 3, 4};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[4];
        int result = base64InputStream.read(buffer, 0, 4);
        assertArrayEquals(new byte[]{1, 2, 3, 4}, buffer);
        assertEquals(4, result);
    }

    @Test
    public void testReadWithInvalidOffset() throws IOException {
        byte[] input = new byte[]{1, 2};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[2];
        try {
            base64InputStream.read(buffer, -1, 2);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithInvalidLength() throws IOException {
        byte[] input = new byte[]{1, 2};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[2];
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadWithInvalidLengthAndOffset() throws IOException {
        byte[] input = new byte[]{1, 2};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[2];
        try {
            base64InputStream.read(buffer, 1, -1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

}
