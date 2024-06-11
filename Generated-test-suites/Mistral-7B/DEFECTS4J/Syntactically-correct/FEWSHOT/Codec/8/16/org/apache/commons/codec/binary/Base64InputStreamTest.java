package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    private static final byte[] DATA = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };

    @Test
    public void testBase64InputStream() {
        InputStream input = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        Base64 base64 = base64InputStream.base64;

        assertEquals(false, base64InputStream.doEncode);
        assertSame(input, base64InputStream.in);
        assertSame(Base64.NO_LINE_BREAK, base64InputStream.lineSeparator);
        assertSame(Base64.DEFAULT_LINE_LENGTH, base64InputStream.lineLength);
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        InputStream input = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expected = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
        byte[] result = new byte[expected.length];
        int len = base64InputStream.read(result);

        assertArrayEquals(expected, result);
        assertEquals(expected.length, len);

        assertFalse(base64InputStream.hasNextLine());
        assertFalse(base64InputStream.hasNext());
    }

    @Test
    public void testBase64InputStreamDecodeWithLineBreaks() throws IOException {
        InputStream input = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input, 72, new byte[] { '\r', '\n' });

        byte[] expected = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
        byte[] result = new byte[expected.length];
        int len = base64InputStream.read(result);

        assertArrayEquals(expected, result);
        assertEquals(expected.length, len);

        assertTrue(base64InputStream.hasNextLine());
        assertEquals("VG8gZXhwIHlvdSBzaXplIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWw=", base64InputStream.readLine());
        assertFalse(base64InputStream.hasNextLine());
        assertFalse(base64InputStream.hasNext());
    }

    @Test
    public void testBase64InputStreamDecodeLargeData() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] input = "VG8gZXhwIHlvdSBzaXplIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWw=".getBytes();
        byteArrayOutputStream.write(input);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[] { 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9 };
        byte[] result = new byte[expected.length];
        int len = base64InputStream.read(result);

        assertArrayEquals(expected, result);
        assertEquals(expected.length, len);

        assertFalse(base64InputStream.hasNextLine());
        assertFalse(base64InputStream.hasNext());
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        InputStream input = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] result = base64InputStream.readAllBytes();
        byteArrayOutputStream.write(result);
        String expected = "VG8gZXhwIHlvdSBzaXplIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWw=";
        assertEquals(expected, byteArrayOutputStream.toString());
    }

    @Test
    public void testBase64InputStreamNullInput() {
        InputStream input = null;
        try {
            new Base64InputStream(input);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamNullOutput() {
        InputStream input = new ByteArrayInputStream(DATA);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        try {
            base64InputStream.writeTo(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] result = new byte[1];
        int len = base64InputStream.read(result);

        assertNull(result);
        assertEquals(-1, len);

        assertFalse(base64InputStream.hasNextLine());
        assertFalse(base64InputStream.hasNext());
    }
}