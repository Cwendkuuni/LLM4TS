package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;
    private InputStream input;

    @Before
    public void setUp() {
        input = new ByteArrayInputStream("Hello World!".getBytes());
        base64InputStream = new Base64InputStream(input);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(72, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 0, 5);
        assertEquals(5, result);
        assertEquals(72, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(108, buffer[2]);
        assertEquals(108, buffer[3]);
        assertEquals(111, buffer[4]);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 5, 5);
        assertEquals(5, result);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(72, buffer[5]);
        assertEquals(101, buffer[6]);
        assertEquals(108, buffer[7]);
        assertEquals(108, buffer[8]);
        assertEquals(111, buffer[9]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 11, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 11);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}