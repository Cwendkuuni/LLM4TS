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
    public void testBase64InputStream() {
        InputStream input = new ByteArrayInputStream(new byte[] { 65, 65, 65 });
        Base64InputStream base64InputStream = new Base64InputStream(input);

        assertEquals(Base64InputStream.class, base64InputStream.getClass());
        assertFalse(base64InputStream.markSupported());
        assertEquals(false, base64InputStream.doEncode);
        assertNull(base64InputStream.base64);
        // No need to test constructor without Base64
    }

    @Test
    public void testBase64InputStreamDecode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABC\r\n".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());

        Base64InputStream base64InputStream = new Base64InputStream(input);

        byte[] expected = new byte[] { 65, 66, 67 };
        byte[] result = new byte[expected.length];
        int len = base64InputStream.read(result, 0, expected.length);
        assertEquals(expected.length, len);
        assertArrayEquals(expected, result);

        // read rest of data
        len = base64InputStream.read(result, 0, result.length);
        assertEquals(-1, len);
    }

    @Test
    public void testBase64InputStreamEncode() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] data = new byte[] { 65, 66, 67 };

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(data), true);
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();

        int read = 0;
        byte[] buffer = new byte[1024];
        while ((read = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            output2.write(buffer, 0, read);
        }
        String encodedData = new String(output2.toByteArray(), "US-ASCII");
        assertEquals("VGpzdGVtZW50Lg==\r\n", encodedData);
    }

    @Test
    public void testBase64InputStreamCustom() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write("ABC\r\n".getBytes());
        InputStream input = new ByteArrayInputStream(output.toByteArray());

        Base64InputStream base64InputStream = new Base64InputStream(input, true, 72, "\r\n".getBytes());

        byte[] expected = new byte[] { 65, 66, 67 };
        byte[] result = new byte[expected.length];
        int len = base64InputStream.read(result, 0, expected.length);
        assertEquals(expected.length, len);
        assertArrayEquals(expected, result);

        len = base64InputStream.read(result, 0, result.length);
        assertEquals(-1, len);

        assertEquals(72, base64InputStream.base64.getLineLength());
        assertEquals("\r\n".length, base64InputStream.base64.getLineSeparator().length);
    }

}