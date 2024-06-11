package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        assertNotNull(base64InputStream);
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testReadSingleByte() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        int result = base64InputStream.read();
        assertEquals('t', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(4, bytesRead);
        assertEquals("test", new String(buffer));
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[0];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
        assertEquals(0, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBuffer() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream("test".getBytes()));
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 5);
    }
}