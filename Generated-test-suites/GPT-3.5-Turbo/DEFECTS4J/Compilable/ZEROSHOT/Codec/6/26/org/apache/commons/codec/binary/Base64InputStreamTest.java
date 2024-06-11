package org.apache.commons.codec.binary;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputData = "Hello, world!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        
        int result = base64InputStream.read();
        
        assertFalse(result == -1);
        assertEquals('H', (char) result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] inputData = "Hello, world!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] outputData = new byte[5];
        
        int result = base64InputStream.read(outputData, 0, 5);
        
        assertFalse(result == -1);
        assertEquals("Hello", new String(outputData));
    }

    @Test
    public void testReadWithInvalidOffset() throws IOException {
        byte[] inputData = "Hello, world!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] outputData = new byte[5];
        
        int result = base64InputStream.read(outputData, -1, 5);

        assertEquals(-1, result);
    }

    @Test
    public void testReadWithInvalidLen() throws IOException {
        byte[] inputData = "Hello, world!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] outputData = new byte[5];
        
        int result = base64InputStream.read(outputData, 0, -1);

        assertEquals(-1, result);
    }

    @Test
    public void testReadWithInvalidBuffer() throws IOException {
        byte[] inputData = "Hello, world!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        
        int result = base64InputStream.read(null, 0, 5);

        assertEquals(-1, result);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        byte[] inputData = "Hello, world!".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] outputData = new byte[0];
        
        int result = base64InputStream.read(outputData, 0, 0);

        assertEquals(0, result);
    }

    @Test
    public void testReadWithNoData() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] outputData = new byte[5];
        
        int result = base64InputStream.read(outputData, 0, 5);

        assertEquals(-1, result);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);

        boolean result = base64InputStream.markSupported();

        assertFalse(result);
    }
}