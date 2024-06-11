import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

public class Base64InputStreamTest {
    @Test
    public void testRead() {
        byte[] input = Base64.encodeBase64("Hello, World!".getBytes());
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
        byte[] output = new byte[18];
        try {
            int len = stream.read(output);
            assertEquals(13, len);
            assertArrayEquals("Hello, World!".getBytes(), Arrays.copyOfRange(output, 0, len));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadWithOffsetAndLength() {
        byte[] input = Base64.encodeBase64("Hello, World!".getBytes());
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
        byte[] output = new byte[18];
        try {
            int len = stream.read(output, 6, 12);
            assertEquals(13, len);
            assertArrayEquals(new byte[6], Arrays.copyOfRange(output, 0, 6));
            assertArrayEquals("Hello, World!".getBytes(), Arrays.copyOfRange(output, 6, 18));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadWithEmptyInput() {
        byte[] input = new byte[0];
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(input));
        byte[] output = new byte[18];
        try {
            int len = stream.read(output);
            assertEquals(-1, len);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadWithNullInput() {
        Base64InputStream stream = new Base64InputStream(null);
        byte[] output = new byte[18];
        try {
            int len = stream.read(output);
            assertEquals(-1, len);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(stream.markSupported());
    }
}