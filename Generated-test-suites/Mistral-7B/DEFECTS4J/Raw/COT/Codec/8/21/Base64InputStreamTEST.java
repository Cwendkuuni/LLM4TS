import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base64InputStreamTest
 */
public class Base64InputStreamTest {

    @Test
    public void testDefaultConstructor() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41, 0x42, 0x43}));
        byte[] decoded = new byte[3];
        int read = inputStream.read(decoded, 0, 3);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{0x41, 0x42, 0x43}, decoded);
        inputStream.close();
    }

    @Test
    public void testEncodeConstructor() throws IOException {
        Base64OutputStream outputStream = new Base64OutputStream(new ByteArrayOutputStream());
        outputStream.write(new byte[]{0x41, 0x42, 0x43});
        outputStream.finish();
        ByteArrayInputStream encodedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream inputStream = new Base64InputStream(encodedInputStream, true);
        byte[] encoded = new byte[4];
        int read = inputStream.read(encoded, 0, 4);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{0x41, 0x42, 0x43}, decodedFromBase64(encoded, 0, 3));
        inputStream.close();
    }

    @Test
    public void testCustomLineLengthAndLineSeparatorConstructor() throws IOException {
        Base64OutputStream outputStream = new Base64OutputStream(new ByteArrayOutputStream());
        outputStream.write(new byte[]{0x41, 0x42, 0x43});
        outputStream.finish();
        ByteArrayInputStream encodedInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream inputStream = new Base64InputStream(encodedInputStream, true, 50, new byte[]{'\r', '\n'});
        byte[] encoded = new byte[4];
        int read = inputStream.read(encoded, 0, 4);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{0x41, 0x42, 0x43}, decodedFromBase64(encoded, 0, 3));
        Assert.assertArrayEquals(new byte[]{'.'}, inputStream.read(encoded, 0, encoded.length)); // Check for empty line
        inputStream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        new Base64InputStream(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeRead() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}));
        byte[] decoded = new byte[1];
        int read = inputStream.read(decoded, 2, 1);
        inputStream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeWrite() throws IOException {
        Base64OutputStream outputStream = new Base64OutputStream(new ByteArrayOutputStream());
        outputStream.write(new byte[]{0x41});
        outputStream.finish();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true);
        byte[] encoded = new byte[3];
        int read = inputStream.read(encoded, 2, 1);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testNegativeLineLength() throws IOException {
        new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}), true, -1, new byte[]{'\r', '\n'});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLineSeparator() throws IOException {
        new Base64InputStream(new ByteArrayInputStream(new byte[]{0x41}), true, 50, new byte[]{'\r', '\n', 0x01});
    }

    private byte[] decodedFromBase64(byte[] encoded, int offset, int length) {
        Base64 decoder = new Base64();
        return decoder.decode(encoded, offset, length);
    }
}