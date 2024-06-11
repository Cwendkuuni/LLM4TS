package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * Test cases for Base64InputStream class.
 *
 * @author John Doe
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding behavior.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testDefaultDecodingBehavior() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);
        base64OutputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes());
        base64OutputStream.finish();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream inputStream = new Base64InputStream(byteArrayInputStream);

        byte[] buffer = new byte[24];
        int len;
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            assertArrayEquals("Encoded data should be decoded correctly", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(), buffer, 0, len);
        }
        inputStream.close();
    }

    /**
     * Test constructor with custom encoding/decoding behavior.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testCustomEncodingDecodingBehavior() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64InputStream inputStream = new Base64InputStream(byteArrayOutputStream, true); // encoding

        inputStream.write(0);
        inputStream.write(1);
        inputStream.write(2);
        inputStream.finish();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64 base64 = new Base64();
        byte[] decodedData = base64.decode(byteArrayInputStream.readAllBytes());

        assertEquals("Encoded and decoded data should be equal", (byte) 0, decodedData[0]);
        assertEquals("Encoded and decoded data should be equal", (byte) 1, decodedData[1]);
        assertEquals("Encoded and decoded data should be equal", (byte) 2, decodedData[2]);

        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream2, 64);
        base64OutputStream.write(0);
        base64OutputStream.write(1);
        base64OutputStream.write(2);
        base64OutputStream.finish();

        ByteArrayInputStream byteArrayInputStream2 = new ByteArrayInputStream(byteArrayOutputStream2.toByteArray());
        Base64InputStream inputStream2 = new Base64InputStream(byteArrayInputStream2, false); // decoding

        byte[] buffer = new byte[3];
        int len2;
        while ((len2 = inputStream2.read(buffer, 0, buffer.length)) != -1) {
            assertArrayEquals("Encoded and decoded data should be equal", new byte[] { 0, 1, 2 }, buffer, 0, len2);
        }
        inputStream2.close();
    }

    /**
     * Test constructor with null input stream.
     */
    @org.junit.Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        new Base64InputStream(null, false);
    }

    /**
     * Test read method with empty input stream.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEmptyInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream inputStream = new Base64InputStream(byteArrayInputStream, false);
        byte[] buffer = new byte[1];
        assertFalse("Should return -1", inputStream.read(buffer, 0, buffer.length) != -1);
        inputStream.close();
    }

    /**
     * Test read method with EOF reached.
     *
     * @throws IOException
     */
    @org.junit.Test
    public void testEndOfStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream inputStream = new Base64InputStream(byteArrayInputStream, false);

        byte[] buffer = new byte[1];
        int read = inputStream.read(buffer, 0, buffer.length);
        assertFalse("Should return -1", read != -1);

        read = inputStream.read(buffer, 0, buffer.length);
        assertTrue("Should reach EOF", read == -1);
        inputStream.close();
    }

    /**
     * Test markSupported method.
     */
    @org.junit.Test
    public void testMarkSupported() {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        assertFalse("Mark support is not implemented", inputStream.markSupported());
        inputStream.close();
    }
}