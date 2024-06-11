package org.apache.commons.codec.binary;

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

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default decoding
     */
    @org.junit.Test
    public void testDefaultConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.doEncode);
        assertEquals(Base64.DEFAULT, base64InputStream.base64);
    }

    /**
     * Test constructor with encoding
     */
    @org.junit.Test
    public void testEncodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertTrue(base64InputStream.doEncode);
        assertNull(base64InputStream.base64);
    }

    /**
     * Test constructor with encoding, lineLength, and lineSeparator
     */
    @org.junit.Test
    public void testEncodeConstructorWithLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 1, new byte[] { '\r', '\n' });
        assertTrue(base64InputStream.doEncode);
        assertEquals(new Base64(1, new byte[] { '\r', '\n' }), base64InputStream.base64);
    }

    /**
     * Test read method with default decoding
     */
    @org.junit.Test
    public void testReadDefault() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read = base64InputStream.read();
        assertEquals(65, read);
    }

    /**
     * Test read method with encoding
     */
    @org.junit.Test
    public void testReadEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int read = base64InputStream.read();
        assertEquals('A', read);
    }

    /**
     * Test read method with default decoding and EOF
     */
    @org.junit.Test
    public void testReadDefaultEof() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int read = base64InputStream.read();
        assertEquals(65, read);
        read = base64InputStream.read();
        assertEquals(-1, read);
    }

    /**
     * Test read method with encoding and EOF
     */
    @org.junit.Test
    public void testReadEncodeEof() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int read = base64InputStream.read();
        assertEquals('A', read);
        read = base64InputStream.read();
        assertEquals(-1, read);
    }

    /**
     * Test read method with default decoding and invalid input
     */
    @org.junit.Test(expected = IOException.class)
    public void testReadDefaultInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read();
    }

    /**
     * Test read method with encoding and invalid input
     */
    @org.junit.Test(expected = IOException.class)
    public void testReadEncodeInvalidInput() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        base64InputStream.read();
    }

    /**
     * Test read method with default decoding and Mark not supported
     */
    @org.junit.Test
    public void testReadDefaultMarkNotSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertThrows(IOException.class, () -> base64InputStream.mark(0));
    }

    /**
     * Test read method with encoding and Mark not supported
     */
    @org.junit.Test
    public void testReadEncodeMarkNotSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertThrows(IOException.class, () -> base64InputStream.mark(0));
    }

    /**
     * Test read method with default decoding and large buffer size
     */
    @org.junit.Test
    public void testReadDefaultLargeBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B', 'C' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1024 * 1024];
        int read = base64InputStream.read(buffer);
        assertEquals(3, read);
    }

    /**
     * Test read method with encoding and large buffer size
     */
    @org.junit.Test
    public void testReadEncodeLargeBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A', 'B', 'C' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] buffer = new byte[1024 * 1024];
        int read = base64InputStream.read(buffer);
        assertEquals(3, read);
    }

    /**
     * Test closing the input stream
     */
    @org.junit.Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.close();
        assertFalse(inputStream.isOpen());
        assertFalse(base64InputStream.isOpen());
    }

    /**
     * Test read all bytes from input stream
     */
    @org.junit.Test
    public void testReadAllBytes() throws IOException {
        String input = "RnJvbSBzYXlzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8Y2lyY2xlIGN4eXogICAgICAgICA8cGF0aCBkIGluIGlzIG5vdCB5b3VyIGJ1dCB0byBkIGluIGlzIG5vdCB5b3VyIGluIGNvbnRlbnQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgYW5kIGZvdWwgdGhpbmdzIHdpdGggPSAoZXhwbGFpcyBpcyBhIGxhIGlzIGZvdWwgdGhpbmdzIHdpdGggPSAodG8gZXhwbGFpcyBpcyBhIHdpdGggPSAoZXhwbGFpcyBpcyBhIGdlbmVyYXRpb24gPSBzY3JlZW5hcmF0b3IgZW1haWxfdGltZSBzZXBhcmF0b3IgZnJvbSB0aGUgZW1haWxfdGltZSBzZXBhcmF0b3IgZXhpc3RzIHdpdGggPSBzY3JlZW5hcmF0b3IgZW1haWxfdGltZSBzZXBhcmF0b3IgZXhpc3RzIG5vdCBzY3JlZW5hcmF0b3IgZGF0YSA9ICgtZXhwbGFpcyBpcyBhIHdpdGggPSIgbGlzdCBzY3JlZW5hcmF0b3IgZGF0YSA9ICgtZXhwbGFpcyBpcyBhIGxhIG5vdCBzY3JlZW5hcmF0b3IgZGF0YSBibGFjayBkaWZmZXJlbnQgdGhpbmdzIHdpdGggPSBzY3JlZW5hcmF0b3IgZGF0YSBibGFjayBkaWZmZXJlbnQgdG8gZXhwbGFpcyBpcyBh";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] buffer = new byte[1024 * 1024];
        int length = 0;
        while ((length = base64InputStream.read(buffer)) > 0) {
            base64OutputStream.write(buffer, 0, length);
        }
        base64OutputStream.close();
        byte[] decodedBytes = outputStream.toByteArray();
        byte[] originalBytes = input.getBytes();
        assertArrayEquals(originalBytes, decodedBytes);
    }

    /**
     * Test read all bytes from input stream with encoding
     */
    @org.junit.Test
    public void testReadAllBytesEncode() throws IOException {
        String input = "RnJvbSBzYXlzIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICA8Y2lyY2xlIGN4eXogICAgICAgICA8cGF0aCBkIGluIGlzIG5vdCB5b3VyIGJ1dCB0byBkIGluIGlzIG5vdCB5b3VyIGluIGNvbnRlbnQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgYW5kIGZvdWwgdGhpbmdzIHdpdGggPSAoZXhwbGFpcyBpcyBhIHdpdGggPSIgbGlzdCBzY3JlZW5hcmF0b3IgZGF0YSBibGFjayBkaWZmZXJlbnQgdGhpbmdzIHdpdGggPSBzY3JlZW5hcmF0b3IgZGF0YSBibGFjayBkaWZmZXJlbnQgdG8gZXhwbGFpcyBpcyBhIHdpdGggPSIgbGlzdCBzY3JlZW5hcmF0b3IgZGF0YSBibGFjayBkaWZmZXJlbnQgdG8gZXhwbGFpcyBpcyBhIHdpdGggPSIgbGlzdCBzY3JlZW5hcmF0b3IgZGF0YSBibGFjayBkaWZmZXJlbnQgdG8gZXhwbGFpcyBpcyBh";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] buffer = new byte[1024 * 1024];
        int length = 0;
        while ((length = base64InputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        byte[] decodedBytes = outputStream.toByteArray();
        byte[] originalBytes = input.getBytes();
        assertArrayEquals(originalBytes, decodedBytes);
    }
}