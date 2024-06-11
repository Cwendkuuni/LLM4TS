package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test reading an encoded stream and decoding it.
     */
    @Test
    public void testDecodeStream() throws IOException {
        String encodedData = "VG8gZXhhbXBsZSBoYXZlIGlzIGh1bWFuLCBmaWxlIG11dGF0b24=";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encodedData.getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read = 0;
        byte[] buffer = new byte[1024];

        while ((read = base64InputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, read);
        }

        byte[] decodedData = outputStream.toByteArray();
        assertEquals("Test data", "Test data".getBytes(StandardCharsets.UTF_8), decodedData);
    }

    /**
     * Test reading a decoded stream and encoding it.
     */
    @Test
    public void testEncodeStream() throws IOException {
        String decodedData = "Test data";
        byte[] decodedBytes = decodedData.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64 base64 = new Base64();

        int read = 0;
        byte[] buffer = new byte[decodedBytes.length];

        while ((read = inputStream.read(buffer)) > 0) {
            base64OutputStream.write(buffer, 0, read);
        }

        base64OutputStream.finish();
        outputStream.flush();

        byte[] encodedData = outputStream.toByteArray();
        assertArrayEquals("Encoded data", encodedData, Base64.decodeBase64(encodedData));
    }

    /**
     * Test reading an empty stream.
     */
    @Test
    public void testEmptyStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int read = base64InputStream.read();

        assertEquals("End of stream reached", -1, read);
    }

    /**
     * Test reading an invalid stream.
     */
    @Test(expected = IOException.class)
    public void testInvalidStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int read = base64InputStream.read();
    }
}