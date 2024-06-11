package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBmaWxlIGlzIG5vdCB5b3VyIGZvdWwgdG8gcmVhbGx5IGZvdWwgYW5kIGEgY29tcHV0ZXIuCiAgICAtLSIgYW5kIGlzIG5vdCBpbmtzY2FwZT4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBmaWxlIGlzIG5vdCB5b3VyIGZvdWwgdG8gcmVhbGx5IGZvdWwgYW5kIGEgY29tcHV0ZXIuCiAgICAtLSIgYW5kIGlzIG5vdCBpbmtzY2FwZT4=".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamWithLineLengthAndLineSeparator() throws IOException {
        String inputString = "--Base64\r\nVG8gZXJyIGlzIGh1bWFuLCBmaWxlIGlzIG5vdCB5b3VyIGZvdWwgdG8gcmVhbGx5IGZvdWwgYW5kIGEgY29tcHV0ZXIuCiAgICAtLSIgYW5kIGlzIG5vdCBpbmtzY2FwZT4=\r\n--Base64--";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamWithNullInput() {
        InputStream inputStream = null;
        try {
            new Base64InputStream(inputStream);
            fail("Expected IllegalArgumentException when inputStream is null.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithNegativeLineLength() {
        try {
            new Base64InputStream(new ByteArrayInputStream("test".getBytes()), true, -1, null);
            fail("Expected IllegalArgumentException when lineLength is negative.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithNullLineSeparator() {
        try {
            new Base64InputStream(new ByteArrayInputStream("test".getBytes()), true, 76, null);
            // Expected no exception
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBase64InputStreamWithInvalidLineSeparatorLength() {
        try {
            new Base64InputStream(new ByteArrayInputStream("test".getBytes()), true, 76, new byte[0]);
            fail("Expected IllegalArgumentException when lineSeparator is empty or has a length less than 2.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Base64InputStream(new ByteArrayInputStream("test".getBytes()), true, 76, new byte[3]);
            fail("Expected IllegalArgumentException when lineSeparator has a length greater than 2.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithLineLengthZero() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBmaWxlIGlzIG5vdCB5b3VyIGZvdWwgdG8gcmVhbGx5IGZvdWwgYW5kIGEgY29tcHV0ZXIuCiAgICAtLSIgYW5kIGlzIG5vdCBpbmtzY2FwZT4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            // Do nothing
        }
        // Expected EOF
        assertNull(base64InputStream.read(buffer));
    }

    @Test
    public void testBase64InputStreamWithInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("InvalidData".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            // Do nothing
        }
        assertNull(base64InputStream.read(buffer));
    }
}