package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base64InputStreamTest class.
 */
public class Base64InputStreamTest {

    /**
     * Test encoding and decoding of a simple byte array.
     */
    @Test
    public void testEncodeAndDecodeSimpleByteArray() throws IOException {
        String sourceData = "Hello, World!";
        byte[] sourceBytes = sourceData.getBytes();

        // Encode the source data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(sourceBytes, 0, sourceBytes.length);
        base64OutputStream.close();
        byte[] encodedBytes = outputStream.toByteArray();

        // Decode the encoded data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] decodedBytes = new byte[sourceBytes.length];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        base64InputStream.close();

        // Compare the decoded data to the original source data
        assertArrayEquals(sourceBytes, decodedBytes);
    }

    /**
     * Test decoding an empty input stream.
     */
    @Test
    public void testDecodeEmptyInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int length = base64InputStream.read();
        base64InputStream.close();

        // Should return -1 since the input stream was empty
        assertEquals(-1, length);
    }

    /**
     * Test decoding a malformed Base64 input stream.
     */
    @Test(expected = IOException.class)
    public void testDecodeMalformedInputStream() throws IOException {
        String malformedInput = "VGhlIGlzIG15IGxpbmsKCmVuZG9iago=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(malformedInput.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        // The malformed input should throw an IOException
        base64InputStream.read();
    }

    /**
     * Test encoding and decoding a large byte array.
     */
    @Test
    public void testEncodeAndDecodeLargeByteArray() throws IOException {
        String largeData = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.";
        byte[] largeBytes = largeData.getBytes();

        // Encode the large source data
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(largeBytes, 0, largeBytes.length);
        base64OutputStream.close();
        byte[] encodedBytes = outputStream.toByteArray();

        // Decode the encoded data
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] decodedBytes = new byte[largeBytes.length];
        int length = base64InputStream.read(decodedBytes, 0, decodedBytes.length);
        base64InputStream.close();

        // Compare the decoded data to the original source data
        assertArrayEquals(largeBytes, decodedBytes);
    }

    /**
     * Test decoding an input stream with invalid line length and line separator.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidLineLengthAndLineSeparator() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("VGhlIGlzIG15IGxpbmsKCmVuZG9iag==\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, null);

        // The invalid line length and line separator should throw an IOException
        base64InputStream.read();
    }
}