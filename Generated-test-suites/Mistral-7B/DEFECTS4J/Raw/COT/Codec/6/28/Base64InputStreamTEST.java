import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream.
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_DefaultConstructor() throws IOException {
        // Create a Base64InputStream with default decoding behavior
        InputStream inputStream = new ByteArrayInputStream("Base64 data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Read data from the Base64InputStream and write it to a ByteArrayOutputStream for verification
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = base64InputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
        }

        byte[] expectedData = "Data".getBytes();
        Assert.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStream_EncodingConstructor() throws IOException {
        // Create a Base64InputStream with encoding behavior
        InputStream inputStream = new ByteArrayInputStream("Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // Read data from the Base64InputStream and write it to a ByteArrayOutputStream for verification
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = base64InputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
        }

        String expectedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        Assert.assertEquals(expectedData, outputStream.toString());
    }

    @Test
    public void testBase64InputStream_CustomLineLengthAndLineSeparatorConstructor() throws IOException {
        // Create a Base64InputStream with custom line length and line separator
        InputStream inputStream = new ByteArrayInputStream("Data".getBytes());
        Base64 base64 = new Base64(72, "\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, "\r\n".getBytes());

        // Read data from the Base64InputStream and write it to a ByteArrayOutputStream for verification
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int length;
        while ((length = base64InputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, length);
        }

        String expectedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g==\r\n";
        Assert.assertEquals(expectedData, outputStream.toString());
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_IOExceptionThrownWhenInputStreamThrowsException() throws IOException {
        // Create a Base64InputStream with default decoding behavior
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Expect an IOException to be thrown when reading from the InputStream
        base64InputStream.read();
    }

    @Test
    public void testBase64InputStream_MarkSupportedMethodReturnsFalse() throws IOException {
        // Create a Base64InputStream
        InputStream inputStream = new ByteArrayInputStream("Data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Assert that markSupported method returns false
        Assert.assertFalse(base64InputStream.markSupported());
    }
}