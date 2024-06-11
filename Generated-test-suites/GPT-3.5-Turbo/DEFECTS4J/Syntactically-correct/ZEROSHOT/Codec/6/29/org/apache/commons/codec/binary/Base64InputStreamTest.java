import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] input = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        byte[] expectedOutput = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 };

        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(input));
        byte[] buffer = new byte[input.length];
        int bytesRead = inputStream.read(buffer);
        inputStream.close();

        assertArrayEquals(expectedOutput, buffer);
        assertEquals(input.length, bytesRead);
    }

    @Test
    public void testReadEmptyArray() throws Exception {
        byte[] input = {};
        byte[] expectedOutput = {};

        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(input));
        byte[] buffer = new byte[input.length];
        int bytesRead = inputStream.read(buffer);
        inputStream.close();

        assertArrayEquals(expectedOutput, buffer);
        assertEquals(input.length, bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        byte[] input = { 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100 }; // "Hello World" in ASCII
        byte[] expectedOutput = { 72, 101, 108, 108, 111 };

        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(input));
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 0, 5);
        inputStream.close();

        assertArrayEquals(expectedOutput, buffer);
        assertEquals(5, bytesRead);
    }

    @Test(expected=NullPointerException.class)
    public void testReadNullArray() throws Exception {
        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(new byte[] {}));
        inputStream.read(null);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws Exception {
        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(new byte[] {}));
        inputStream.read(new byte[5], -1, 5);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws Exception {
        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(new byte[] {}));
        inputStream.read(new byte[5], 0, -1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadInvalidBufferSize() throws Exception {
        Base64InputStream inputStream = new Base64InputStream(new java.io.ByteArrayInputStream(new byte[] {}));
        inputStream.read(new byte[5], 0, 10);
    }
}