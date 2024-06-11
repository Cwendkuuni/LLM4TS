package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Base64InputStreamTest {
    private Base64InputStream inputStream;

    @Before
    public void setUp() {
        byte[] inputBytes = "Test String".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes);
        inputStream = new Base64InputStream(byteArrayInputStream);
    }

    @Test
    public void testRead() throws Exception {
        int expected = 84; // ASCII value of 'T'
        int actual = inputStream.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBytes() throws Exception {
        byte[] expected = "Test String".getBytes();
        byte[] actual = new byte[expected.length];
        inputStream.read(actual, 0, expected.length);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testReadThrowsNullPointerException() throws Exception {
        byte[] bytes = null;
        inputStream.read(bytes, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadThrowsIndexOutOfBoundsException() throws Exception {
        byte[] bytes = new byte[5];
        int offset = -1;
        int len = 5;
        inputStream.read(bytes, offset, len);
    }

    @Test
    public void testReadReturnsZero() throws Exception {
        byte[] bytes = new byte[5];
        int offset = 0;
        int len = 0;
        int actual = inputStream.read(bytes, offset, len);
        assertEquals(0, actual);
    }

    @Test
    public void testMarkSupported() {
        boolean actual = inputStream.markSupported();
        assertFalse(actual);
    }
}