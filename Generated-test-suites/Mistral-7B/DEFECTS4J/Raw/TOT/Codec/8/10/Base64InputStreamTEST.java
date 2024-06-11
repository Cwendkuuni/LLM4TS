package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

/**
 * Test cases for Base64InputStream class.
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private InputStream base64DecodedInputStream;
    private InputStream base64EncodedInputStream;
    private InputStream emptyInputStream;

    @Before
    public void setUp() throws Exception {
        String plainText = "Hello, World!";
        byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, false);
        base64OutputStream.write(plainTextBytes, 0, plainTextBytes.length);
        base64OutputStream.finish();
        byte[] base64EncodedBytes = byteArrayOutputStream.toByteArray();

        inputStream = new ByteArrayInputStream(plainTextBytes);
        base64DecodedInputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes), false);
        base64EncodedInputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes), true);
        emptyInputStream = new ByteArrayInputStream(new byte[0]);
    }

    @After
    public void tearDown() throws Exception {
        if (inputStream != null) {
            inputStream.close();
        }
        if (base64DecodedInputStream != null) {
            base64DecodedInputStream.close();
        }
        if (base64EncodedInputStream != null) {
            base64EncodedInputStream.close();
        }
        if (emptyInputStream != null) {
            emptyInputStream.close();
        }
    }

    /**
     * Test decoding of Base64-encoded data.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testDecodeBase64() throws IOException {
        byte[] expected = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        byte[] readBytes = new byte[expected.length];
        int readLen = base64DecodedInputStream.read(readBytes, 0, expected.length);
        assertEquals("Decoded Base64 data does not match original data", expected, readBytes, 0, expected.length);
    }

    /**
     * Test decoding of empty Base64-encoded data.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testDecodeEmptyBase64() throws IOException {
        byte[] readBytes = new byte[0];
        int readLen = base64DecodedInputStream.read(readBytes, 0, readBytes.length);
        assertEquals("Expected empty input to return -1, but got " + readLen, -1, readLen);
    }

    /**
     * Test decoding of Base64-encoded data with invalid padding.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalidBase64() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, false);
        byte[] plainTextBytes = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        base64OutputStream.write(plainTextBytes, 0, plainTextBytes.length - 1);
        base64OutputStream.finish();
        byte[] base64EncodedBytes = byteArrayOutputStream.toByteArray();

        InputStream base64InvalidEncodedInputStream = new Base64InputStream(new ByteArrayInputStream(base64EncodedBytes), false);
        byte[] readBytes = new byte[plainTextBytes.length];
        int readLen = base64InvalidEncodedInputStream.read(readBytes, 0, plainTextBytes.length);
        assertEquals("Decoded Base64 data does not match original data", plainTextBytes, readBytes, 0, plainTextBytes.length);
    }

    /**
     * Test encoding of data.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testEncodeBase64() throws IOException {
        byte[] expected = "SGVsbG8sIFdvcmxkIQ==".getBytes(StandardCharsets.UTF_8);
        byte[] readBytes = new byte[expected.length];
        int readLen = base64EncodedInputStream.read(readBytes, 0, expected.length);
        assertEquals("Encoded data does not match expected data", expected, readBytes, 0, expected.length);
    }

    /**
     * Test encoding of empty data.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testEncodeEmpty() throws IOException {
        byte[] readBytes = new byte[0];
        int readLen = base64EncodedInputStream.read(readBytes, 0, readBytes.length);
        assertEquals("Expected empty input to return -1, but got " + readLen, -1, readLen);
    }
}