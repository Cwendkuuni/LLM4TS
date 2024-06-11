package org.apache.commons.codec.binary;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    private Base64InputStream inputStream;

    @Before
    public void setUp() {
        byte[] data = "Test String".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new Base64InputStream(byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('T', inputStream.read());
        assertEquals('e', inputStream.read());
        assertEquals('s', inputStream.read());
        assertEquals('t', inputStream.read());
        assertEquals(' ', inputStream.read());
        assertEquals('S', inputStream.read());
        assertEquals('t', inputStream.read());
        assertEquals('r', inputStream.read());
        assertEquals('i', inputStream.read());
        assertEquals('n', inputStream.read());
        assertEquals('g', inputStream.read());
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer, 0, 5);
        assertArrayEquals(new byte[]{84, 101, 115, 116, 32}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullArray() throws IOException {
        inputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer, 6, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLen() throws IOException {
        byte[] buffer = new byte[5];
        inputStream.read(buffer, 0, -1);
    }

    @Test
    public void testReadEndOfFile() throws IOException {
        byte[] buffer = new byte[50];
        int bytesRead = inputStream.read(buffer, 0, 50);
        assertEquals(-1, bytesRead);
    }

}