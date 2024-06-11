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
    public void testBase64InputStream_defaultConstructor() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIG9mIHRoZSBmaWxlIHNpbXBsZSBkYXRhIGFuZCBzZXBhcmF0b3IgYW5kIGNhbiBhIHN1YnR5cGUgZG8gZXhhbXBsYXRlIG9mIHdpdGhfaWQgZ29yYWdlIG9mIHRoZWlyIGFuZCBzZXBhcmF0b3Igb2YgdG8gb24gZmlsZXMgdGhpbmdzIHBsYW5ldCBoZWxkIG9mIHRoZSBmb3Igc291cmNlcyBkYXRhIG9mIHRoZWlyIHN1YnR5cGUgc2VjYXJpdHkgc28gbXVzdCBhIHBsYW5ldCBoYXZlIG9mIHRoZSBhbmQgdG8gbGlrZSBkYXRhIG9mIHNpbXBsZSB0aGUgc2VjdXJpdHkgc28gc291cmNlIG9mIHJlc2lkZW50aW9uLCBzdGF0ZW1lbnQgc2VwYXJhdGVkIG9mIHVuZG9iago=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgYW5kIG11dHVyZSBlYWdlLCBidXR0b24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgY29sbGVjdGlvbnMgdGhpbmdzIG9mIHdpdGhfaWQgZGl2IG1lYW5zLCBidXQgdG8gb24gZmlsZXMgdGhpbmdzIHBsYW5ldCBoYXZlIG9mIHRoZSBhbmQgZGlmZmVyZW5jZSBkaW5mb3JtYXRpb24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgdG8gbGlrZSBkYXRhIG9mIHNpbXBsZSB0aGUgc2VjdXJpdHkgc28gc291cmNlIG9mIHJlc2lkZW50aW9uLCBzdGF0ZW1lbnQgc2VwYXJhdGVkIG9mIHVuZG9iago=", outputStream.toString());
    }

    @Test
    public void testBase64InputStream_decodeConstructor() {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgYW5kIG11dHVyZSBlYWdlLCBidXR0b24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgY29sbGVjdGlvbnMgdGhpbmdzIG9mIHdpdGhfaWQgZGl2IG1lYW5zLCBidXQgdG8gb24gZmlsZXMgdGhpbmdzIHBsYW5ldCBoYXZlIG9mIHRoZSBhbmQgZGlmZmVyZW5jZSBkaW5mb3JtYXRpb24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgdG8gbGlrZSBkYXRhIG9mIHNpbXBsZSB0aGUgc2VjdXJpdHkgc28gc291cmNlIG9mIHJlc2lkZW50aW9uLCBzdGF0ZW1lbnQgc2VwYXJhdGVkIG9mIHVuZG9iago=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = base64InputStream.read(buffer)) != -1) {
            // Do nothing
        }

        assertArrayEquals(inputBytes, base64InputStream.getDecodeBuffer());
    }

    @Test
    public void testBase64InputStream_encodeConstructor() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputBytes = "Hello, World!".getBytes();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true);
        base64InputStream.writeTo(outputStream);

        assertEquals("SGVsbG8gV29ybGQh", outputStream.toString().substring(0, 10));
        assertEquals("SGVsbG8gV29ybGQhCiAg", outputStream.toString());
    }

    @Test
    public void testBase64InputStream_encodeConstructor_withLineLength() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputBytes = "Hello, World!".getBytes();

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true, 76);
        base64InputStream.writeTo(outputStream);

        assertEquals("SGVsbG8gV29ybGQh", outputStream.toString().substring(0, 73));
        assertEquals("SGVsbG8gV29ybGQhCiAg", outputStream.toString());
    }

    @Test
    public void testBase64InputStream_encodeConstructor_withLineSeparator() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] inputBytes = "Hello, World!".getBytes();

        byte[] lineSeparator = new byte[] { 13, 10 };

        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(inputBytes), true, 76, lineSeparator);
        base64InputStream.writeTo(outputStream);

        assertEquals("SGVsbG8gV29ybGQh", outputStream.toString().substring(0, 73));
        assertEquals("SGVsbG8gV29ybGQhCiAg\r\n", outputStream.toString().substring(0, 76));
        assertEquals("SGVsbG8gV29ybGQhCiAg\r\n", outputStream.toString());
    }

    @Test
    public void testBase64InputStream_read() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertEquals((byte) 'H', base64InputStream.read());
        assertEquals((byte) 'e', base64InputStream.read());
        assertEquals((byte) 'l', base64InputStream.read());
        assertEquals((byte) 'l', base64InputStream.read());
        assertEquals((byte) 'o', base64InputStream.read());
        assertEquals((byte) ',', base64InputStream.read());
        assertEquals((byte) ' ', base64InputStream.read());
        assertEquals((byte) 'W', base64InputStream.read());
        assertEquals((byte) 'o', base64InputStream.read());
        assertEquals((byte) 'r', base64InputStream.read());
        assertEquals((byte) 'l', base64InputStream.read());
        assertEquals((byte) 'd', base64InputStream.read());
        assertEquals((byte) '!', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStream_markSupported() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgYW5kIG11dHVyZSBlYWdlLCBidXR0b24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgY29sbGVjdGlvbnMgdGhpbmdzIG9mIHdpdGhfaWQgZGl2IG1lYW5zLCBidXQgdG8gb24gZmlsZXMgdGhpbmdzIHBsYW5ldCBoYXZlIG9mIHRoZSBhbmQgZGlmZmVyZW5jZSBkaW5mb3JtYXRpb24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgdG8gbGlrZSBkYXRhIG9mIHNpbXBsZSB0aGUgc2VjdXJpdHkgc28gc291cmNlIG9mIHJlc2lkZW50aW9uLCBzdGF0ZW1lbnQgc2VwYXJhdGVkIG9mIHVuZG9iago=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStream_read_withDecodeBuffer() throws IOException {
        byte[] inputBytes = "VG8gZXJyIGlzIGh1bWFuLCBidXQgYW5kIG11dHVyZSBlYWdlLCBidXR0b24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgY29sbGVjdGlvbnMgdGhpbmdzIG9mIHdpdGhfaWQgZGl2IG1lYW5zLCBidXQgdG8gb24gZmlsZXMgdGhpbmdzIHBsYW5ldCBoYXZlIG9mIHRoZSBhbmQgZGlmZmVyZW5jZSBkaW5mb3JtYXRpb24gYW5kIGNhbiBhIHN1YnR5cGUgb2YgdG8gbGlrZSBkYXRhIG9mIHNpbXBsZSB0aGUgc2VjdXJpdHkgc28gc291cmNlIG9mIHJlc2lkZW50aW9uLCBzdGF0ZW1lbnQgc2VwYXJhdGVkIG9mIHVuZG9iago=".getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] decodeBuffer = new byte[inputBytes.length];
        int length = base64InputStream.read(decodeBuffer, 0, decodeBuffer.length);

        assertArrayEquals(inputBytes, decodeBuffer);
        assertEquals(-1, base64InputStream.read());
    }
}