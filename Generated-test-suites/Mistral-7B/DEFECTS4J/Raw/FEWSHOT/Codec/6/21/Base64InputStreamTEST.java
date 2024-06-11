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
    public void testBase64InputStreamDefaultConstructor() {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBmcm9tIHVzZSB0aGUtcHJvZHVjdHJpYyBkYXRh".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] byteArray = new byte[10];
        int length = base64InputStream.read(byteArray, 0, 10);
        assertArrayEquals(new byte[] { 72, 101, 118, 101, 114, 101, 116, 105, 111, 110 }, byteArray, 0, length);
        assertEquals(10, length);
    }

    @Test
    public void testBase64InputStreamWithEncodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBmcm9tIHVzZSB0aGUtcHJvZHVjdHJpYyBkYXRh".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] byteArray = new byte[10];

        int length = 0;
        while ((length = base64InputStream.read(byteArray, 0, byteArray.length)) > 0) {
            outputStream.write(byteArray, 0, length);
        }

        assertArrayEquals("SGVsbG8sIFdvcmxkIQ==".getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamWithEncodeAndLineLengthConstructor() {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBmcm9tIHVzZSB0aGUtcHJvZHVjdHJpYyBkYXRh".getBytes());
        byte[] lineSeparator = new byte[] { 13, 10 };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, lineSeparator);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] byteArray = new byte[76];

        int length = 0;
        while ((length = base64InputStream.read(byteArray, 0, byteArray.length)) > 0) {
            outputStream.write(byteArray, 0, length);
            outputStream.write(lineSeparator, 0, lineSeparator.length);
        }

        assertArrayEquals(("SGVsbG8sIFdvcmxkIQ==\r\n").getBytes(), outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamWithDecodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream("SGVsbG8sIFdvcmxkIQ==\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] byteArray = new byte[10];
        int length = base64InputStream.read(byteArray, 0, 10);
        assertArrayEquals(new byte[] { 72, 101, 118, 101, 114, 101, 116, 105, 111, 110 }, byteArray, 0, length);
        assertEquals(10, length);
    }

    @Test
    public void testBase64InputStreamWithDecodeAndLineLengthConstructor() {
        InputStream inputStream = new ByteArrayInputStream("SGVsbG8sIFdvcmxkIQ==\r\n".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, null);

        byte[] byteArray = new byte[76];
        int length = 0;
        while ((length = base64InputStream.read(byteArray, 0, byteArray.length)) > 0) {
            // Base64InputStream always returns the exact number of decoded bytes
            assertEquals(length, base64InputStream.read(byteArray, 0, byteArray.length));
        }

        assertArrayEquals(new byte[] { 72, 101, 118, 101, 114, 101, 116, 105, 111, 110 }, byteArray, 0, length);
    }

    @Test
    public void testBase64InputStreamWithNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
        assertNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamWithNegativeLineLength() {
        InputStream inputStream = new ByteArrayInputStream("RnJvbSBmcm9tIHVzZSB0aGUtcHJvZHVjdHJpYyBkYXRh".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, -1, null);
        assertFalse(base64InputStream instanceof Base64InputStream);
    }
}