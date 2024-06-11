
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class Base64InputStreamTest {
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] data = "Hello, World!".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('o', base64InputStream.read());
        assertEquals(',', base64InputStream.read());
        assertEquals(' ', base64InputStream.read());
        assertEquals('W', base64InputStream.read());
        assertEquals('o', base64InputStream.read());
        assertEquals('r', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('d', base64InputStream.read());
        assertEquals('!', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(5, base64InputStream.read(buffer, 0, 5));
        assertArrayEquals("Hello".getBytes(), buffer);

        byte[] buffer2 = new byte[7];
        assertEquals(5, base64InputStream.read(buffer2, 0, 7));
        assertArrayEquals(", Worl".getBytes(), buffer2);

        byte[] buffer3 = new byte[5];
        assertEquals(4, base64InputStream.read(buffer3, 0, 5));
        assertArrayEquals("d!".getBytes(), buffer3);

        assertEquals(-1, base64InputStream.read(buffer3, 0, 5));
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }
}
