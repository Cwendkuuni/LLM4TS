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
    public void testCreateBase64InputStreamDecodingByDefault() {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expectedBytes = "Hello World".getBytes();
        byte[] readBytes = new byte[expectedBytes.length];

        int bytesRead = base64InputStream.read(readBytes, 0, expectedBytes.length);
        assertEquals(expectedBytes.length, bytesRead);
        assertArrayEquals(expectedBytes, readBytes);

        // Verify that the original input stream has been consumed
        InputStream originalInput = base64InputStream.getInputStream();
        byte[] origBytes = new byte[1];
        int bytesReadFromOriginal = originalInput.read(origBytes, 0, 1);
        assertEquals(-1, bytesReadFromOriginal);
    }

    @Test
    public void testCreateBase64InputStreamEncoding() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);
        Base64 base64 = new Base64();

        byte[] encodedBytes = new byte[base64.getEncodedLength("Hello World")];
        int bytesRead = base64InputStream.read(encodedBytes, 0, encodedBytes.length);
        assertEquals(encodedBytes.length, bytesRead);

        byte[] decodedBytes = base64.decode(encodedBytes);
        assertArrayEquals("Hello World".getBytes(), decodedBytes);
        assertArrayEquals(encodedBytes, base64InputStream.getInputStream().readAllBytes());
    }

    @Test
    public void testCreateBase64InputStreamCustomLineLengthAndLineSeparator() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World\r\n".getBytes());
        Base64 base64 = new Base64(72, "\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 72, "\r\n".getBytes());

        byte[] encodedBytes = new byte[base64.getEncodedLength("Hello World")];
        int bytesRead = base64InputStream.read(encodedBytes, 0, encodedBytes.length);
        assertEquals(encodedBytes.length, bytesRead);

        byte[] decodedBytes = base64.decode(encodedBytes, 0, encodedBytes.length);
        assertArrayEquals("Hello World".getBytes(), decodedBytes);

        String expectedEncodedString = "VG8gZXJyIGZvciBmaWxlIGxpbmsuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IGZvdWwgdG8gcmVhbGx5IGZvdWw=";
        assertEquals(expectedEncodedString, new String(encodedBytes));
    }

    @Test
    public void testCreateBase64InputStreamCustomLineLengthAndLineSeparatorWithNoLineBreaks() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        Base64 base64 = new Base64(72, null);
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 72, null);

        byte[] encodedBytes = new byte[base64.getEncodedLength("Hello World")];
        int bytesRead = base64InputStream.read(encodedBytes, 0, encodedBytes.length);
        assertEquals(encodedBytes.length, bytesRead);

        byte[] decodedBytes = base64.decode(encodedBytes, 0, encodedBytes.length);
        assertArrayEquals("Hello World".getBytes(), decodedBytes);

        String expectedEncodedString = "VG8gZXJyIGZvciBmaWxlIGxpbmsuCiAgICAtLQ==";
        assertEquals(expectedEncodedString, new String(encodedBytes));
    }

    @Test
    public void testCreateBase64InputStreamCustomLineLengthAndLineSeparatorWithInvalidLineLength() {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        try {
            new Base64InputStream(input, true, -1, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateBase64InputStreamCustomLineLengthAndLineSeparatorWithInvalidLineSeparator() {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        try {
            new Base64InputStream(input, true, 72, new byte[] { 0x01, 0x02 });
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateBase64InputStreamWithNullInputStream() {
        try {
            new Base64InputStream(null, false);
            fail();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateBase64InputStreamWithNullBase64() {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        try {
            new Base64InputStream(input, false, 72, null, null);
            fail();
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testCreateBase64InputStreamWithNegativeDoEncode() {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        try {
            new Base64InputStream(input, false);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadSingleByte() throws IOException {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        assertEquals('H', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('o', base64InputStream.read());
        assertEquals(' ', base64InputStream.read());
        assertEquals('W', base64InputStream.read());
        assertEquals('r', base64InputStream.read());
        assertEquals('d', base64InputStream.read());
        assertEquals(' ', base64InputStream.read());
        assertEquals('!', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadEmptyInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(10, bytesRead);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals('o', buffer[4]);
        assertEquals(' ', buffer[5]);
        assertEquals('W', buffer[6]);
        assertEquals('r', buffer[7]);
        assertEquals('d', buffer[8]);
        assertEquals('!', buffer[9]);
    }

    @Test
    public void testReadMultipleBytesWithPartialFill() throws IOException {
        InputStream input = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(5, bytesRead);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('l', buffer[2]);
        assertEquals('l', buffer[3]);
        assertEquals(' ', buffer[4]);

        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(5, bytesRead);
        assertEquals('W', buffer[0]);
        assertEquals('r', buffer[1]);
        assertEquals('d', buffer[2]);
        assertEquals(' ', buffer[3]);
        assertEquals('!', buffer[4]);

        bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        assertFalse(base64InputStream.markSupported());
    }
}