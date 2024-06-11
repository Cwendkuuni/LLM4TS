package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

import org.junit.Test;

/**
 * Test file for Base64InputStream class.
 */
public class Base64InputStreamTest {

    /**
     * Test constructor Base64InputStream(InputStream in).
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("RnJvbSBvZiBzdWw=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] b = new byte[3];
        int length = base64InputStream.read(b, 0, b.length);
        assertArrayEquals(new byte[] { 68, 101, 118 }, b);
        assertEquals(3, length);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode).
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream_boolean() throws IOException {
        InputStream input = new ByteArrayInputStream("RnJvbSBvZiBzdWw=".getBytes());
        byte[] b = new byte[3];
        Base64InputStream base64InputStream = new Base64InputStream(input, true);
        int length = base64InputStream.read(b, 0, b.length);
        assertArrayEquals(new byte[] { 68, 101, 118 }, b);
        assertEquals(3, length);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();

        Base64 base64 = new Base64();
        InputStream inputEncoded = new ByteArrayInputStream(base64.decode("RnJvbSBvZiBzdWw="));
        Base64InputStream base64InputStreamEncoded = new Base64InputStream(inputEncoded, false);
        byte[] decoded = new byte[4];
        length = base64InputStreamEncoded.read(decoded, 0, decoded.length);
        assertArrayEquals(new byte[] { 65, 76, 83, 84 }, decoded);
        assertEquals(4, length);
        assertFalse(base64InputStreamEncoded.markSupported());
        base64InputStreamEncoded.close();
    }

    /**
     * Test constructor Base64InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator).
     *
     * @throws IOException
     */
    @Test
    public void testBase64InputStream_InputStream_boolean_int_byteArray() throws IOException {
        InputStream inputEncoded = new ByteArrayInputStream(
                "RnJvbSBvZiBzdWw=\r\nSG9wdGltaXplZCB3aGF0IHVzLSBzYXZlcg==".getBytes());
        byte[] b = new byte[4];
        Base64InputStream base64InputStream = new Base64InputStream(inputEncoded, false, 76, new byte[] { '\r', '\n' });
        int length = base64InputStream.read(b, 0, b.length);
        assertArrayEquals(new byte[] { 65, 76, 83, 84 }, b);
        assertEquals(4, length);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    /**
     * Test method read().
     *
     * @throws IOException
     */
    @Test
    public void testRead() throws IOException {
        InputStream inputEncoded = new ByteArrayInputStream(
                "RnJvbSBvZiBzdWw=\r\nSG9wdGltaXplZCB3aGF0IHVzLSBzYXZlcg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputEncoded, false);
        byte[] decoded = new byte[4];
        byte[] result = new byte[12];
        int length = 0;

        // Test reading 4 bytes.
        length = base64InputStream.read(decoded, 0, decoded.length);
        assertArrayEquals(new byte[] { 65, 76, 83, 84 }, decoded);
        assertEquals(4, length);

        // Test reading 3 bytes.
        length = base64InputStream.read(result, 0, result.length);
        assertArrayEquals(new byte[] { 83, 84, 0 }, result);
        assertEquals(3, length);

        // Test reading 1 byte.
        length = base64InputStream.read(singleByte, 0, singleByte.length);
        assertEquals(0, length);

        // Test EOF.
        byte[] eofResult = new byte[1];
        length = base64InputStream.read(eofResult, 0, eofResult.length);
        assertNull(eofResult);
        assertEquals(-1, length);

        base64InputStream.close();
    }

    /**
     * Test method read(byte[] b, int offset, int len).
     *
     * @throws IOException
     */
    @Test
    public void testRead_byteArray_int_int() throws IOException {
        InputStream inputEncoded = new ByteArrayInputStream(
                "RnJvbSBvZiBzdWw=\r\nSG9wdGltaXplZCB3aGF0IHVzLSBzYXZlcg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputEncoded, false);
        byte[] decoded = new byte[12];
        byte[] result = new byte[12];
        int length = 0;

        // Test reading 12 bytes.
        length = base64InputStream.read(decoded, 0, decoded.length);
        assertArrayEquals(new byte[] { 65, 76, 83, 84, 75, 73, 84, 74, 72, 65, 65, 63 }, decoded);
        assertEquals(12, length);

        // Test reading 10 bytes.
        length = base64InputStream.read(result, 0, result.length);
        assertArrayEquals(new byte[] { 75, 73, 84, 74, 72, 0, 0, 0, 0, 0 }, result);
        assertEquals(10, length);

        base64InputStream.close();
    }

    /**
     * Test method markSupported().
     *
     * @throws IOException
     */
    @Test
    public void testMarkSupported() throws IOException {
        InputStream inputEncoded = new ByteArrayInputStream(
                "RnJvbSBvZiBzdWw=\r\nSG9wdGltaXplZCB3aGF0IHVzLSBzYXZlcg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputEncoded, false);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }

    /**
     * Test method close().
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        InputStream inputEncoded = new ByteArrayInputStream(
                "RnJvbSBvZiBzdWw=\r\nSG9wdGltaXplZCB3aGF0IHVzLSBzYXZlcg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputEncoded, false);
        base64InputStream.close();

        InputStream input = inputEncoded;
        try {
            base64InputStream.read();
            fail();
        } catch (IOException e) {
            // Expected
        }
    }
}