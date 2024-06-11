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

    private static final String TEST_DATA = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";

    @Test
    public void testBase64InputStream_decode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[] { 0x41, 0x67, 0x65, 0x74, 0x65, 0x72, 0x79, 0x20, 0x49, 0x6c, 0x6c, 0x65, 0x64, 0x20, 0x47, 0x69, 0x6e, 0x66, 0x6f, 0x72, 0x6d, 0x65, 0x64, 0x20, 0x4c, 0x69, 0x62, 0x65, 0x64, 0x2d, 0x43, 0x61, 0x6d, 0x53, 0x65, 0x72, 0x76, 0x65, 0x72, 0x2d, 0x56, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x3d, 0x3d, 0x3d, 0x00 };

        byte[] result = new byte[expected.length];
        int readLen = base64InputStream.read(result);

        assertArrayEquals(expected, result);
        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test
    public void testBase64InputStream_encode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x41, (byte) 0x42, (byte) 0x43 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead = -1;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        String encodedData = outputStream.toString();
        assertEquals("VG8gZXJyIG1yIGlzIG15IHBhcmNoIGlzIGhlYWx0IHBsYW5ldCwK", encodedData);
    }

    @Test
    public void testBase64InputStream_lineLengthAndLineSeparator() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        byte[] lineSeparator = new byte[] { 0x0D, 0x0A };

        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 48, lineSeparator);

        byte[] expected = new byte[] { 0x41, 0x67, 0x65, 0x74, 0x65, 0x72, 0x79, 0x20, 0x49, 0x6c, 0x6c, 0x65, 0x64, 0x20, 0x47, 0x69, 0x6e, 0x66, 0x6f, 0x72, 0x6d, 0x65, 0x64, 0x20, 0x4c, 0x69, 0x62, 0x65, 0x64, 0x2d, 0x43, 0x61, 0x6d, 0x53, 0x65, 0x72, 0x76, 0x65, 0x72, 0x2d, 0x56, 0x65, 0x72, 0x73, 0x69, 0x6f, 0x6e, 0x3d, 0x3d, 0x3d, 0x0D, 0x0A };

        byte[] result = new byte[expected.length];
        int readLen = base64InputStream.read(result);

        assertArrayEquals(expected, result);
        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test
    public void testBase64InputStream_markSupported() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0x41, (byte) 0x42, (byte) 0x43 });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        assertTrue(base64InputStream.markSupported()); // Should be false
    }
}