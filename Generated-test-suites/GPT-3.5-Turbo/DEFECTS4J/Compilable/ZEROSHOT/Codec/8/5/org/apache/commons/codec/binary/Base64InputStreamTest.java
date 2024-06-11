package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        assertEquals(65, result);
    }

    @Test
    public void testReadWithOffset() throws Exception {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        int result = base64InputStream.read(buffer, 1, 2);
        assertEquals(2, result);
        assertEquals(65, buffer[1]);
        assertEquals(66, buffer[2]);
    }

    @Test
    public void testReadWithInvalidOffset() throws Exception {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        try {
            base64InputStream.read(buffer, -1, 2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadWithInvalidLen() throws Exception {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadWithInvalidBufferSize() throws Exception {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1]; // Invalid buffer size
        try {
            base64InputStream.read(buffer, 0, 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadWithEmptyBuffer() throws Exception {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[0];
        int result = base64InputStream.read(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertEquals(false, base64InputStream.markSupported());
    }
}