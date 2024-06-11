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
    public void testBase64InputStream() {
        InputStream input = new ByteArrayInputStream("RnJvbSBzYWNrLg==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        assertEquals(0, base64InputStream.read());
        assertEquals('A', (char) base64InputStream.read());
        assertEquals('B', (char) base64InputStream.read());
        assertEquals(65, (int) base64InputStream.read());
        assertEquals(66, (int) base64InputStream.read());
        assertEquals(67, (int) base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytesToEncode = "Hello World!".getBytes();
        byteArrayOutputStream.write(bytesToEncode);
        ByteArrayOutputStream byteArrayOutputStreamEncoded = new ByteArrayOutputStream();
        byte[] lineSeparator = "\n".getBytes();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStreamEncoded, -1, lineSeparator);
        base64OutputStream.write(bytesToEncode);
        base64OutputStream.finish();
        InputStream input = new ByteArrayInputStream(byteArrayOutputStreamEncoded.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] expectedByteArray = "Hello World!".getBytes();
        byte[] resultByteArray = new byte[expectedByteArray.length];
        int len = base64InputStream.read(resultByteArray, 0, expectedByteArray.length);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytesToEncode = "Hello World!".getBytes();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream);
        base64OutputStream.write(bytesToEncode);
        base64OutputStream.finish();
        InputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);
        byte[] expectedByteArray = "RnJvbSBzYWNrLg==".getBytes();
        byte[] resultByteArray = new byte[expectedByteArray.length];
        int len = base64InputStream.read(resultByteArray, 0, expectedByteArray.length);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testBase64InputStreamWithCustomLineLengthAndLineSeparator() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytesToEncode = "Hello World!".getBytes();
        byte[] lineSeparator = "\r\n".getBytes();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 60, lineSeparator);
        base64OutputStream.write(bytesToEncode);
        base64OutputStream.finish();
        InputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 72, lineSeparator);
        byte[] expectedByteArray = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLQ==\r\n".getBytes();
        byte[] resultByteArray = new byte[expectedByteArray.length];
        int len = base64InputStream.read(resultByteArray, 0, expectedByteArray.length);
        assertArrayEquals(expectedByteArray, resultByteArray);
    }

    @Test
    public void testBase64InputStreamWithNullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
        assertNull(base64InputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNegativeLineLength() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("RnJvbSBzYWNrLg==".getBytes()), true, -10, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNegativeLineSeparatorLength() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] lineSeparator = new byte[1];
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 0, lineSeparator);
        base64OutputStream.write("Hello World!".getBytes());
        base64OutputStream.finish();
        InputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 0, lineSeparator);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNullLineSeparator() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteArrayOutputStream, 0, null);
        base64OutputStream.write("Hello World!".getBytes());
        base64OutputStream.finish();
        InputStream input = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 0, null);
    }

    @Test
    public void testBase64InputStreamWithEmptyInput() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        assertEquals(-1, base64InputStream.read());
    }
}