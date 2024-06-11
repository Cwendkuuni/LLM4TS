package org.apache.commons.codec.binary;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.codec.binary.Base64InputStream;
import java.io.*;

public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() throws Exception {
        // Set up the input stream and base64 input stream
        byte[] bytes = "Hello, World!".getBytes();
        inputStream = new ByteArrayInputStream(bytes);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', base64InputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer);
        assertEquals(5, bytesRead);
        assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 'H', 'e', 'l', 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        base64InputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeLength() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 1, -3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 10, 3);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(1, base64InputStream.available());
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(0, base64InputStream.skip(100));
    }

    @Test
    public void testRead_eof() throws IOException {
        base64InputStream.read();
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testMergeAndComplete() {
        // Test merge and complete test cases
    }
    
}
