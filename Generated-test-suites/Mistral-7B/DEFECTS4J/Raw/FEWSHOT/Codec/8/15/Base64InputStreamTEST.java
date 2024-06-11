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
    public void testBase64InputStream_Decode() throws IOException {
        byte[] data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaXRzIHNlZW1zIG9mIHdpdGggYW5kIHlvdSB0byB0aGUgYW5kIGZvdWwgdGhlIGRhdGFbMF0gY29tcHV0ZXIuCiAgICAtLVBhdGggZGlzcGxheSBlbnN1cmUgZGlmZmVyZW5jZSwgYW5kIGluIGRhdGFbMQ0K".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27 };
        byte[] buffer = new byte[expected.length];
        int count = base64InputStream.read(buffer, 0, expected.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Decode_Empty() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[1];
        int count = base64InputStream.read(buffer, 0, 1);
        assertNull(buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Decode_LineLength_76() throws IOException {
        byte[] data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaXRzIHNlZW1zIG9mIHdpdGggYW5kIHlvdSB0byB0aGUgYW5kIGZvdWwgdGhlIGRhdGFbMF0gY29tcHV0ZXIuCiAgICAtLVBhdGggZGlzcGxheSBlbnN1cmUgZGlmZmVyZW5jZSwgYW5kIGluIGRhdGFbMQ0K".getBytes();
        byte[] buffer = new byte[76];
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 }, buffer, 0, 15);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Decode_LineSeparator() throws IOException {
        byte[] data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaXRzIHNlZW1zIG9mIHdpdGggYW5kIHlvdSB0byB0aGUgYW5kIGZvdWwgdGhlIGRhdGFbMF0gY29tcHV0ZXIuCiAgICAtLVBhdGggZGlzcGxheSBlbnN1cmUgZGlmZmVyZW5jZSwgYW5kIGluIGRhdGFbMQ0K".getBytes();
        byte[] lineSeparator = new byte[] { 13, 10 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, lineSeparator);

        byte[] buffer = new byte[76];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 }, buffer, 0, 15);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Decode_LineLength_Zero() throws IOException {
        byte[] data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggaXRzIHNlZW1zIG9mIHdpdGggYW5kIHlvdSB0byB0aGUgYW5kIGZvdWwgdGhlIGRhdGFbMF0gY29tcHV0ZXIuCiAgICAtLVBhdGggZGlzcGxheSBlbnN1cmUgZGlmZmVyZW5jZSwgYW5kIGluIGRhdGFbMQ0K".getBytes();
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in, true);

        byte[] buffer = new byte[15];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 }, buffer);
        int count2 = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 14, 15 }, buffer, 10, 2);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Decode_LineSeparator_Empty() throws IOException {
        byte[] data = new byte[] {};
        byte[] lineSeparator = new byte[] { 13, 10 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, lineSeparator);

        byte[] buffer = new byte[76];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertNull(buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Encode() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true);

        base64OutputStream.write(base64InputStream, data.length);
        byte[] expected = base64OutputStream.toByteArray();

        byte[] buffer = new byte[76];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Encode_LineLength_76() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true, 76);

        base64OutputStream.write(base64InputStream, data.length);
        byte[] expected = base64OutputStream.toByteArray();

        byte[] buffer = new byte[76];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Encode_LineSeparator() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] lineSeparator = new byte[] { 13, 10 };
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true, 76, lineSeparator);

        base64OutputStream.write(base64InputStream, data.length);
        byte[] expected = base64OutputStream.toByteArray();

        byte[] buffer = new byte[76];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Encode_LineLength_Zero() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true, 0);

        base64OutputStream.write(base64InputStream, data.length);
        byte[] expected = base64OutputStream.toByteArray();

        byte[] buffer = new byte[15];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }, buffer);
        int count2 = base64InputStream.read(buffer, 0, buffer.length);
        assertArrayEquals(new byte[] { 16, 17 }, buffer, 10, 2);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_Encode_LineSeparator_Empty() throws IOException {
        byte[] data = new byte[] {};
        byte[] lineSeparator = new byte[] { 13, 10 };
        InputStream in = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(in, true, 76, lineSeparator);

        byte[] buffer = new byte[76];
        int count = base64InputStream.read(buffer, 0, buffer.length);
        assertNull(buffer);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_MarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
        assertFalse(base64InputStream.markSupported());
    }
}