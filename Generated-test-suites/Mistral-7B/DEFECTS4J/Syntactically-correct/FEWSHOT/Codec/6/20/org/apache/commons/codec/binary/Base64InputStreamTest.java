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
    public void testCreateInstance() {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream1 = new Base64InputStream(in);
        Base64InputStream inputStream2 = new Base64InputStream(in, true);
        Base64InputStream inputStream3 = new Base64InputStream(in, true, 76, "\r\n".getBytes());

        assertEquals(false, inputStream1.doEncode);
        assertEquals(true, inputStream2.doEncode);
        assertEquals(true, inputStream3.doEncode);
        assertEquals(76, inputStream3.base64.lineLength);
        assertArrayEquals("\r\n".getBytes(), inputStream3.base64.lineSeparator);
    }

    @Test
    public void testReadOneByte() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in);

        byte[] expected = new byte[1];
        int actual;

        // Testing decoding:
        actual = inputStream.read();
        assertEquals(expected[0], actual);
        assertEquals((byte) 'A', expected[0]);

        actual = inputStream.read();
        assertEquals(expected[1], actual);
        assertEquals((byte) 'B', expected[1]);

        // Testing end of stream:
        actual = inputStream.read();
        assertEquals(-1, actual);

        // Testing encoding:
        inputStream = new Base64InputStream(in, true);
        byte[] decoded = new byte[7];
        inputStream.read(decoded, 0, 7);
        byte[] encoded = new byte[10];
        inputStream.read(encoded, 0, 10);

        String decodedString = new String(decoded, 0, decoded.length);
        String encodedString = new String(encoded, 0, encoded.length);

        assertEquals("ABCDEFG", decodedString);
        assertEquals("VGpXZXN0IGlzIGh1bWFuLCB3aGljaCBpcyBpcyBicmVha2luZw==", encodedString);
    }

    @Test
    public void testReadBytes() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in);

        byte[] expected = new byte[3];
        int actual;

        // Testing decoding:
        actual = inputStream.read(expected, 0, 3);
        assertEquals(3, actual);
        assertArrayEquals(new byte[] { 'A', 'B', 'C' }, expected);

        // Testing end of stream:
        actual = inputStream.read(expected, 0, 3);
        assertEquals(-1, actual);

        // Testing encoding:
        inputStream = new Base64InputStream(in, true);
        byte[] decoded = new byte[7];
        inputStream.read(decoded, 0, 7);
        byte[] encoded = new byte[10];
        inputStream.read(encoded, 0, 10);

        String decodedString = new String(decoded, 0, decoded.length);
        String encodedString = new String(encoded, 0, encoded.length);

        assertEquals("ABCDEFG", decodedString);
        assertEquals("VGpXZXN0IGlzIGh1bWFuLCB3aGljaCBpcyBpcyBicmVha2luZw==", encodedString);

        byte[] expectedEncoded = new byte[3];
        actual = inputStream.read(expectedEncoded, 0, 3);
        assertEquals(3, actual);
        assertArrayEquals(new byte[] { 'V', 'G', 'p' }, expectedEncoded);
    }

    @Test
    public void testMarkSupported() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in);

        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testNegativeArguments() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in);

        byte[] expected = new byte[1];
        int actual;

        // Testing negative offset:
        actual = inputStream.read(expected, -1, 1);
        assertEquals(-1, actual);

        // Testing negative length:
        actual = inputStream.read(expected, 0, -1);
        assertEquals(-1, actual);

        // Testing negative buffer size:
        actual = inputStream.read(null, -1, -1);
        assertEquals(-1, actual);
    }

    @Test
    public void testNullArguments() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in);

        byte[] expected = new byte[1];
        int actual;

        // Testing null byte array:
        actual = inputStream.read(null, 0, 1);
        assertNull(actual);
    }

    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream inputStream = new Base64InputStream(in);

        byte[] expected = new byte[1];
        int actual = inputStream.read(expected, 0, 1);

        assertEquals(-1, actual);
    }

    @Test
    public void testBase64InputStreamWithLineLengthAndLineSeparator() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in, true, 48, "LINE_SEPARATOR".getBytes());

        byte[] expected = new byte[16];
        int actual = inputStream.read(expected, 0, 16);
        assertArrayEquals(new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', '\r', '\n', 'A', 'B', 'C', 'D', 'E', 'F', 'G', '\r', '\n' }, expected);
    }

    @Test
    public void testBase64InputStreamWithEmptyLineSeparator() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in, true, 48, new byte[0]);

        byte[] expected = new byte[16];
        int actual = inputStream.read(expected, 0, 16);
        assertArrayEquals(new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', '\r', '\n', 'A', 'B', 'C', 'D', 'E', 'F', 'G', '\r', '\n' }, expected);
    }

    @Test
    public void testBase64InputStreamWithCustomBase64() throws IOException {
        InputStream in = new ByteArrayInputStream("ABCDEFG".getBytes());
        Base64 customBase64 = new Base64(32, "LINE_SEPARATOR".getBytes());
        Base64InputStream inputStream = new Base64InputStream(in, true, 48, customBase64);

        byte[] expected = new byte[16];
        int actual = inputStream.read(expected, 0, 16);
        assertArrayEquals(new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', '\r', '\n', 'A', 'B', 'C', 'D', 'E', 'F', 'G', '\r', '\n' }, expected);
    }
}