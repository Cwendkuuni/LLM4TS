import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Tests for Base64InputStream class.
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamBasicFunctionality() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Testing Base64InputStream".getBytes());
        byte[] dataToEncode = outputStream.toByteArray();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(dataToEncode);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] decodedData = new byte[dataToEncode.length];
        int decodedBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        Assert.assertEquals(dataToEncode.length, decodedBytes);
        Assert.assertArrayEquals(dataToEncode, decodedData);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamDecodeInvalidData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("InvalidBase64Data".getBytes()); // Base64 encoding of 'InvalidBase64Data' is not a valid Base64 string

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodedData = new byte[inputStream.available()];
        int decodedBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        Assert.assertEquals(-1, decodedBytes); // EOF should be reached since the input stream contains invalid Base64 data

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        String dataToEncode = "Testing Base64InputStream";
        Base64 base64 = new Base64();

        byte[] encodedData = base64.encode(dataToEncode.getBytes());

        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodedData = new byte[encodedData.length];
        int decodedBytes = base64InputStream.read(decodedData, 0, decodedData.length);

        Assert.assertEquals(encodedData.length, decodedBytes);
        Assert.assertArrayEquals(encodedData, decodedData);

        base64InputStream.close();
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamExceptionWhenClosingStreamTwice() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        base64InputStream.close();
        base64InputStream.close();
    }

}