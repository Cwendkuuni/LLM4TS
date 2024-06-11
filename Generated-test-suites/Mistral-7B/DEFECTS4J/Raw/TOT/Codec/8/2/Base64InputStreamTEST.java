package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test decoding a Base64 encoded string using a Base64InputStream
     */
    @Test
    public void testDecodingBase64InputStream() throws IOException {
        String base64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(base64Encoded.getBytes());
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expected = new byte[] { 0x66, 0x69, 0x6c, 0x69, 0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x65, 0x6e, 0x74, 0x68, 0x65, 0x61, 0x74, 0x65, 0x20, 0x49, 0x6d, 0x61, 0x67, 0x6b, 0x61, 0x67, 0x65, 0x20, 0x50, 0x72, 0x6f, 0x63, 0x65, 0x74, 0x20, 0x69, 0x6e, 0x66, 0x6f, 0x20, 0x61, 0x73, 0x74, 0x20, 0x6d, 0x61, 0x69, 0x6e, 0x69, 0x6f, 0x6e, 0x20, 0x63, 0x61, 0x6d, 0x65, 0x6d, 0x65, 0x20, 0x73, 0x74, 0x61, 0x72, 0x74, 0x62, 0x61, 0x63, 0x6b, 0x73, 0x20, 0x62, 0x79, 0x20 };

        byte[] testByteArray = new byte[26];
        int read = base64InputStream.read(testByteArray, 0, 26);
        assertEquals(read, 26);
        assertArrayEquals(expected, testByteArray);

        base64InputStream.close();
    }

    /**
     * Test encoding a byte array using a Base64OutputStream and decoding it using a Base64InputStream
     */
    @Test
    public void testEncodingDecodingByteArray() throws IOException {
        byte[] input = new byte[] { 0x66, 0x69, 0x6c, 0x69, 0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x65, 0x6e, 0x74, 0x68, 0x65, 0x61, 0x74, 0x65, 0x20, 0x49, 0x6d, 0x61, 0x67, 0x6b, 0x61, 0x67, 0x65, 0x20, 0x50, 0x72, 0x6f, 0x63, 0x65, 0x74, 0x20, 0x69, 0x6e, 0x66, 0x6f, 0x20, 0x61, 0x73, 0x74, 0x20, 0x6d, 0x61, 0x69, 0x6e, 0x69, 0x6f, 0x6e, 0x20, 0x63, 0x61, 0x6d, 0x65, 0x6d, 0x65, 0x20, 0x73, 0x74, 0x61, 0x72, 0x74, 0x62, 0x61, 0x63, 0x6b, 0x73 };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n");
        base64OutputStream.write(input);
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, "\r\n".getBytes());

        byte[] testByteArray = new byte[26];
        int read = base64InputStream.read(testByteArray, 0, 26);
        assertEquals(read, 26);
        assertArrayEquals(input, testByteArray);

        base64InputStream.close();
    }

    /**
     * Test decoding invalid base64 data using a Base64InputStream
     */
    @Test(expected = IOException.class)
    public void testDecodingInvalidBase64InputStream() throws IOException {
        String invalidBase64Encoded = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=X";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(invalidBase64Encoded.getBytes());
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] testByteArray = new byte[2];
        int read = base64InputStream.read(testByteArray, 0, 2);
        assert read == -1;

        base64InputStream.close();
    }

    /**
     * Test encoding a byte array using a Base64OutputStream with an invalid line length and decoding it using a
     * Base64InputStream
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEncodingInvalidLineLengthBase64OutputStream() throws IOException {
        byte[] input = new byte[] { 0x66, 0x69, 0x6c, 0x69, 0x65, 0x6e, 0x63, 0x6f, 0x6d, 0x65, 0x6e, 0x74, 0x68, 0x65, 0x61, 0x74, 0x65, 0x20, 0x49, 0x6d, 0x61, 0x67, 0x6b, 0x61, 0x67, 0x65, 0x20, 0x50, 0x72, 0x6f, 0x63, 0x65, 0x74, 0x20, 0x69, 0x6e, 0x66, 0x6f, 0x20, 0x61, 0x73, 0x74, 0x20, 0x6d, 0x61, 0x69, 0x6e, 0x69, 0x6f, 0x6e, 0x20, 0x63, 0x61, 0x6d, 0x65, 0x6d, 0x65, 0x20, 0x73, 0x74, 0x61, 0x72, 0x74, 0x62, 0x61, 0x63, 0x6b, 0x73 };

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n".getBytes()); // incorrect line length
        base64OutputStream.write(input);
        base64OutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, "\r\n".getBytes());

        base64InputStream.close();
    }
}