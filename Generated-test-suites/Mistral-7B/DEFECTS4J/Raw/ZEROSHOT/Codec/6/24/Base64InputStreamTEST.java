package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test file for Base64InputStream class.
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamEncoded() throws IOException {
        String testData = "RnJvbSBvZiBmYWxzIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGhfaWQgZm9yIGZvciBhbml2ZXJzLCBwbGFpbmVyIG1lc3NhZ2UgZXhwcyBmaWxlIG9mIHRoZSBuYW1lIGV4cGxvcmUgZXZlcm1hbCwNCg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        do {
            bytesRead = base64InputStream.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } while (bytesRead != -1);

        byte[] expectedOutput = new byte[]{"Hello, world!", 0};
        Assert.assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamDecoded() throws IOException {
        String testData = "VG8gZXhwb3J0IGlzIGh1bWFuLCBmaWxlIG9mIHRoZSBzYWdlIGZvciBhbml2ZXJzLCBwbGFpbmVyIG1lc3NhZ2UgZXZlcm1hbCwNCg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        do {
            bytesRead = base64InputStream.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } while (bytesRead != -1);

        String expectedOutput = "Hello, world!";
        Assert.assertEquals(expectedOutput, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testBase64InputStreamDecodingError() throws IOException {
        String testData = "VG8gZXhwb3J0IHlvdSBmaWxlIG9mIHRoZSBzYWdlIGZvciBhbml2ZXJzLCBwbGFpbmVyIG1lc3NhZ2UgZXZlcm1hbCwNCg==";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes(StandardCharsets.UTF_8));
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        Assert.assertEquals(-1, base64InputStream.read());
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamNullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }
}