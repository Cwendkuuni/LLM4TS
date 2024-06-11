package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Base64InputStreamTest.java
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamWithDefaultConstructor() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(inputStream.doEncode);
        assertNull(inputStream.base64);
    }

    @Test
    public void testBase64InputStreamWithEncodeConstructor() throws IOException {
        Base64OutputStream outputStream = new Base64OutputStream(new ByteArrayOutputStream());
        outputStream.write("abcdefghijklmnopqrstuvwxyz".getBytes());
        outputStream.finish();
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        assertTrue(inputStream.doEncode);
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", inputStream.readLine());
        inputStream.close();
    }

    @Test
    public void testBase64InputStreamWithDecodeConstructor() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] decodedData = new byte[19];
        int read = base64InputStream.read(decodedData);
        assertArrayEquals("abcdefghijklmnopqrstuvwxyz".getBytes(), decodedData, 0, read);
        base64InputStream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamWithNullInputStream() {
        new Base64InputStream(null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamWithNegativeLineLength() {
        new Base64InputStream(new ByteArrayInputStream(new byte[0]), false, -1, null);
    }

    @Test
    public void testBase64InputStreamWithCustomLineLengthAndLineSeparator() throws IOException {
        String base64EncodedData = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream inputStream = new ByteArrayInputStream(base64EncodedData.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 72, "\r\n".getBytes());
        byte[] decodedData = new byte[19];
        int read = base64InputStream.read(decodedData);
        assertArrayEquals("abcdefghijklmnopqrstuvwxyz".getBytes(), decodedData, 0, read);
        String line = base64InputStream.readLine();
        assertEquals("abcdefghijk\r\n", line);
        base64InputStream.close();
    }

    @Test(expected = IOException.class)
    public void testBase64InputStreamWithInvalidBase64Data() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g===".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.close();
    }
}