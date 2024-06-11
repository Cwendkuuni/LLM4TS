
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    // Test read() method
    @Test
    public void testReadMethod() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertEquals('H', base64InputStream.read());
        assertEquals('e', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('o', base64InputStream.read());
        assertEquals(' ', base64InputStream.read());
        assertEquals('W', base64InputStream.read());
        assertEquals('o', base64InputStream.read());
        assertEquals('r', base64InputStream.read());
        assertEquals('l', base64InputStream.read());
        assertEquals('d', base64InputStream.read());
        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    // Test read(byte[], int, int) method
    @Test
    public void testReadWithByteArrayMethod() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[6];
        assertEquals(6, base64InputStream.read(buffer, 0, 6));
        assertArrayEquals("Hello ".getBytes(), buffer);
        assertEquals(5, base64InputStream.read(buffer, 0, 6));
        assertArrayEquals("World ".getBytes(), buffer);
        assertEquals(-1, base64InputStream.read(buffer, 0, 6));
        base64InputStream.close();
    }

    // Test markSupported() method
    @Test
    public void testMarkSupportedMethod() {
        InputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
        base64InputStream.close();
    }
}
