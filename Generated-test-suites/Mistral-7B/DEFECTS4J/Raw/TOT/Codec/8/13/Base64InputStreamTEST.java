package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
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
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a simple Base64 stream with default settings.
     */
    @Test
    public void testDecodeDefault() throws IOException {
        String base64Data = "VG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream input = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, length);
        }
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, output.toByteArray());
    }

    /**
     * Test encoding a simple Base64 stream with default settings.
     */
    @Test
    public void testEncodeDefault() throws IOException {
        String base64Data = "VG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream input = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(input, true);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, length);
        }
        assertEquals(base64Data, output.toString());
    }

    /**
     * Test decoding a Base64 stream with a custom line length and line separator.
     */
    @Test
    public void testDecodeCustom() throws IOException {
        String base64Data = "VG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream input = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64 base64 = new Base64(72, new byte[] { '\r', '\n' });
        Base64InputStream base64InputStream = new Base64InputStream(input, false, base64.getLineLength(), base64.getLineSeparator());
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            output.write(buffer, 0, length);
        }
        assertArrayEquals(new byte[] { 1, 2, 3, 4 }, output.toByteArray());
    }

    /**
     * Test decoding an empty Base64 stream.
     */
    @Test
    public void testDecodeEmpty() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] buffer = new byte[1];
        int length = base64InputStream.read(buffer, 0, buffer.length);
        assertFalse(length > 0);
        assertNull(base64InputStream.base64);
    }

    /**
     * Test decoding a Base64 stream with an invalid Base64 character.
     */
    @Test(expected = IOException.class)
    public void testDecodeInvalid() throws IOException {
        String base64Data = "VG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=x";
        ByteArrayInputStream input = new ByteArrayInputStream(base64Data.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);
    }
}