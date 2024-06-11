import org.junit.Test;
import java.io.*;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        int data;
        StringBuilder result = new StringBuilder();
        while ((data = base64InputStream.read()) != -1) {
            result.append((char) data);
        }

        assertEquals("Hello, World!", result.toString());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        int bytesRead = base64InputStream.read(buffer, 0, buffer.length);

        assertEquals(5, bytesRead);
        assertArrayEquals("Hello".getBytes(), buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 11);
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream("Hello, World!".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(in);

        assertFalse(base64InputStream.markSupported());
    }
}