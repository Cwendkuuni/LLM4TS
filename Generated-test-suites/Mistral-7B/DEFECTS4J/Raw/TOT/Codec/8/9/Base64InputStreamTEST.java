package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

/**
 * Test case for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Tests the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggc29tZSBmaWxlLg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expected = "Hello World!".getBytes();
        byte[] actual = new byte[expected.length];
        int actualLen = base64InputStream.read(actual);

        assertEquals(expected.length, actualLen);
        assertArrayEquals(expected, actual);
        assertFalse(base64InputStream.hasNextByte());
    }

    /**
     * Tests the constructor with decoding.
     */
    @Test
    public void testDecodeConstructor() {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        byte[] expected = "Hello World!".getBytes();
        byte[] actual = new byte[expected.length];
        int actualLen = base64InputStream.read(actual);

        assertEquals(expected.length, actualLen);
        assertArrayEquals(expected, actual);
        assertFalse(base64InputStream.hasNextByte());
    }

    /**
     * Tests the constructor with encoding.
     */
    @Test
    public void testEncodeConstructor() {
        String original = "Hello World!";
        byte[] bytes = original.getBytes();
        InputStream input = new ByteArrayInputStream(bytes);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(output, 76, "\r\n");
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 76, "\r\n".getBytes());
        int len;
        byte[] buffer = new byte[1];

        while ((len = base64InputStream.read(buffer)) > 0) {
            output.write(buffer, 0, len);
        }

        String actual = output.toString();
        String expected = "V2UgZW5jIHlvdSBjb250aW51IGRhdGEgY29tcHV0ZW1lbnQ=";
        assertEquals(expected, actual);
    }

    /**
     * Tests the constructor with invalid length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDecodeConstructorWithInvalidLength() {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false, -1, null);
    }

    /**
     * Tests the constructor with null line separator.
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeConstructorWithNullLineSeparator() {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 76, null);
    }

    /**
     * Tests the constructor with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testDecodeConstructorWithNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

}