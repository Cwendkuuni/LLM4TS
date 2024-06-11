import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructorWithoutEncoding() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] decodedBytes = new byte[10];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        Assert.assertNotEqual(0, length);
        Assert.assertEquals("Base64 decoded data", decodedBytes, 0, length);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncoding() {
        InputStream inputStream = new ByteArrayInputStream("plain data".getBytes());
        Base64OutputStream base64OutputStream = new Base64OutputStream();
        base64OutputStream.write(inputStream);
        byte[] encodedBytes = base64OutputStream.toByteArray();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(encodedBytes), true);

        byte[] decodedBytes = new byte[10];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        Assert.assertNotEqual(0, length);
        Assert.assertEquals("plain data", decodedBytes, 0, length);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamWithNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNegativeLineLength() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNullLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -5, null);
    }

    @Test
    public void testBase64InputStreamWithEmptyLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[0]);

        byte[] decodedBytes = new byte[10];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        Assert.assertNotEqual(0, length);
        Assert.assertEquals("Base64 decoded data", decodedBytes, 0, length);
    }

    @Test
    public void testBase64InputStreamWithLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\n\r".getBytes());

        byte[] decodedBytes = new byte[10];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        Assert.assertNotEqual(0, length);
        Assert.assertEquals("Base64 decoded data", decodedBytes, 0, length);
    }

    @Test
    public void testBase64InputStreamWithNegativeLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -5, "\n\r".getBytes());
    }

    @Test
    public void testBase64InputStreamWithLineLengthZero() {
        InputStream inputStream = new ByteArrayInputStream("Base64 encoded data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);

        byte[] decodedBytes = new byte[10];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        Assert.assertNotEqual(0, length);
        Assert.assertEquals("Base64 decoded data", decodedBytes, 0, length);
    }
}