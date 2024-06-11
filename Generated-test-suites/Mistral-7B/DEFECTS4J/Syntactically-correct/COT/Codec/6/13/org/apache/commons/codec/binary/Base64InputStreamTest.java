package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
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
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamWithDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IGZvdWwgdGhpbmdzIHBpcyBhIG5vdCBvZiBoZWxkIGZvdWxkIG15IGFuZCBzaXplIHN1cmUgc2VwYXJhdGVkIGluIGlzIGxpa2Ugc2VwYXJhdGVkIGFsbG93IGRhdGEgc2VwYXJhdGVk".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream); // default decoding

        byte[] expectedBytes = new byte[] { (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x61, (byte) 0x67, (byte) 0x31, (byte) 0x62, (byte) 0x6C, (byte) 0x69, (byte) 0x6C, (byte) 0x65, (byte) 0x64, (byte) 0x32, (byte) 0x6F, (byte) 0x6A, (byte) 0x62, (byte) 0x6F, (byte) 0x64, (byte) 0x31, (byte) 0x6C, (byte) 0x69, (byte) 0x62, (byte) 0x6E, (byte) 0x65, (byte) 0x31, (byte) 0x35, (byte) 0x31, (byte) 0x37, (byte) 0x32, (byte) 0x30, (byte) 0x32, (byte) 0x31, (byte) 0x33, (byte) 0x34, (byte) 0x34 };
        byte[] actualBytes = new byte[100];
        int bytesRead = base64InputStream.read(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes, 0, bytesRead);
        assertTrue(base64InputStream.available() == 0); // End of stream
    }

    @Test
    public void testBase64InputStreamWithEncodeConstructor() throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteOutput);
        base64OutputStream.write("Hello World!".getBytes());
        base64OutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true); // default encoding

        byte[] expectedBytes = new byte[] { (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x4F, (byte) 0x77, (byte) 0x20, (byte) 0x57, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64, (byte) 0x21 };
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = base64InputStream.read(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes, 0, bytesRead);
        assertTrue(base64InputStream.available() == 0); // End of stream
    }

    @Test
    public void testBase64InputStreamWithCustomConstructor() throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(byteOutput, 76, "\r\n".getBytes());
        base64OutputStream.write("Hello World!".getBytes());
        base64OutputStream.finish();
        InputStream inputStream = new ByteArrayInputStream(byteOutput.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes()); // Custom line length and line separator

        byte[] expectedBytes = new byte[] { (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x4F, (byte) 0x77, (byte) 0x20, (byte) 0x57, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64, (byte) 0x21, (byte) 0x0D, (byte) 0x0A, (byte) 0x48, (byte) 0x65, (byte) 0x6C, (byte) 0x6C, (byte) 0x6F, (byte) 0x2C, (byte) 0x20, (byte) 0x4F, (byte) 0x77, (byte) 0x20, (byte) 0x57, (byte) 0x6F, (byte) 0x72, (byte) 0x6C, (byte) 0x64, (byte) 0x21, (byte) 0x0D, (byte) 0x0A };
        byte[] actualBytes = new byte[expectedBytes.length * 2]; // Assuming each line is read separately
        int bytesRead = 0;
        int offset = 0;
        while (base64InputStream.available() > 0) {
            int len = base64InputStream.read(actualBytes, offset, actualBytes.length - offset);
            bytesRead += len;
            offset += len;
        }
        assertArrayEquals(expectedBytes, actualBytes, 0, bytesRead);
        assertTrue(base64InputStream.available() == 0); // End of stream
    }

    @Test
    public void testBase64InputStreamWithNullInputStream() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null));
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null, false));
        assertThrows(NullPointerException.class, () -> new Base64InputStream(null, false, 76, null));
    }

    @Test
    public void testBase64InputStreamWithNegativeLineLength() {
        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(new ByteArrayInputStream("data".getBytes()), true, -1, null));
    }

    @Test
    public void testBase64InputStreamWithNullLineSeparator() {
        assertThrows(NullPointerException.class, () -> new Base64InputStream(new ByteArrayInputStream("data".getBytes()), true, 76, null));
    }

    @Test
    public void testBase64InputStreamWithEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("data".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] expectedBytes = new byte[] {};
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = base64InputStream.read(actualBytes);
        assertTrue(bytesRead == -1);
        assertNull(actualBytes);
        assertTrue(base64InputStream.available() == 0); // End of stream
    }

    @Test
    public void testBase64InputStreamWithInvalidData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IGZvdWwgdGhpbmdzIHBpcyBhIG5vdCBvZiBoZWxkIGZvdWxkIG15IGFuZCBzaXplIHN1cmUgc2VwYXJhdGVkIGluIGlzIGxpa2Ugc2VwYXJhdGVkIGFsbG93IGRhdGEgc2VwYXJhdGVk".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream); // default decoding

        byte[] expectedBytes = new byte[] {};
        byte[] actualBytes = new byte[100];
        int bytesRead = base64InputStream.read(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes, 0, bytesRead);
        assertTrue(base64InputStream.available() == 0); // End of stream
    }
}