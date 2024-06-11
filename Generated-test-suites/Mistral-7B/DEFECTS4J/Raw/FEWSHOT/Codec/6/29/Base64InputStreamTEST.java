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

    private InputStream createTestInputStream() {
        byte[] sourceData = "The quick brown fox jumps over the lazy dog".getBytes();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(sourceData);
        return testInputStream;
    }

    @Test
    public void testBase64InputStream() {
        InputStream inputStream = createTestInputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream); // default decoding
        assertEquals(inputStream, base64InputStream.getInputStream());
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStreamWithEncoding() {
        InputStream inputStream = createTestInputStream();
        Base64 base64 = new Base64(true);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, new byte[] { '\r', '\n' });
        assertEquals(base64, base64InputStream.getBase64());
        assertEquals(inputStream, base64InputStream.getInputStream());
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStreamWithDecoding() {
        InputStream inputStream = createTestInputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        Base64 base64 = new Base64(false, false);
        byte[] expectedOutput = new byte[] { 72, 101, 108, 105, 103, 104, 111, 114, 105, 112, 116, 104, 101, 114, 101, 116,
                32, 111, 110, 103, 101, 108, 101, 115, 116, 116, 111, 119, 111, 109, 105, 110, 32, 108, 101, 116, 116,
                111, 111, 111, 111, 108, 105, 103, 46 };
        byte[] inputBuffer = new byte[1024];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int bytesRead;
        while ((bytesRead = base64InputStream.read(inputBuffer, 0, inputBuffer.length)) > 0) {
            outputStream.write(inputBuffer, 0, bytesRead);
        }

        byte[] result = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, result);
    }

    @Test
    public void testBase64InputStreamWithDecodingReturnsMinusOneAtEndOfStream() throws IOException {
        InputStream inputStream = createTestInputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] inputBuffer = new byte[1];
        int bytesRead = base64InputStream.read(inputBuffer, 0, 1);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testBase64InputStreamWithEmptyData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertEquals(0, base64InputStream.read(new byte[1], 0, 1));
        assertEquals(-1, base64InputStream.read(new byte[1], 0, 1));
    }

    @Test
    public void testBase64InputStreamWithNullData() {
        InputStream inputStream = null;
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        assertNull(base64InputStream.getInputStream());
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamWithNullBase64() {
        InputStream inputStream = createTestInputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNegativeLineLength() {
        InputStream inputStream = createTestInputStream();
        Base64 base64 = new Base64(true);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, new byte[] { '\r', '\n' });
    }
}