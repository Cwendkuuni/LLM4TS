package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test encoding and decoding of Base64InputStream
     */
    @Test
    public void testBase64InputStream() throws IOException {
        String originalText = "Hello, World!";
        String encodedText = "SGVsbG8sIFdvcmxkIQ==";

        // Create a ByteArrayOutputStream to store the encoded text
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);
        base64OutputStream.write(originalText.getBytes());
        base64OutputStream.finish();
        IOUtils.closeQuietly(base64OutputStream);

        // Create a ByteArrayInputStream from the encoded text
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // Create a Base64InputStream with decoding behavior
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        // Read the decoded text from the Base64InputStream
        byte[] decodedText = new byte[originalText.length()];
        int numberOfBytesRead = base64InputStream.read(decodedText, 0, originalText.length());
        Assert.assertEquals(numberOfBytesRead, originalText.length());

        // Convert decoded bytes to String and compare with original text
        String decodedString = new String(decodedText, 0, numberOfBytesRead);
        Assert.assertEquals(decodedString, originalText);

        // Close resources
        IOUtils.closeQuietly(base64InputStream);
        IOUtils.closeQuietly(byteArrayInputStream);

        // Test encoding and decoding round-trip
        ByteArrayOutputStream roundTripByteArrayOutputStream = new ByteArrayOutputStream();
        Base64InputStream roundTripBase64InputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false);
        IOUtils.copy(roundTripBase64InputStream, roundTripByteArrayOutputStream);
        IOUtils.closeQuietly(roundTripBase64InputStream);
        Assert.assertArrayEquals(byteArrayOutputStream.toByteArray(), roundTripByteArrayOutputStream.toByteArray());

        // Test with empty input
        Base64InputStream emptyBase64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        IOUtils.closeQuietly(emptyBase64InputStream);

        // Test with incorrect input
        ByteArrayInputStream incorrectByteArrayInputStream = new ByteArrayInputStream(new byte[]{-1, -2, -3});
        Base64InputStream incorrectBase64InputStream = new Base64InputStream(incorrectByteArrayInputStream, false);
        int numberOfBytesReadIncorrect = base64InputStream.read(decodedText, 0, originalText.length());
        Assert.assertEquals(numberOfBytesReadIncorrect, -1);
        IOUtils.closeQuietly(incorrectBase64InputStream);
        IOUtils.closeQuietly(incorrectByteArrayInputStream);
    }

    /**
     * Test encoding and decoding of Base64InputStream with custom line length and line separator
     */
    @Test
    public void testBase64InputStreamCustomLineLengthAndLineSeparator() throws IOException {
        String originalText = "Hello, World!";
        String encodedText = "SGVsbG8sIFdvcmxkIQ==";

        // Create a ByteArrayOutputStream to store the encoded text
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 70, "\r\n".getBytes());
        base64OutputStream.write(originalText.getBytes());
        base64OutputStream.finish();
        IOUtils.closeQuietly(base64OutputStream);

        // Create a ByteArrayInputStream from the encoded text
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

        // Create a Base64InputStream with decoding behavior and custom line length and line separator
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, false, 70, "\r\n".getBytes());

        // Read the decoded text from the Base64InputStream
        byte[] decodedText = new byte[originalText.length()];
        int numberOfBytesRead = base64InputStream.read(decodedText, 0, originalText.length());
        Assert.assertEquals(numberOfBytesRead, originalText.length());

        // Convert decoded bytes to String and compare with original text
        String decodedString = new String(decodedText, 0, numberOfBytesRead);
        Assert.assertEquals(decodedString, originalText);

        // Close resources
        IOUtils.closeQuietly(base64InputStream);
        IOUtils.closeQuietly(byteArrayInputStream);

        // Test encoding and decoding round-trip with custom line length and line separator
        ByteArrayOutputStream roundTripByteArrayOutputStream = new ByteArrayOutputStream();
        Base64InputStream roundTripBase64InputStream = new Base64InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false, 70, "\r\n".getBytes());
        IOUtils.copy(roundTripBase64InputStream, roundTripByteArrayOutputStream);
        IOUtils.closeQuietly(roundTripBase64InputStream);
        Assert.assertArrayEquals(byteArrayOutputStream.toByteArray(), roundTripByteArrayOutputStream.toByteArray());
    }
}