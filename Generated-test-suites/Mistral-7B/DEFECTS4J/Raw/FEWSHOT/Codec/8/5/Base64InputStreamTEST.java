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
    public void testBase64InputStream_decodeDefault() {
        InputStream inputStream = new ByteArrayInputStream(
                "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] b = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(b, 0, b.length)) > 0) {
            // consume the data
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        // verify the decoded data
        inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        byte[] expected = outputStream.toByteArray();
        assertArrayEquals(expected, b);
    }

    @Test
    public void testBase64InputStream_decodeCustomLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream(
                "--\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\n--".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, 50, "\r\n--".getBytes());

        byte[] b = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(b, 0, b.length)) > 0) {
            // consume the data
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        // verify the decoded data
        inputStream = new ByteArrayInputStream("--\r\nHello World!\r\n--\r\n".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, len);
        }
        byte[] expected = outputStream.toByteArray();
        assertArrayEquals(expected, b);
    }

    @Test
    public void testBase64InputStream_encodeDefault() {
        String input = "Hello World!";
        byte[] bytes = input.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(bytes, 0, bytes.length);
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] b = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(b, 0, b.length)) > 0) {
            // consume the data
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        // verify the encoded data
        byte[] expected = outputStream.toByteArray();
        assertArrayEquals(expected, b);
    }

    @Test
    public void testBase64InputStream_encodeCustomLineLengthAndLineSeparator() {
        String input = "Hello World!";
        byte[] bytes = input.getBytes();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 50, "\r\n".getBytes());
        base64OutputStream.write(bytes, 0, bytes.length);
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, "\r\n".getBytes());

        byte[] b = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(b, 0, b.length)) > 0) {
            // consume the data
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        // verify the encoded data
        byte[] expected = outputStream.toByteArray();
        assertArrayEquals(expected, b);
    }

    @Test
    public void testBase64InputStream_decodeEmptyInput() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0][0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] b = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(b, 0, b.length)) > 0) {
            // consume the data
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        assertNull(b);
    }

    @Test
    public void testBase64InputStream_decodeInvalidInput() {
        InputStream inputStream = new ByteArrayInputStream("InvalidInput".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] b = new byte[1024];
        int read = 0;
        while ((read = base64InputStream.read(b, 0, b.length)) > 0) {
            // consume the data
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        // verify the decoded data should be empty
        byte[] expected = new byte[0][0];
        assertArrayEquals(expected, b);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStream_nullStream() {
        new Base64InputStream(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStream_negativeOffsetOrLength() {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        new Base64InputStream(inputStream, -1, new byte[0][0]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStream_offsetTooLarge() {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        new Base64InputStream(inputStream, inputStream.available(), new byte[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStream_negativeLineLength() {
        new Base64InputStream(new ByteArrayInputStream("Hello World!".getBytes()), false, -1, new byte[0][0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStream_nullLineSeparator() {
        new Base64InputStream(new ByteArrayInputStream("Hello World!".getBytes()), false, 0, null);
    }
}