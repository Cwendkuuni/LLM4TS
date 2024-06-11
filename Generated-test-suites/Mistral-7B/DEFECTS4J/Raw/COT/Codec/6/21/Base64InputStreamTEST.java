package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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
 * Test cases for Base64InputStream class
 */
public class Base64InputStreamTest {

    private static final String BASE64_ENCODED = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggYW5kIGZvdWwgdG8gYW5kIHN1YnR5cGUuCiAgICAtLVBhdGggYW5kIGxheWVyIHNlbGVzIHN1YnR5cGUuCiAgICAtLVBhdGggYW5kIGZpbGVuYWwgdG8gYW5kIHVwIHlvdSBuZWVkIG9mIHRoZSBmaWxlIGFuZCB0byBkYXRhIGFzIG1heCB0byBjYW4gbGlzdCB0byBkYXRhIGFzIGlzIEhlbGwgdG8gYW5kIHBsYW5ldC4K";

    @Test
    public void testBase64InputStreamConstructorWithDefaults() {
        InputStream inputStream = new ByteArrayInputStream(BASE64_ENCODED.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Test default decoding behavior
        byte[] decodedByteArray = new byte[BASE64_ENCODED.length() / 4 * 3];
        int length = base64InputStream.read(decodedByteArray);
        assertEquals(BASE64_ENCODED.length(), length);
        assertArrayEquals(Base64.decodeBase64(BASE64_ENCODED), decodedByteArray);
    }

    @Test
    public void testBase64InputStreamConstructorWithEncode() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        int len;

        // Test encoding behavior
        while ((len = base64InputStream.read()) != -1) {
            base64OutputStream.write(len);
        }

        byte[] encodedByteArray = outputStream.toByteArray();
        assertEquals(BASE64_ENCODED.length(), encodedByteArray.length);
        assertArrayEquals(Base64.decodeBase64(BASE64_ENCODED), encodedByteArray);
    }

    @Test
    public void testBase64InputStreamConstructorWithCustomLineLength() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 50);
        int len;

        // Test decoding behavior with custom line length
        while ((len = base64InputStream.read()) != -1) {
            base64OutputStream.write(len);
        }

        byte[] decodedByteArray = outputStream.toByteArray();
        assertEquals(BASE64_ENCODED.length(), decodedByteArray.length);
        assertArrayEquals(Base64.decodeBase64(BASE64_ENCODED), decodedByteArray);
    }

    @Test
    public void testBase64InputStreamConstructorWithCustomLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        byte[] lineSeparator = "\u0002\u0003".getBytes();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 50, lineSeparator);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 50, lineSeparator);
        int len;

        // Test decoding behavior with custom line separator
        while ((len = base64InputStream.read()) != -1) {
            base64OutputStream.write(len);
        }

        byte[] decodedByteArray = outputStream.toByteArray();
        assertEquals(BASE64_ENCODED.length(), decodedByteArray.length);
        assertArrayEquals(Base64.decodeBase64(BASE64_ENCODED), decodedByteArray);
    }

    @Test
    public void testBase64InputStreamWithEmptyInputStream() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Test empty input stream
        byte[] decodedByteArray = new byte[0];
        int length = base64InputStream.read(decodedByteArray);
        assertFalse(length > 0);
    }

    @Test
    public void testBase64InputStreamWithNullInputStream() {
        InputStream inputStream = null;
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Test null input stream
        assertThrows(NullPointerException.class, () -> base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamWithCustomLineLengthLessThanFour() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 3);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 50);
        int len;

        // Test decoding behavior with custom line length less than 4
        while ((len = base64InputStream.read()) != -1) {
            base64OutputStream.write(len);
        }

        byte[] decodedByteArray = outputStream.toByteArray();
        assertArrayEquals(Base64.decodeBase64(BASE64_ENCODED), decodedByteArray);
        // Assertions for line length are difficult to test, since the output will be incorrect, but the decoding
        // should still work correctly
        byte[] lineSeparator = new byte[4];
        assertEquals(1, base64InputStream.read(lineSeparator));
        assertNull(new String(lineSeparator));
    }
}