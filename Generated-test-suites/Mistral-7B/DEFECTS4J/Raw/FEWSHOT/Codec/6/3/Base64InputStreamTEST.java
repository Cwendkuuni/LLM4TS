package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private static final String TEST_BASE64_ENCODED = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdWwgQXJyYXkgb2YgZnV0IGluIG15IHByZWZpeC4=";
    private static final String TEST_BASE64_DECODED = "Mime mail with attachment\r\n--1234567890\r\nContent-Disposition: attachment; filename=\"test.txt\"\r\nContent-Type: text/plain\r\n\r\nTest Content\r\n--1234567890--";

    @Test
    public void testBase64InputStreamDefaultDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_BASE64_ENCODED.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            // check decoded data
            byte[] decodedData = new byte[bytesRead];
            base64InputStream.read(decodedData, 0, bytesRead);
            assertEquals(TEST_BASE64_DECODED.substring(0, bytesRead), new String(decodedData, 0, bytesRead));
        }

        // check that end of stream has been reached
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamDecodingWithLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_BASE64_ENCODED.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, "\r\n".getBytes());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            // check decoded data
            byte[] decodedData = new byte[bytesRead];
            base64InputStream.read(decodedData, 0, bytesRead);
            assertEquals(TEST_BASE64_DECODED.substring(0, bytesRead), new String(decodedData, 0, bytesRead));
        }

        // check that end of stream has been reached
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        String testString = "Mime mail with attachment";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, false);
        base64OutputStream.write(testString.getBytes());
        base64OutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            // check encoded data
            byte[] encodedData = new byte[bytesRead];
            base64InputStream.read(encodedData, 0, bytesRead);
            assertEquals(TEST_BASE64_ENCODED.substring(0, bytesRead), new String(encodedData, 0, bytesRead));
        }

        // check that end of stream has been reached
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamEncodingWithLineLength() throws IOException {
        String testString = "Mime mail with attachment";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 76, "\r\n".getBytes());
        base64OutputStream.write(testString.getBytes());
        base64OutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            // check encoded data
            byte[] encodedData = new byte[bytesRead];
            base64InputStream.read(encodedData, 0, bytesRead);
            assertEquals(TEST_BASE64_ENCODED.substring(0, bytesRead), new String(encodedData, 0, bytesRead));
        }

        // check that end of stream has been reached
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamNullInputStream() {
        InputStream inputStream = null;
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamNullDoEncode() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, null);
        assertFalse(base64InputStream.isEncode());
    }

    @Test
    public void testBase64InputStreamNegativeLineLength() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        try {
            Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, "\r\n".getBytes());
            assertFalse("Expected IllegalArgumentException", false);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}