import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertEquals(65, base64InputStream.read());
        assertEquals(66, base64InputStream.read());
        assertEquals(67, base64InputStream.read());
        assertEquals(68, base64InputStream.read());
        assertEquals(69, base64InputStream.read());
        assertEquals(70, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] input = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[6];
        int bytesRead = base64InputStream.read(output);

        assertEquals(6, bytesRead);
        assertArrayEquals(input, output);

        bytesRead = base64InputStream.read(output);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadArrayOffsetLen() throws IOException {
        byte[] input = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[8];
        int bytesRead = base64InputStream.read(output, 1, 6);

        assertEquals(6, bytesRead);
        assertEquals(0, output[0]);
        assertArrayEquals(input, 1, output);

        bytesRead = base64InputStream.read(output, 7, 1);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadArrayOffsetLenInvalidIndex() throws IOException {
        byte[] input = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[8];

        try {
            base64InputStream.read(output, -1, 6);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }

        try {
            base64InputStream.read(output, 1, -1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }

        try {
            base64InputStream.read(output, 7, 2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadArrayNullInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            base64InputStream.read(null, 0, 5);
            fail();
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testReadArrayNoInput() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] output = new byte[5];
        int bytesRead = base64InputStream.read(output, 0, 5);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }
}