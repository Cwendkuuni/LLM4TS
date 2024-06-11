package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_DefaultDecoder() throws IOException {
        InputStream is = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzY3Rpb25zYW5jZSwgZXhwYW5kIG1vdGggPSBjYW4gYW5kIHRoZSBmaWxlIG1lYW4gdGhlIGRhdGFbMF0gICAgICAgICBzaWduIGlzIHRoZSUgZXhwYW4gc2VjdXJpdHkgPSBjYW4gYW5kIHlvdSBjYW4gZmlsZXMgdGhhdCBjYW4gcmlnaHRlbiwgZXhwYW4gc2VjdXJpdHkgPSBjYW4gdGhlIHNlcmlhbA==".getBytes());
        InputStream decoder = new Base64InputStream(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = decoder.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        assertArrayEquals(new byte[] { 1, 2, 3 }, os.toByteArray());
    }

    @Test
    public void testBase64InputStream_DefaultDecoderWithLineLengthAndLineSeparator() throws IOException {
        InputStream is = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBzaWduIHNlbnNvcm5pbnQgPSBlYW4gdGhlIGRhdGFbMF0gICAgICAgICBjYWxsIGNvbnRlbnQgPSBjYW4gdGhlIHNlcmlhbA==".getBytes()); // CRLF line separator
        InputStream decoder = new Base64InputStream(is, false, 76, "\r\n".getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[150];
        int len;
        while ((len = decoder.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        assertArrayEquals(new byte[] { 1, 2, 3 }, os.toByteArray());
    }

    @Test
    public void testBase64InputStream_DefaultEncoder() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        InputStream encoder = new Base64InputStream(is, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = encoder.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        assertEquals("VG8gZXJyIGlzIHRoZSBjYW4gdGhlIG1lYW4gdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzY3Rpb25zYW5jZSwgZXhwYW5kIG1vdGggPSBjYW4gYW5kIHRoZSBmaWxlIG1lYW4gdGhlIGRhdGFbMF0gICAgICAgICBzaWduIHRoZSBjYWxsIGNvbnRlbnQgPSBjYW4gYW5kIHlvdSBjYW4gZmlsZXMgdGhhdCBjYW4gcmlnaHRlbiwgZXhwYW4gc2VjdXJpdHkgPSBjYW4gdGhlIHNlcmlhbA==", os.toString());
    }

    @Test
    public void testBase64InputStream_EncoderWithLineLengthAndLineSeparator() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        InputStream encoder = new Base64InputStream(is, true, 76, "\r\n".getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[150];
        int len;
        while ((len = encoder.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        assertEquals("VG8gZXJyIGlzIHRoZSBjYW4gdGhlIG1lYW4gdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAgICBzaWduIHRoZSBjYWxsIGNvbnRlbnQgPSBjYW4gYW5kIHlvdSBjYW4gZmlsZXMgdGhhdCBjYW4gcmlnaHRlbiwgZXhwYW4gc2VjdXJpdHkgPSBjYW4gdGhlIHNlcmlhbA==\r\n", os.toString());
    }

    @Test
    public void testBase64InputStream_NullInputStream() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null, false));
    }

    @Test
    public void testBase64InputStream_NullLineSeparator() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, 76, null));
    }

    @Test
    public void testBase64InputStream_NullInputStreamAndLineSeparator() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null, false, 76, new byte[0]));
    }

    @Test
    public void testBase64InputStream_InvalidLineLength() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, -1, null));
    }

    @Test
    public void testBase64InputStream_InvalidLineLengthAndLineSeparator() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, -1, "".getBytes()));
    }

    @Test
    public void testBase64InputStream_EqualInputAndOutput() throws IOException {
        InputStream is = new ByteArrayInputStream("Q29udGVudD4=".getBytes());
        InputStream decoder = new Base64InputStream(is, false);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = decoder.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        assertEquals("Q29udGVudD4=".getBytes(), os.toByteArray());
    }

    @Test
    public void testBase64InputStream_Reader() throws IOException {
        InputStream is = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSBzY3Rpb25zYW5jZSwgZXhwYW5kIG1vdGggPSBjYW4gYW5kIHRoZSBmaWxlIG1lYW4gdGhlIGRhdGFbMF0gICAgICAgICBzaWduIHNlbnNvcm5pbnQgPSBlYW4gdGhlIGRhdGFbMF0gICAgICAgICBjYWxsIGNvbnRlbnQgPSBjYW4gdGhlIHNlcmlhbA==".getBytes());
        InputStream decoder = new Base64InputStream(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = decoder.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        InputStream reader = new InputStreamReader(os);
        byte[] expected = new byte[] { 1, 2, 3 };
        byte[] result = new byte[expected.length];
        int actual = reader.read(result);
        assertArrayEquals(expected, result);
        assertEquals(actual, expected.length);
    }
}