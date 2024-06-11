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
    public void testBase64InputStream_default_decode() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGVpeC5jb20=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expected = "hello world".getBytes();
        byte[] buffer = new byte[expected.length];
        int bytesRead = base64InputStream.read(buffer, 0, expected.length);
        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, buffer);

        input.close();
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStream_encode_throwException() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGVpeC5jb20=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true); // encode

        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertNull(bytesRead);
    }

    @Test
    public void testBase64InputStream_lineLength_decode() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGVpeC5jb20=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 50, new byte[] { '\r', '\n' });

        byte[] expected = "hello world".getBytes();
        byte[] buffer = new byte[expected.length];
        int bytesRead = base64InputStream.read(buffer, 0, expected.length);
        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, buffer);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        base64InputStream.writeTo(output);
        byte[] actual = output.toByteArray();

        String expectedEncoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGVpeC5jb20=";
        String actualEncoded = new String(actual);
        assertTrue(actualEncoded.startsWith(expectedEncoded.substring(0, 60)) && actualEncoded.endsWith(expectedEncoded.substring(expectedEncoded.length() - 60)));

        input.close();
        base64InputStream.close();
        output.close();
    }

    @Test
    public void testBase64InputStream_lineLength_encode() throws IOException {
        InputStream input = new ByteArrayInputStream("hello world".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Base64InputStream base64InputStream = new Base64InputStream(input, false, 50, new byte[] { '\r', '\n' });
        base64InputStream.writeTo(output);
        byte[] actual = output.toByteArray();

        String expectedEncoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGVpeC5jb20=";
        String actualEncoded = new String(actual);
        assertEquals(expectedEncoded, new String(actual, "UTF-8"));

        input.close();
        base64InputStream.close();
        output.close();
    }
}