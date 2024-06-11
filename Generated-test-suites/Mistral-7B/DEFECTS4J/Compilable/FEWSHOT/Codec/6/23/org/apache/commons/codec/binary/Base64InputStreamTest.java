package org.apache.commons.codec.binary;

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
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBvZiBvdGhlciBmb3Jt".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        InputStream originalInputStream = base64InputStream;

        assertEquals(inputStream.getClass(), originalInputStream.getClass());
        assertFalse(base64InputStream.markSupported());
        assertTrue(base64InputStream instanceof Base64InputStream);
        assertSame(base64InputStream, base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        byte[] inputData = "RnJvbSBvZiBvdGhlciBmb3Jt".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read;
        byte[] buffer = new byte[1024];

        while ((read = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        byte[] expectedOutput = "Hello world!".getBytes();
        byte[] output = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, output);
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        byte[] inputData = "Hello world!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int read;
        byte[] buffer = new byte[1024];

        while ((read = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        String expectedOutput = "SGVsbG8gV29ybGQh";
        String output = new String(outputStream.toByteArray());

        assertEquals(expectedOutput, output);
    }

    @Test
    public void testBase64InputStreamDecodeWithCustomLineLengthAndLineSeparator() throws IOException {
        byte[] inputData = "RnJvbSBvZiBvdGhlciBmb3Jt\r\n".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, "\r\n".getBytes());
        int read;
        byte[] buffer = new byte[1024];

        while ((read = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, read);
        }

        byte[] expectedOutput = "Hello world!\r\n".getBytes();
        byte[] output = outputStream.toByteArray();

        assertArrayEquals(expectedOutput, output);
    }

    private void assertArrayEquals(byte[] expected, byte[] actual) {
        if (null == expected || null == actual) {
            assertEquals(expected, actual);
        } else {
            assertEquals(expected.length, actual.length);
            for (int i = 0; i < expected.length; i++) {
                assertEquals(expected[i], actual[i]);
            }
        }
    }

}