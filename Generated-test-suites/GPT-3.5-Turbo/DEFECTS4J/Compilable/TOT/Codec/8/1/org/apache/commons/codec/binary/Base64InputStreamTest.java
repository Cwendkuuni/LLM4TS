package org.apache.commons.codec.binary;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class Base64InputStreamTest {

    // Test read() method
    @Test
    public void testRead() throws IOException {
        byte[] input = {71, 101, 101, 107, 115};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        int result = base64InputStream.read();
        assertEquals(71, result);

        // Test read() when EOF is reached
        result = base64InputStream.read();
        assertEquals(-1, result);
    }

    // Test read(byte[] b, int offset, int len) method
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] input = {71, 101, 101, 107, 115};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        byte[] buffer = new byte[3];
        int result = base64InputStream.read(buffer, 0, 3);
        assertEquals(3, result);
        assertEquals(71, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(101, buffer[2]);

        // Test read(byte[] b, int offset, int len) when EOF is reached
        result = base64InputStream.read(buffer, 0, 3);
        assertEquals(-1, result);
    }

    // Test read(byte[] b, int offset, int len) method with null byte array
    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(null, 0, 3);
    }

    // Test read(byte[] b, int offset, int len) method with invalid offset
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(new byte[3], -1, 3);
    }

    // Test read(byte[] b, int offset, int len) method with invalid length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(new byte[3], 0, -1);
    }

    // Test read(byte[] b, int offset, int len) method with invalid buffer size
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        base64InputStream.read(new byte[5], 0, 5);
    }

    // Test markSupported() method
    @Test
    public void testMarkSupported() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(byteArrayInputStream);
        assertFalse(base64InputStream.markSupported());
    }
}