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

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_defaultConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[] { 0x41, 0x42, 0x43 };
        byte[] result = new byte[3];
        assertEquals(3, base64InputStream.read(result));
        assertArrayEquals(expected, result);

        assertTrue(base64InputStream.markSupported()); // test for inheritDoc
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStream_decodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] expected = new byte[] { 0x41, 0x42, 0x43 };
        byte[] result = new byte[3];
        assertEquals(3, base64InputStream.read(result));
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64InputStream_encodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        do {
            len = base64InputStream.read(buffer);
            if (len > 0) {
                outputStream.write(buffer, 0, len);
            }
        } while (len > 0);

        String encodedString = outputStream.toString();
        String expected = "VGpzdGVtcyBpcyBhIGlzIGRvd25sb2FkIGZvdWwgdG8=";
        assertEquals(expected, encodedString);
    }

    @Test
    public void testBase64InputStream_decodeConstructor_withLineLength() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x41, 0x42, 0x43 }); // "ABCABC"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 2, new byte[] { 0x0D, 0x0A });

        byte[] expected = new byte[] { 0x41, 0x42, 0x43, 0x41, 0x42, 0x43 };
        byte[] result = new byte[6];
        int read = base64InputStream.read(result);

        assertEquals(6, read);
        assertArrayEquals(expected, result);

        byte[] lastBytes = new byte[2];
        int lastRead = base64InputStream.read(lastBytes);
        assertEquals(2, lastRead);
        assertArrayEquals(new byte[] { 0x0D, 0x0A }, lastBytes);
    }

    @Test
    public void testBase64InputStream_encodeConstructor_withLineLength() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 }); // "ABC"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, new byte[] { 0x0D, 0x0A });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        do {
            len = base64InputStream.read(buffer);
            if (len > 0) {
                outputStream.write(buffer, 0, len);
            }
        } while (len > 0);

        String encodedString = outputStream.toString();
        String expected = "VGpzdGVtcyBpcyBhIGlzIGRvd25sb2FkIGZvdWwgdG8="; // base64 encoding of "ABC" with default line length
        String expectedWithLineLength = "VGpzdGVtcyBpcyBhIGlzIGRvd25sb2FkIGZvdWwgdG8=\r\n"; // base64 encoding of "ABC" with line length 72
        assertEquals(expectedWithLineLength, encodedString);
    }

    @Test
    public void testBase64InputStream_decodeConstructor_withInvalidInput() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41 }); // "A"
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertNull(base64InputStream.read(new byte[1]));
    }

    @Test
    public void testBase64InputStream_decodeConstructor_withNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
        assertNull(base64InputStream);
    }
}