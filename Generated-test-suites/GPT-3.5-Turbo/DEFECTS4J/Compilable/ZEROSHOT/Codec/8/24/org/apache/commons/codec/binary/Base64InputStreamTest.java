package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(result, 84);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] result = new byte[4];
        int readLen = base64InputStream.read(result, 0, 4);
        assertEquals(readLen, 4);
        assertArrayEquals(result, new byte[]{84, 101, 115, 116});
    }

    @Test
    public void testReadByteArrayInvalidOffset() throws IOException {
        byte[] result = new byte[4];
        try {
            base64InputStream.read(result, -1, 4);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testReadByteArrayInvalidLength() throws IOException {
        byte[] result = new byte[4];
        try {
            base64InputStream.read(result, 0, -1);
            fail("Should throw IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testReadByteArrayInsufficientBuffer() throws IOException {
        byte[] result = new byte[3];
        int readLen = base64InputStream.read(result, 0, 3);
        assertEquals(readLen, 3);
        assertArrayEquals(result, new byte[]{84, 101, 115});
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}