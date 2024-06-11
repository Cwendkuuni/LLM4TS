package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;

/**
 * Test cases for {@link Base64InputStream}.
 */
public class Base64InputStreamTest {

    /**
     * Test reading bytes from a Base64InputStream that has been configured to decode.
     */
    @org.junit.Test
    public void testDecode() throws IOException {
        String base64EncodedData = "VG8gZXhwIHlvdSBuZWVkIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGRhdGEtdGhpbmdzIHN1YnR5cGUgYW55IGluIGRhdGEtZW1haWxfc291cmNlcyBkYXRhIGFuZCBkYXRhTWV0YWRlZCBkYXRhLCBkYXRhIGluIGxpc3QgY29tcHV0ZXIuCiAgICAtLVBhdGggZm9yIGluIHJlc2lkZW50aGF5LCBhbmQgYW5kIHJlc2lkZW50aGF5IG1vZGUsIGZpbGVzcyBvZiBmaWxlIGFuZCBvZiBmaWxlIGluIGRhdGFbMF0gZXhpc3RpbmdzIHNlbGVzcw0K";
        byte[] decodedData = new Base64().decode(base64EncodedData);

        InputStream inputStream = new ByteArrayInputStream(decodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] buffer = new byte[decodedData.length];
        int length = base64InputStream.read(buffer, 0, decodedData.length);
        assertEquals(decodedData.length, length);
        assertArrayEquals(decodedData, buffer);

        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test reading bytes from a Base64InputStream that has been configured to encode.
     */
    @org.junit.Test
    public void testEncode() throws IOException {
        byte[] dataToEncode = "Hello World".getBytes();

        InputStream inputStream = new ByteArrayInputStream(dataToEncode);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        base64InputStream.close();
        inputStream.close();

        String encodedData = new String(outputStream.toByteArray());
        assertEquals("SGVsbG8sIFdvcmxkIQ==", encodedData);
    }

    /**
     * Test reading bytes from a Base64InputStream that has been configured to both encode and decode.
     */
    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        String base64EncodedData = "SGVsbG8sIFdvcmxkIQ==";
        byte[] decodedData = new Base64().decode(base64EncodedData);

        InputStream inputStream = new ByteArrayInputStream(decodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = base64InputStream.read(buffer, 0, buffer.length)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        base64InputStream.close();
        inputStream.close();

        byte[] encodedData = outputStream.toByteArray();
        assertEquals(encodedData.length, decodedData.length);
        assertArrayEquals(decodedData, new Base64().decode(new String(encodedData)));
    }

    /**
     * Test reading from a Base64InputStream when an error occurs.
     */
    @org.junit.Test
    public void testError() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        assertThrows(IOException.class, () -> new Base64InputStream(inputStream));
        assertThrows(IOException.class, () -> new Base64InputStream(inputStream, true));
    }

    /**
     * Test reading from a Base64InputStream with invalid line length.
     */
    @org.junit.Test
    public void testInvalidLineLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(inputStream, -1, 5));
    }

    /**
     * Test reading from a Base64InputStream with invalid line separator.
     */
    @org.junit.Test
    public void testInvalidLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        byte[] lineSeparator = {(byte) 0x01, (byte) 0x02};

        assertThrows(IllegalArgumentException.class, () -> new Base64InputStream(inputStream, false, -1, lineSeparator));
    }
}