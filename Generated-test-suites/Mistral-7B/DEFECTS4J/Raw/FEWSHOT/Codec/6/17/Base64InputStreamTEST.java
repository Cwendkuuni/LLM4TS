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
        InputStream inputStream = new ByteArrayInputStream(
                new byte[]{"MTIzNDU2Nzg5" + System.lineSeparator() + "MQ=="});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[]{'M', 'T', 'I', 'z', 'N', 'D', 'U', '2', 'N', 'z', 'g', '5', System.lineSeparator(), 0, 0};

        byte[] buffer = new byte[expected.length];
        int bytesRead = base64InputStream.read(buffer);
        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, buffer);

        assertTrue(base64InputStream.markSupported()); // for the sake of JUnit
        base64InputStream.mark(0);
        assertNull(base64InputStream.read(buffer)); // verify mark is not supported
    }

    @Test
    public void testBase64InputStream_encode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        String result = outputStream.toString();
        assertEquals("VGhpcyBpcyBwYXNzd29yZA==", result);
    }

    @Test
    public void testBase64InputStream_lineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        String result = outputStream.toString();
        assertEquals("VGhpcyBpcyBwYXNzd29yZA==\r\n", result);
    }

    @Test
    public void testBase64InputStream_lineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, -11});
        byte[] lineSeparator = new byte[]{13, 10};
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, lineSeparator);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, bytesRead);
        }

        String result = outputStream.toString();
        assertEquals("VGhpcyBpcyBwYXNzd29yZA==\r\n", result);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStream_nullInputStream() {
        new Base64InputStream(null);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStream_nullBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52});
        new Base64InputStream(inputStream, true, 72, (byte[]) null);
    }
}