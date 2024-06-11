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
    public void testBase64InputStreamDefaultConstructor() {
        InputStream input = new ByteArrayInputStream("VG8gZXhhbXBsZSBmaWxl".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] testByteArray = new byte[4];

        int length = base64InputStream.read(testByteArray, 0, testByteArray.length);
        assertEquals(3, length);
        assertArrayEquals(new byte[] { 72, -68, -65 }, testByteArray);

        length = base64InputStream.read(testByteArray, 0, testByteArray.length);
        assertEquals(1, length);
        assertArrayEquals(new byte[] { 67 }, testByteArray);

        length = base64InputStream.read(testByteArray, 0, testByteArray.length);
        assertEquals(-1, length);
    }

    @Test
    public void testBase64InputStreamBooleanConstructor() {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64InputStream.writeTo(outputStream);

        assertEquals("QmFzZTY0IGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbnQtdGhpbmdzIHlvdSBuZWVkIG9mIHRoZSBmaWxlIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbnQtdGhpbmdzIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIG9mIHRoZSBmaWxlIG5vdCBmaWxlLCB3ZSBhbmQgaXQgc29tZSBpbiB0aGUxJmF0b20=".length(), outputStream.size());
        assertArrayEquals("QmFzZTY0IGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbnQtdGhpbmdzIHlvdSBuZWVkIG9mIHRoZSBmaWxlIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbnQtdGhpbmdzIGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIG9mIHRoZSBmaWxlIG5vdCBmaWxlLCB3ZSBhbmQgaXQgc29tZSBpbiB0aGUxJmF0b20=".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamBooleanConstructorWithLineLengthAndLineSeparator() {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        base64InputStream.writeTo(outputStream);

        assertEquals("VG8gZXhhbXBsZSBmaWxl\r\n", new String(outputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStreamNullConstructor() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
        assertNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamBooleanConstructorWithNullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
        assertNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamBooleanConstructorWithNegativeLength() {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        try {
            new Base64InputStream(inputStream, true, -1, null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid line length: -1", e.getMessage());
        }
    }

    @Test
    public void testBase64InputStreamBooleanConstructorWithNullLineSeparator() {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        try {
            new Base64InputStream(inputStream, true, 76, null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("Line separator is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testBase64InputStreamWriteTo() throws IOException {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        base64InputStream.writeTo(outputStream);

        assertEquals("VG8gZXhhbXBsZSBmaWxl\r\n", new String(outputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStreamWriteToWithLineLength() throws IOException {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 48);

        base64InputStream.writeTo(outputStream);

        assertEquals("VG8gZXhhbXBsZSBmaWxl\r\nVG", new String(outputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStreamWriteToWithLineSeparator() throws IOException {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n\r\n".getBytes());

        base64InputStream.writeTo(outputStream);

        assertEquals("VG8gZXhhbXBsZSBmaWxl\r\n\r\n", new String(outputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStreamWriteToWithDefaultLineLengthAndDefaultLineSeparator() throws IOException {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        base64InputStream.writeTo(outputStream);

        assertEquals("VG8gZXhhbXBsZSBmaWxl\r\n", new String(outputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStreamWriteToWithLineLengthZero() throws IOException {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 0, null);

        base64InputStream.writeTo(outputStream);

        assertEquals("VG8gZXhhbXBsZSBmaWxl", new String(outputStream.toByteArray()));
    }

    @Test
    public void testBase64InputStreamWriteToWithNullOutput() {
        byte[] input = "VG8gZXhhbXBsZSBmaWxl".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);

        try {
            new Base64InputStream(inputStream, true).writeTo(null);
            fail();
        } catch (NullPointerException e) {
            assertEquals("Output stream is marked non-null but is null", e.getMessage());
        }
    }

    @Test
    public void testBase64InputStreamMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);

        assertFalse(base64InputStream.markSupported());
    }
}