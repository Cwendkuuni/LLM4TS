package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamConstructor() {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertNotNull(base64InputStream);
    }

    @Test
    public void testBase64InputStreamRead() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        int data = base64InputStream.read();
        assertEquals('T', data);
    }

    @Test
    public void testBase64InputStreamReadByteArray() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] data = new byte[4];
        int bytesRead = base64InputStream.read(data, 0, data.length);
        assertEquals(4, bytesRead);
        assertArrayEquals("Test".getBytes(), data);
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamReadNullBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        base64InputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadInvalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] data = new byte[4];
        base64InputStream.read(data, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadInvalidLength() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        byte[] data = new byte[4];
        base64InputStream.read(data, 0, -1);
    }

    @Test
    public void testBase64InputStreamHasData() throws IOException {
        InputStream in = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.hasData());
    }
}