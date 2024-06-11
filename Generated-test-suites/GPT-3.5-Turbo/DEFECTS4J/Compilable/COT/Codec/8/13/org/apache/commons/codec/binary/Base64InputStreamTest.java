package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private static final String TEST_STRING = "Hello, World!";
    private static final byte[] TEST_STRING_BYTES = TEST_STRING.getBytes();

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(TEST_STRING_BYTES);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals((int) 'H', result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer);
        assertArrayEquals("Hello".getBytes(), buffer);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = base64InputStream.read(buffer, 2, 5);
        assertArrayEquals(new byte[]{0, 0, 'H', 'e', 'l', 'l', 'o', 0, 0, 0}, buffer);
        assertEquals(5, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 4, 2);
    }

    @Test
    public void testReadZeroLengthByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 2, 0);
        assertArrayEquals(new byte[]{0, 0, 'H', 'e', 'l'}, buffer);
        assertEquals(0, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}