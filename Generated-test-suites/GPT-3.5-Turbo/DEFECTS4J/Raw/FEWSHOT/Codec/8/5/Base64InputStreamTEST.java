
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello World!".getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream b64is = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        int bytesRead = b64is.read(buffer);

        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello World!".getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream b64is = new Base64InputStream(bais);
        byte[] buffer = new byte[20];
        int offset = 5;
        int length = 10;
        int bytesRead = b64is.read(buffer, offset, length);

        assertEquals(length, bytesRead);
        assertArrayEquals(data, 0, bytesRead, buffer, offset);
    }

    @Test
    public void testReadEmptyData() throws IOException {
        byte[] data = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream b64is = new Base64InputStream(bais);
        byte[] buffer = new byte[10];
        int bytesRead = b64is.read(buffer);

        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] data = "Hello World!".getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream b64is = new Base64InputStream(bais);
        byte[] buffer = new byte[20];
        int offset = -5;
        int length = -10;
        int bytesRead = b64is.read(buffer, offset, length);

        assertEquals(0, bytesRead);
        assertArrayEquals(new byte[20], buffer);
    }

    @Test
    public void testReadWithInvalidBuffer() throws IOException {
        byte[] data = "Hello World!".getBytes("UTF-8");
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.encodeBase64(data));
        Base64InputStream b64is = new Base64InputStream(bais);
        byte[] buffer = null;
        int bytesRead = b64is.read(buffer);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[10]);
        Base64InputStream b64is = new Base64InputStream(bais);
        boolean supported = b64is.markSupported();

        assertEquals(false, supported);
    }
}
