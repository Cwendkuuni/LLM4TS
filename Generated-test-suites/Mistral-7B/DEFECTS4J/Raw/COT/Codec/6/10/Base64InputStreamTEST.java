package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64 base64;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[] { 0x2a, 0x2b, 0x30, 0x31 });
        base64 = new Base64();
        base64InputStream = new Base64InputStream(inputStream, false);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(0x2a, base64InputStream.read());
        assertEquals(0x2b, base64InputStream.read());
        assertEquals(0x30, base64InputStream.read());
        assertEquals(0x31, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = new byte[] { 0x2a, 0x2b, 0x30, 0x31 };
        byte[] buffer = new byte[4];

        int read = base64InputStream.read(buffer);
        assertArrayEquals(expected, buffer, 0, 4);
        assertEquals(-1, base64InputStream.read(buffer));
    }

    @Test
    public void testDoEncodeFalse() throws IOException {
        Base64InputStream base64InputStreamDecoding = new Base64InputStream(inputStream, false);
        assertEquals(0x2a, base64InputStreamDecoding.read());
        assertEquals(0x2b, base64InputStreamDecoding.read());
        assertEquals(0x30, base64InputStreamDecoding.read());
        assertEquals(0x31, base64InputStreamDecoding.read());
        assertEquals(-1, base64InputStreamDecoding.read());
    }

    @Test
    public void testDoEncodeTrue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        byte[] input = new byte[] { 0x2a, 0x2b, 0x30, 0x31 };
        base64OutputStream.write(input, 0, input.length);
        base64OutputStream.finish();
        InputStream inputStreamEncoded = new ByteArrayInputStream(outputStream.toByteArray());

        Base64InputStream base64InputStreamEncoding = new Base64InputStream(inputStreamEncoded, true);
        byte[] buffer = new byte[4];
        int read = base64InputStreamEncoding.read(buffer);
        assertArrayEquals(new byte[] { 0x2a, 0x2b, 0x30, 0x31 }, buffer, 0, 4);
        assertEquals(-1, base64InputStreamEncoding.read(buffer));
    }

    @Test(expected = IOException.class)
    public void testMarkSupportedFalse() throws IOException {
        base64InputStream.markSupported();
    }

    @Test
    public void testConstructorWithInvalidLineLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Base64InputStream(inputStream, true, -1, null);
        });
    }

    @Test
    public void testConstructorWithNullLineSeparator() {
        assertThrows(NullPointerException.class, () -> {
            new Base64InputStream(inputStream, true, 76, null);
        });
    }

    @Test
    public void testConstructorWithLongLineLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Base64InputStream(inputStream, true, 1234, null);
        });
    }

    @Test
    public void testReadWhenNoDataAvailable() throws IOException {
        InputStream inputStreamEmpty = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStreamEmpty = new Base64InputStream(inputStreamEmpty, false);
        byte[] buffer = new byte[4];
        assertEquals(-1, base64InputStreamEmpty.read(buffer));
    }

    @Test
    public void testReadWhenInputStreamThrowsIOException() throws IOException {
        InputStream inputStreamThrowing = new ThrowingInputStream();
        Base64InputStream base64InputStreamThrowing = new Base64InputStream(inputStreamThrowing, false);
        byte[] buffer = new byte[4];
        assertEquals(-1, base64InputStreamThrowing.read(buffer));

        Throwable thrown = assertThrows(IOException.class, () -> {
            base64InputStreamThrowing.read();
        });
        assertEquals("Input stream threw an exception", thrown.getMessage());
    }
}

class ThrowingInputStream extends FilterInputStream {

    @Override
    public int read() throws IOException {
        throw new IOException("Input stream threw an exception");
    }
}