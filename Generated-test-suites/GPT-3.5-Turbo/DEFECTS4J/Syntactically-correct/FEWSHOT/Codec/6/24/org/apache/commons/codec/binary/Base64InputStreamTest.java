
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testConstructor() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        assertFalse(base64InputStream.doEncode);
    }

    @Test
    public void testConstructorWithEncode() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, true);
        assertTrue(base64InputStream.doEncode);
    }

    @Test
    public void testConstructorWithLineLengthAndLineSeparator() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream, false, 76, "\r\n".getBytes());
        assertFalse(base64InputStream.doEncode);
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        assertEquals(116, base64InputStream.read());
        assertEquals(101, base64InputStream.read());
        assertEquals(115, base64InputStream.read());
        assertEquals(116, base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadWithBufferAndOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        assertEquals(4, base64InputStream.read(buffer, 0, 4));
        assertArrayEquals(new byte[]{116, 101, 115, 116, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(buffer, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(buffer, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[5];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(buffer, 0, 100);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        assertFalse(base64InputStream.markSupported());
    }
}
