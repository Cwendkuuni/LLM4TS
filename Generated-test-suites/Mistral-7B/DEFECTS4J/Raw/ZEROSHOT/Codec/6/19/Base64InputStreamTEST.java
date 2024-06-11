import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link Base64InputStream} class.
 */
public class Base64InputStreamTest {

    /**
     * Test for default constructor.
     *
     * @throws IOException if an error occurs during the test.
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', ' ', ' ', 'i', 'n', ' ', 'I', 'n', 'p', 'u', 't', ' ', 'S', 't', 'r', 'e', 'a', 'm', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '};

        byte[] readBytes = new byte[expectedBytes.length];
        int read = base64InputStream.read(readBytes, 0, expectedBytes.length);
        Assert.assertEquals(expectedBytes.length, read);
        Assert.assertArrayEquals(expectedBytes, readBytes);

        base64InputStream.close();
    }

    /**
     * Test for constructor with decode flag.
     *
     * @throws IOException if an error occurs during the test.
     */
    @Test
    public void testDecodeConstructor() throws IOException {
        String encodedData = "RnJvbSBzYXZlIGlzIGh1bWFuLCBmaWxlIGRhdGFbMSI6IHsKCiAgICAtLVBhdGgiLCBjb250ZW50IHRoZSBzaWdodCwNCkBnbWFpbCwNCkBzcGFjZSBmaWxlIG9mIHRoZSBzdWJmaXggPSBzbyB0aGUxLg==";

        InputStream inputStream = new ByteArrayInputStream(encodedData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', ' ', ' ', 'i', 'n', ' ', 'I', 'n', 'p', 'u', 't', ' ', 'S', 't', 'r', 'e', 'a', 'm'};

        byte[] readBytes = new byte[expectedBytes.length];
        int read = base64InputStream.read(readBytes, 0, expectedBytes.length);
        Assert.assertEquals(expectedBytes.length, read);
        Assert.assertArrayEquals(expectedBytes, readBytes);

        base64InputStream.close();
    }

    /**
     * Test for constructor with encode flag.
     *
     * @throws IOException if an error occurs during the test.
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        String data = "Test data";
        Base64 base64 = new Base64();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputBytes = data.getBytes();
        base64.encode(outputStream, inputBytes, 0, inputBytes.length);

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] expectedBytes = inputBytes;

        byte[] readBytes = new byte[expectedBytes.length];
        int read = base64InputStream.read(readBytes, 0, expectedBytes.length);
        Assert.assertEquals(expectedBytes.length, read);
        Assert.assertArrayEquals(expectedBytes, readBytes);

        base64InputStream.close();
    }

    /**
     * Test for constructor with encode flag, line length and line separator.
     *
     * @throws IOException if an error occurs during the test.
     */
    @Test
    public void testEncodeConstructorWithLineLengthAndLineSeparator() throws IOException {
        String data = "Test data";
        Base64 base64 = new Base64(64, '\n'.toString().getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputBytes = data.getBytes();
        base64.encode(outputStream, inputBytes, 0, inputBytes.length);

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 64, "\n".getBytes());

        byte[] expectedBytes = inputBytes;

        byte[] readBytes = new byte[expectedBytes.length];
        int read = base64InputStream.read(readBytes, 0, expectedBytes.length);
        Assert.assertEquals(expectedBytes.length, read);
        Assert.assertArrayEquals(expectedBytes, readBytes);

        base64InputStream.close();
    }
}