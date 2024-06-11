package org.apache.commons.codec.binary;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] testBytes = "Test String".getBytes();
        ByteArrayInputStream inputstream = new ByteArrayInputStream(testBytes);
        base64InputStream = new Base64InputStream(inputstream);
    }

    @Test
    public void testRead_oneByte() throws IOException {
        int byteRead = base64InputStream.read();
        assertEquals((int) 'T', byteRead);
    }

    @Test
    public void testRead_multipleBytes() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, 5);
        assertArrayEquals("Test ".getBytes(), buffer);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testRead_eof() throws IOException {
        byte[] buffer = new byte[50];
        int bytesRead = base64InputStream.read(buffer, 0, 50);
        assertEquals(-1, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullBuffer() throws IOException {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 6, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidLength() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 0, 10);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

}