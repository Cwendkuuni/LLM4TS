package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Tests for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Tests decoding of Base64 data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testDecodeBase64() throws IOException {
        String base64 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        assertNull(base64InputStream.markSupported());
        assertArrayEquals(new byte[] { 32, 101, 108, 108, 111, 32, 56, 51, 56, 51 }, outputStream.toByteArray());
    }

    /**
     * Tests encoding of Base64 data.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeBase64() throws IOException {
        byte[] data = new byte[] { 32, 101, 108, 108, 111 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, len);
        }
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=",
                outputStream.toString("US-ASCII"));
    }

    /**
     * Tests decoding of Base64 data with custom line length and line separator.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testDecodeBase64Custom() throws IOException {
        String base64 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        byte[] lineSeparator = new byte[] { 13, 10 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(base64.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64(72, lineSeparator);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 72, lineSeparator);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        assertNull(base64InputStream.markSupported());
        assertArrayEquals(
                new byte[] { 32, 101, 108, 108, 111, 13, 10, 56, 51, 56, 51, 13, 10 }, outputStream.toByteArray());
    }

    /**
     * Tests encoding of Base64 data with custom line length and line separator.
     *
     * @throws IOException
     *             if an I/O error occurs
     */
    @org.junit.Test
    public void testEncodeBase64Custom() throws IOException {
        byte[] data = new byte[] { 32, 101, 108, 108, 111 };
        byte[] lineSeparator = new byte[] { 13, 10 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64 base64 = new Base64(72, lineSeparator);
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, lineSeparator);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            base64OutputStream.write(buffer, 0, len);
        }
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=",
                outputStream.toString("US-ASCII"));
    }
}