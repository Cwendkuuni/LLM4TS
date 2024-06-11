package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JUnit test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA = "This is a test Base64InputStream.";
    private static final byte[] TEST_DATA_BYTES = TEST_DATA.getBytes(StandardCharsets.UTF_8);

    /**
     * Test decoding a Base64-encoded InputStream.
     */
    @Test
    public void testDecodeBase64InputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64.encodeBase64(TEST_DATA_BYTES, outputStream.getBuffer(), false);
        outputStream.flush();
        byte[] encodedData = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] resultBytes = new byte[TEST_DATA_BYTES.length];
        int bytesRead = base64InputStream.read(resultBytes, 0, resultBytes.length);
        Assert.assertEquals(TEST_DATA_BYTES.length, bytesRead);
        Assert.assertArrayEquals(TEST_DATA_BYTES, resultBytes);
    }

    /**
     * Test encoding a Base64-decodable InputStream.
     */
    @Test
    public void testEncodeBase64InputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA_BYTES), true);
        byte[] readByte = new byte[1];
        while (base64InputStream.read(readByte, 0, 1) != -1) {
            outputStream.write(readByte);
        }
        outputStream.flush();
        byte[] encodedData = outputStream.toByteArray();
        String decodedData = new String(Base64.decodeBase64(encodedData), StandardCharsets.UTF_8);
        Assert.assertEquals(TEST_DATA, decodedData);
    }

    /**
     * Test decoding an empty Base64-encoded InputStream.
     */
    @Test
    public void testDecodeEmptyBase64InputStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        byte[] resultBytes = new byte[0];
        int bytesRead = base64InputStream.read(resultBytes, 0, resultBytes.length);
        Assert.assertEquals(0, bytesRead);
    }

    /**
     * Test decoding a Base64-encoded InputStream with an invalid character.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidBase64InputStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write('='); // add an invalid character
        byte[] invalidData = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(invalidData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.read(); // consume the first byte to advance the stream
        base64InputStream.read(); // consume the second byte to advance the stream
        base64InputStream.read(); // consume the third byte to advance the stream
        base64InputStream.read(); // consume the fourth byte to advance the stream
        int bytesRead = base64InputStream.read(new byte[1], 0, 1); // read a byte
    }

    /**
     * Test decoding a Base64-encoded InputStream with an unsupported line length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecodeInvalidLineLengthBase64InputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, -1, 1, new byte[0]);
    }
}