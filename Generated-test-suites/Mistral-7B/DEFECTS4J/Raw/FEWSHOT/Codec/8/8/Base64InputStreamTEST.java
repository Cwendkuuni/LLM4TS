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
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] outputBytes = new byte[inputBytes.length];

        int read = 0;
        while ((read = base64InputStream.read(outputBytes)) != -1) {
            outputStream.write(outputBytes, 0, read);
        }

        assertArrayEquals(inputBytes, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamDecodingWithLineLength() throws IOException {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, 76);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] outputBytes = new byte[inputBytes.length];

        int read = 0;
        while ((read = base64InputStream.read(outputBytes)) != -1) {
            outputStream.write(outputBytes, 0, read);
        }

        assertArrayEquals(inputBytes, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamDecodingWithNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
        assertNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamDecodingWithNullOutputArray() {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputBytes = null;

        int read = 0;
        while ((read = base64InputStream.read(outputBytes)) != -1) {
            // This should never be reached as outputBytes is null
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamDecodingWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputBytes = new byte[inputBytes.length];

        int read = base64InputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(inputBytes.length, read);
        assertArrayEquals(inputBytes, outputBytes);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamDecodingWithNegativeOffset() throws IOException {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputBytes = new byte[inputBytes.length];

        int offset = -1;
        int len = inputBytes.length;

        int read = base64InputStream.read(outputBytes, offset, len);
        assertEquals(-1, read);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamDecodingWithNegativeLength() throws IOException {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputBytes = new byte[inputBytes.length];

        int offset = 0;
        int length = -1;

        int read = base64InputStream.read(outputBytes, offset, length);
        assertEquals(-1, read);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamDecodingWithInvalidOffsetAndLength() throws IOException {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQpCg==".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] outputBytes = new byte[inputBytes.length];

        int offset = inputBytes.length + 1;
        int length = inputBytes.length - 1;

        int read = base64InputStream.read(outputBytes, offset, length);
        assertEquals(-1, read);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read = 0;
        while ((read = base64InputStream.read()) != -1) {
            outputStream.write(read);
        }

        String encodedString = new String(outputStream.toByteArray(), "US-ASCII");
        assertEquals("SGVsbG8gV29ybGQh", encodedString);

        byte[] expectedOutputBytes = "SGVsbG8gV29ybGQh".getBytes();
        assertArrayEquals(expectedOutputBytes, outputStream.toByteArray());

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodingWithLineLength() throws IOException {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int read = 0;
        while ((read = base64InputStream.read()) != -1) {
            outputStream.write(read);
        }

        String encodedString = new String(outputStream.toByteArray(), "US-ASCII");
        assertEquals("SGVsbG8gV29ybGQh", encodedString);

        byte[] expectedOutputBytes = "SGVsbG8gV29ybGQh".getBytes();
        assertArrayEquals(expectedOutputBytes, outputStream.toByteArray());

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodingWithNullInputStream() {
        Base64InputStream base64InputStream = new Base64InputStream(null, true);
        assertNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamEncodingWithNullOutputArray() {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] outputBytes = null;

        int read = 0;
        while ((read = base64InputStream.read()) != -1) {
            outputBytes[read] = (byte) read;
        }

        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodingWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] outputBytes = new byte[inputBytes.length];

        int read = base64InputStream.read(outputBytes, 0, outputBytes.length);
        assertEquals(inputBytes.length, read);
        assertArrayEquals(inputBytes, outputBytes);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodingWithNegativeOffset() throws IOException {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] outputBytes = new byte[inputBytes.length];

        int offset = -1;
        int len = inputBytes.length;

        int read = base64InputStream.read(outputBytes, offset, len);
        assertEquals(-1, read);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodingWithNegativeLength() throws IOException {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] outputBytes = new byte[inputBytes.length];

        int offset = 0;
        int length = -1;

        int read = base64InputStream.read(outputBytes, offset, length);
        assertEquals(-1, read);

        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamEncodingWithInvalidOffsetAndLength() throws IOException {
        byte[] inputBytes = "HelloWorld".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] outputBytes = new byte[inputBytes.length];

        int offset = inputBytes.length + 1;
        int length = inputBytes.length - 1;

        int read = base64InputStream.read(outputBytes, offset, length);
        assertEquals(-1, read);

        base64InputStream.close();
    }
}