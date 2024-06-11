import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default settings.
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[4];
        int length = base64InputStream.read(buffer, 0, 4);

        Assert.assertEquals(3, length);
        Assert.assertArrayEquals("test".getBytes(), new byte[]{"t", "e", "s"});
        base64InputStream.close();
    }

    /**
     * Test constructor with decode mode.
     * @throws IOException
     */
    @Test
    public void testDecodeConstructor() throws IOException {
        String encodedData = "Rm9udCAoKSB7CiAgICAtLVBhdGg=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[5];
        int length = base64InputStream.read(buffer, 0, 5);

        Assert.assertEquals(4, length);
        Assert.assertArrayEquals("test".getBytes(), buffer, 0, 3);
        base64InputStream.close();
    }

    /**
     * Test constructor with encode mode and line length 76.
     * @throws IOException
     */
    @Test
    public void testEncodeConstructorWithLineLength() throws IOException {
        String data = "test";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76);
        base64OutputStream.write(data.getBytes());
        base64OutputStream.close();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()), true, 76);

        byte[] buffer = new byte[8];
        int length = base64InputStream.read(buffer, 0, 8);

        Assert.assertEquals(7, length);
        Assert.assertArrayEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJy", buffer, 0, 7);
        base64InputStream.close();
    }

    /**
     * Test exception handling when attempting to mark the stream.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.markSupported();
    }

    /**
     * Test exception handling when reading past EOF.
     */
    @Test(expected = IOException.class)
    public void testReadPastEOF() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[4];
        int length = base64InputStream.read(buffer, 0, 4);

        Assert.assertEquals(3, length);
        base64InputStream.read(buffer, 0, 1); // read 3 bytes
        base64InputStream.read(buffer, 0, 1); // read 3 bytes
        base64InputStream.read(buffer, 0, 1); // read 3 bytes
        base64InputStream.read(buffer, 0, 1); // read EOF

        int length2 = base64InputStream.read(buffer, 0, 1); // should throw an exception
    }
}