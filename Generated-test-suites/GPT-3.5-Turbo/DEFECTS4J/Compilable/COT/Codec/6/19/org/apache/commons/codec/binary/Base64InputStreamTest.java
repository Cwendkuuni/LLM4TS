package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        int result = base64InputStream.read();
        assertEquals('H', result);
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 0, 5);
        assertEquals(5, result);
        assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test
    public void testReadWithInvalidOffset() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[5];
        try {
            base64InputStream.read(buffer, -1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception is expected
        }
    }

    @Test
    public void testReadWithInvalidLength() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[5];
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception is expected
        }
    }

    @Test
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] input = "Hello World!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[10];
        try {
            base64InputStream.read(buffer, 0, 15);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception is expected
        }
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("Hello World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        assertFalse(base64InputStream.markSupported());
    }

}