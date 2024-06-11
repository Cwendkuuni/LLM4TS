package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    /**
     * Test constructor with default settings
     *
     * @throws IOException
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        int read = base64InputStream.read(result);
        assertEquals(4, read);
        assertEquals("VG8g", new String(result, 0, read));
    }

    /**
     * Test constructor with encode flag
     *
     * @throws IOException
     */
    @Test
    public void testEncodeConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] result = new byte[10];
        int read = base64InputStream.read(result);
        assertEquals(8, read);
        assertEquals("SmludGVncmFwaGljYXRpb24=".getBytes(), result);
    }

    /**
     * Test constructor with encode flag, line length and line separator
     *
     * @throws IOException
     */
    @Test
    public void testEncodeConstructorWithParams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, "\n\r".getBytes());
        byte[] result = new byte[10];
        int read = base64InputStream.read(result);
        assertEquals(8, read);
        assertEquals("SmludGVncmFwaGljYXRpb24\r\n".getBytes(), result);
    }

    /**
     * Test exception when passing a null InputStream
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        new Base64InputStream(null);
    }

    /**
     * Test read method when EOF is reached
     *
     * @throws IOException
     */
    @Test
    public void testReadEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[1];
        int read = base64InputStream.read(result);
        assertEquals(4, read);
        assertEquals("VG8g".getBytes(), result, 0, read);
        // EOF
        read = base64InputStream.read(result);
        assertEquals(-1, read);
    }

    /**
     * Test read method when given a negative length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeReadLength() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        base64InputStream.read(result, -1, 2);
    }

    /**
     * Test read method when given a length larger than available bytes
     */
    @Test
    public void testReadMoreBytesThanAvailable() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[10];
        int read = base64InputStream.read(result);
        assertEquals(4, read);
        assertEquals("VG8g".getBytes(), result, 0, read);
        read = base64InputStream.read(result, 4, 6);
        assertEquals(2, read);
    }

    /**
     * Test markSupported method
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertThrows(UnsupportedOperationException.class, base64InputStream::markSupported);
    }

}