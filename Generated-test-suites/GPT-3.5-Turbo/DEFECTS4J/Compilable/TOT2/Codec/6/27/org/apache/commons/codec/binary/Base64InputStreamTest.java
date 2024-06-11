package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStreamRead() throws IOException {
        byte[] inputBytes = {65, 66, 67, 68, 69}; // ASCII values for A, B, C, D, E
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertEquals(65, base64InputStream.read()); // Testing read method for first byte
    }

    @Test
    public void testBase64InputStreamReadArray() throws IOException {
        byte[] inputBytes = {65, 66, 67, 68, 69}; // ASCII values for A, B, C, D, E
        byte[] outputBytes = new byte[3];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        base64InputStream.read(outputBytes, 0, 3); // Testing read method for reading a array of bytes
        assertArrayEquals(new byte[]{65, 66, 67}, outputBytes); // Expected output is ABC
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStreamReadArrayWithNull() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));

        base64InputStream.read(null, 0, 3); // Passing null array should throw NullPointerException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStreamReadArrayWithInvalidOffset() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));

        base64InputStream.read(new byte[3], 4, 3); // Invalid offset throws IndexOutOfBoundsException
    }

    @Test
    public void testBase64InputStreamMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));

        assertFalse(base64InputStream.markSupported()); // mark is not supported, expected false
    }
}