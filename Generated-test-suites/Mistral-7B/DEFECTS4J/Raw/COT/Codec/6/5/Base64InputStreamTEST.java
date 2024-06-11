package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    // Test constructor with default settings
    @org.junit.Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expected = { 't', 'e', 's', 't' };
        byte[] result = new byte[4];

        int len;
        while ((len = base64InputStream.read(result, 0, result.length)) > 0) {
            assertArrayEquals("Unexpected decoded byte[]", expected, result, 0, len);
        }
    }

    // Test constructor with encoding
    @org.junit.Test
    public void testEncodingConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[]{"test".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] result = baos.toByteArray();

        while ((len = base64InputStream.read(result, 0, result.length)) > 0) {
            baos.flush();
        }

        String expected = "VG8gZXih";
        String resultStr = new String(baos.toByteArray());

        assertEquals("Unexpected encoded string", expected, resultStr);
    }

    // Test constructor with decoding and custom lineLength and lineSeparator
    @org.junit.Test
    public void testCustomConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"VG8gZXih\r\n".getBytes()});
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 76, "\r\n".getBytes());

        byte[] expected = { 't', 'e', 's', 't' };
        byte[] result = new byte[4];

        int len;
        while ((len = base64InputStream.read(result, 0, result.length)) > 0) {
            assertArrayEquals("Unexpected decoded byte[]", expected, result, 0, len);
        }
    }

    // Test read method
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadWithNullByteArray() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{}), true);
        base64InputStream.read(null, 0, 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{"test".getBytes()}), true);
        base64InputStream.read(new byte[]{-1}, 0, 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{"test".getBytes()}), true);
        base64InputStream.read(new byte[]{'b'}, -1, 1);
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithOffsetBeyondArraySize() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{"test".getBytes()}), true);
        byte[] b = new byte[1];
        base64InputStream.read(b, b.length, 1);
    }

    // Test markSupported method
    @org.junit.Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[]{"test".getBytes()}), true);
        assertFalse("markSupported should return false", base64InputStream.markSupported());
    }
}