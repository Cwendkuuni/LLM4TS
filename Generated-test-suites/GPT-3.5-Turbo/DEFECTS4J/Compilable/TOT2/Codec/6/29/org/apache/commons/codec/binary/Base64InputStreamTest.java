package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_constructor1() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStream_constructor2() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStream_constructor3() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, new byte[] {13, 10});
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStream_read() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int data = base64InputStream.read();
        assertEquals(84, data);
    }

    @Test
    public void testBase64InputStream_readArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        int bytesRead = base64InputStream.read(buffer, 0 ,4);
        assertEquals(4, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStream_readArray_null() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStream_readArray_invalidOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 5, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStream_readArray_invalidLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer, 0, 5);
    }

    @Test
    public void testBase64InputStream_markSupported() {
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}