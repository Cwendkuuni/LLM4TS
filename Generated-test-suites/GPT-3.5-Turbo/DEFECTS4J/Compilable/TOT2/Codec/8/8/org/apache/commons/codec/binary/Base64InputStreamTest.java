package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);
        assertNotNull(b64InputStream);
    }

    @Test
    public void testBase64InputStreamRead() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);

        int data;
        StringBuilder result = new StringBuilder();
        while ((data = b64InputStream.read()) != -1) {
            result.append((char) data);
        }

        assertEquals("Hello, World!", result.toString());
    }

    @Test
    public void testBase64InputStreamReadByteArray() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        assertEquals(5, b64InputStream.read(buffer, 0, 5));

        String result = new String(buffer);
        assertEquals("Hello", result);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamReadByteArrayNullInput() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);

        b64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadByteArrayInvalidOffset() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        b64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadByteArrayInvalidLength() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        b64InputStream.read(buffer, 0, 10);
    }

    @Test
    public void testBase64InputStreamMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream b64InputStream = new Base64InputStream(in);
        assertFalse(b64InputStream.markSupported());
    }
}