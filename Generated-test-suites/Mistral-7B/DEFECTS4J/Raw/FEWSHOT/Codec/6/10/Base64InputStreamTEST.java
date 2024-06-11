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

    @Test
    public void testBase64InputStream() {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYW5jZQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertEquals(input, base64InputStream.getInputStream());
        assertFalse(base64InputStream.markSupported());
        assertEquals(Base64InputStream.class, base64InputStream.getClass());
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        byte[] inputData = "RnJvbSBzYW5jZQ==".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] outputData = new byte[inputData.length];
        int len = base64InputStream.read(outputData, 0, outputData.length);
        assertArrayEquals(inputData, outputData);
        assertEquals(inputData.length, len);
    }

    @Test
    public void testBase64InputStreamDecodeWithLineLengthAndLineSeparator() throws IOException {
        byte[] inputData = "--\r\nRnJvbSBzYW5jZQ==\r\n--\r\n".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(input, 72, "\r\n".getBytes());
        byte[] outputData = new byte[inputData.length - 3]; // without "--"
        int len = base64InputStream.read(outputData, 0, outputData.length);
        assertArrayEquals(new byte[] { 0, 0, 1, 2, 3 }, outputData); // first line with CRLF
        assertEquals(5, len);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamDecodeWithNegativeOffset() throws IOException {
        byte[] inputData = "RnJvbSBzYW5jZQ==".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] outputData = new byte[inputData.length];
        int offset = -1;
        base64InputStream.read(outputData, offset, outputData.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamDecodeWithNegativeLen() throws IOException {
        byte[] inputData = "RnJvbSBzYW5jZQ==".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] outputData = new byte[inputData.length];
        int len = -1;
        base64InputStream.read(outputData, 0, len);
    }

    @Test
    public void testBase64InputStreamDecodeWithInvalidLineLength() throws IOException {
        byte[] inputData = "--\r\nRnJvbSBzYW5jZQ==\r\n--\r\n".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(input, 0, "\r\n".getBytes());
        byte[] outputData = new byte[inputData.length - 3]; // without "--"
        int len = base64InputStream.read(outputData, 0, outputData.length);
        assertArrayEquals(new byte[] { 0, 0, 1, 2, 3 }, outputData); // first line with CRLF
        assertEquals(5, len);
    }

    @Test
    public void testBase64InputStreamDecodeWithLineLengthLargerThanMaxBufferSize() throws IOException {
        byte[] inputData = "--\r\n";
        byte[] data = new byte[256 * 1024]; // 256 KB
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ((int) (Math.random() * 128 + 1));
            if (i % 4 == 0) {
                data[i] = (byte) 61; // '='
            }
        }
        InputStream input = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(input, 256 * 1024 + 1, "\r\n".getBytes());
        byte[] outputData = new byte[inputData.length + 2 * (data.length / 4)]; // including "--"
        int len = base64InputStream.read(outputData, 0, outputData.length);
        assertFalse(base64InputStream.hasData());
        assertEquals(len, outputData.length);
        assertArrayEquals(data, outputData, 0, data.length);
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        byte[] inputData = "foo\r\nbar\r\n".getBytes();
        InputStream input = new ByteArrayInputStream(inputData);
        Base64OutputStream base64OutputStream = new Base64OutputStream(new ByteArrayOutputStream());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 72, "\r\n".getBytes());
        int len = base64InputStream.read(base64OutputStream.getBuffer(), 0, base64OutputStream.getBuffer().length);
        assertEquals("VGp1b25lYWxzCiAgICAtLVBhdGggUG9pbnQgUHJvZHVjdGlvbiBmaWxlIHByZWZpeCwNCg==\r\n", base64OutputStream.toString());
        assertEquals(len, base64OutputStream.size());
    }
}