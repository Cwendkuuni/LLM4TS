package org.apache.commons.codec.binary;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();
        
        assertEquals('H', result);
    }

    @Test
    public void testRead_withEmptyStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int result = base64InputStream.read();
        
        assertEquals(-1, result);
    }

    @Test
    public void testRead_withEmptyByteArray() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[0];
        int bytesRead = base64InputStream.read(result);
        
        assertEquals(0, bytesRead);
    }

    @Test
    public void testRead_withOffsetAndLength() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[5];
        int bytesRead = base64InputStream.read(result, 1, 3);
        
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 'H', 'e', 'l', 0}, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidOffset() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[5];
        base64InputStream.read(result, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidLength() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[5];
        base64InputStream.read(result, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_withInvalidArraySize() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[10];
        base64InputStream.read(result, 0, 5);
    }

    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        boolean result = base64InputStream.markSupported();
        
        assertFalse(result);
    }
}