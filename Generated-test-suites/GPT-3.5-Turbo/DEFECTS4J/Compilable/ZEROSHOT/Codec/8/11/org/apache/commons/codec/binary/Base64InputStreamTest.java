package org.apache.commons.codec.binary;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] data = "Hello World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(72, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 0, 5);
        assertEquals(5, result);
        assertEquals("Hello".getBytes(), buffer);
    }

    @Test
    public void testReadFullBuffer() throws IOException {
        byte[] buffer = new byte[12];
        int result = base64InputStream.read(buffer, 0, 12);
        assertEquals(12, result);
        assertEquals("Hello World!".getBytes(), buffer);
    }

    @Test
    public void testReadPartialBuffer() throws IOException {
        byte[] buffer = new byte[8];
        int result = base64InputStream.read(buffer, 0, 8);
        assertEquals(8, result);
        assertEquals("Hello Wo".getBytes(), buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBuffer() throws IOException {
        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 0, 10);
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertEquals(false, result);
    }
}