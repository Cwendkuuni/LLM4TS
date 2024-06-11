package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamRead() throws IOException {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int result = base64InputStream.read();
        assertEquals('t', result);
    }

    @Test
    public void testBase64InputStreamReadWithZeroBytes() throws IOException {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int result = base64InputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testBase64InputStreamReadWithInvalidOffset() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] buffer = new byte[4];
        assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(buffer, -1, 4));
    }

    @Test
    public void testBase64InputStreamMarkSupported() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testBase64InputStreamReadWithNullBuffer() {
        InputStream in = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertThrows(NullPointerException.class, () -> base64InputStream.read(null, 0, 4));
    }

}