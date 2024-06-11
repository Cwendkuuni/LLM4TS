import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private static final byte[] TEST_DATA = "Test data for Base64InputStream testing".getBytes();

    @Test
    public void testDefaultConstructor() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA));
        byte[] decoded = new byte[TEST_DATA.length];
        int read = is.read(decoded, 0, TEST_DATA.length);
        Assert.assertEquals(TEST_DATA.length, read);
        Assert.assertArrayEquals(TEST_DATA, decoded);
    }

    @Test
    public void testEncodeConstructor() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Base64OutputStream bos = new Base64OutputStream(os);
        int read = is.read(singleByte, 0, 1);
        while (read > 0) {
            bos.write(singleByte, 0, read);
            read = is.read(singleByte, 0, 1);
        }
        bos.close();
        String encoded = os.toString();
        Assert.assertEquals("VG8gZXhwbGFuZGVyZW5jZQ==\r\n", encoded);
    }

    @Test
    public void testEncodeConstructorWithParams() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, 76, "\r\n".getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Base64OutputStream bos = new Base64OutputStream(os);
        int read = is.read(singleByte, 0, 1);
        while (read > 0) {
            bos.write(singleByte, 0, read);
            read = is.read(singleByte, 0, 1);
        }
        bos.close();
        String encoded = os.toString();
        Assert.assertEquals("VG8gZXhwbGFuZGVyZW5jZQ==\r\n", encoded);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        new Base64InputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLineLength() {
        new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, -5, "\r\n".getBytes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLineSeparator() {
        new Base64InputStream(new ByteArrayInputStream(TEST_DATA), true, 76, null);
    }

    @Test
    public void testReadMethodWithEmptyBuffer() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        byte[] decoded = new byte[TEST_DATA.length];
        int read = is.read(decoded, 0, 0);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadMethodWithSmallBuffer() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        byte[] decoded = new byte[3];
        int read = is.read(decoded, 0, decoded.length);
        Assert.assertArrayEquals(new byte[]{TEST_DATA[0], TEST_DATA[1], TEST_DATA[2]}, decoded);
        Assert.assertEquals(3, read);
    }

    @Test
    public void testReadMethodWithFullBuffer() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        byte[] decoded = new byte[TEST_DATA.length];
        int read = is.read(decoded, 0, decoded.length);
        Assert.assertEquals(TEST_DATA.length, read);
        Assert.assertArrayEquals(TEST_DATA, decoded);
    }

    @Test
    public void testReadMethodWithLargerBuffer() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        byte[] decoded = new byte[TEST_DATA.length + 1];
        int read = is.read(decoded, 0, decoded.length);
        Assert.assertEquals(TEST_DATA.length, read);
        Assert.assertArrayEquals(TEST_DATA, decoded, 0, TEST_DATA.length);
    }

    @Test
    public void testReadMethodWithBufferBeyondEndOfStream() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        byte[] decoded = new byte[TEST_DATA.length + 1];
        int read = is.read(decoded, 0, decoded.length);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testMarkSupportedMethod() throws IOException {
        Base64InputStream is = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        Assert.assertFalse(is.markSupported());
    }
}