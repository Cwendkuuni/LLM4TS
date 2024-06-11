package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class Base64InputStreamTest {

    private final static byte[] TEST_DATA = "abc\ndef\nghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/".getBytes(StandardCharsets.US_ASCII);
    private final static String TEST_DATA_BASE64 = "VG8gZXhwYWNrIFdvcmxkIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBzdWQgTmFtZT4=";

    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[TEST_DATA.length];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(TEST_DATA.length, read);
        assertArrayEquals(TEST_DATA, buffer);
    }

    @Test
    public void testEncodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4];
        while (base64InputStream.read(buffer, 0, buffer.length) != -1) {
            outputStream.write(buffer, 0, buffer.length);
        }

        String result = outputStream.toString(StandardCharsets.US_ASCII.name());
        assertEquals(TEST_DATA_BASE64, result);
    }

    @Test
    public void testDecodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA_BASE64.getBytes(StandardCharsets.US_ASCII));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] buffer = new byte[TEST_DATA.length];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(TEST_DATA.length, read);
        assertArrayEquals(TEST_DATA, buffer);
    }

    @Test
    public void testEncodeConstructorWithLineLengthAndSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.clone());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, "\n".getBytes(StandardCharsets.US_ASCII));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[50];
        while (base64InputStream.read(buffer, 0, buffer.length) != -1) {
            outputStream.write(buffer, 0, buffer.length);
            if (base64InputStream.available() > 0) {
                outputStream.write("\n".getBytes(StandardCharsets.US_ASCII));
            }
        }

        String result = outputStream.toString(StandardCharsets.US_ASCII.name());
        String expected = "VG8gZXhwYWNrIHdvcmxkIGEgY29tcHV0ZXIuCiAgICAtLSBzdWQgTmFtZT4=\n" +
                "VGhlIGEgY29tcHV0ZXIgZW5jb2RlIGluIGlzIG5vdCBkZWZpY2lzLCBmaWxlIGluIGlzIHByZWZpeCw=";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeBufferOffset() {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeBufferLength() {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBufferTooSmall() throws IOException {
        byte[] buffer = new byte[2];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        base64InputStream.read(buffer, 0, buffer.length);
        base64InputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testNegativeBytesRead() throws IOException {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        base64InputStream.read(buffer, 0, -5);
    }

    @Test
    public void testMarkUnsupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA), false);
        assertNull(base64InputStream.markSupported());
    }
}