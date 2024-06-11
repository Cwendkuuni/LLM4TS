package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream() {
        InputStream input = new ByteArrayInputStream("Rm9udCAoKTs=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] b = new byte[2];
        int len;

        // test read method
        len = base64InputStream.read(b, 0, b.length);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32 }, b);

        len = base64InputStream.read(b, 0, b.length);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 101 }, b);

        len = base64InputStream.read(b, 0, b.length);
        assertEquals(len, -1);

        // test read method with buffer larger than data
        len = base64InputStream.read(b, 0, b.length * 2);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32, 101, 101 }, b);

        // test read method with buffer smaller than data
        len = base64InputStream.read(b, 0, b.length - 1);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 32 }, b);

        // test read method with buffer equal to data
        len = base64InputStream.read(b, 0, b.length);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32 }, b);

        // test read method with buffer larger than data and EOF
        len = base64InputStream.read(b, 0, b.length * 2);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32, 101, 101 }, b);

        len = base64InputStream.read(b, 0, b.length * 2);
        assertEquals(len, -1);
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        String base64Data = "Rm9udCAoKTs=";
        InputStream input = new ByteArrayInputStream(base64Data.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        byte[] buffer = new byte[3];
        int len;

        // test read method
        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 101 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, -1);

        // test read method with buffer larger than data
        len = base64InputStream.read(buffer, 0, buffer.length * 2);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32, 101, 101 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length * 2);
        assertEquals(len, -1);

        // test read method with buffer smaller than data
        len = base64InputStream.read(buffer, 0, buffer.length - 1);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 32 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32 }, buffer);

        // test read method with buffer larger than data and EOF
        len = base64InputStream.read(buffer, 0, buffer.length * 2);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 32, 32, 101, 101 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length * 2);
        assertEquals(len, -1);

        // test reading all data
        byte[] result = new byte[3];
        while (len > 0) {
            len = base64InputStream.read(result, 0, result.length);
            if (len > 0) {
                outputStream.write(result, 0, len);
            }
        }

        byte[] expected = new byte[] { 32, 32, 101, 101 };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String inputData = "abc";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] buffer = new byte[4];
        int len;

        // test read method
        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 61 }, buffer); // '=' is padding

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 61 }, buffer); // '=' is padding

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 1);
        assertArrayEquals(new byte[] { 61 }, buffer); // '=' is padding

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, -1);

        byte[] result = outputStream.toByteArray();
        byte[] expected = new byte[] { 65, 65, 65, 61, 63, 63, 63, 61, 61, 61 };
        assertArrayEquals(expected, result);

        // test reading all data
        while (len > 0) {
            len = base64InputStream.read(buffer, 0, buffer.length);
            if (len > 0) {
                outputStream.write(buffer, 0, len);
            }
        }

        expected = new byte[] { 65, 65, 65, 61, 63, 63, 63, 61, 61, 61, 61, 61 };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamWithCustomParameters() throws IOException {
        String inputData = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(input, true, 64, new byte[] { 13, 10 });

        byte[] buffer = new byte[5];
        int len;

        // test read method
        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 4);
        assertArrayEquals(new byte[] { 65, 65, 65, 65 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 4);
        assertArrayEquals(new byte[] { 65, 65, 65, 66 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, 2);
        assertArrayEquals(new byte[] { 13, 10 }, buffer);

        len = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(len, -1);

        byte[] result = outputStream.toByteArray();
        byte[] expected = new byte[] { 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 65, 13, 10, 65, 65, 65, 66, 13, 10 };
        assertArrayEquals(expected, result);
    }

    @Test
    public void testBase64InputStreamWithNullArguments() {
        try {
            new Base64InputStream(null, true);
            assert(false); // should throw NullPointerException
        } catch (NullPointerException e) {
            // expected
        }

        try {
            new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
            assert(false); // should throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new Base64InputStream(new ByteArrayInputStream(new byte[0]), true, 4, null);
            assert(false); // should throw NullPointerException
        } catch (NullPointerException e) {
            // expected
        }
    }
}