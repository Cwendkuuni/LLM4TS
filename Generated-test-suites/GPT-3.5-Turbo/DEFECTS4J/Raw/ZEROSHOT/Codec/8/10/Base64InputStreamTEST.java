import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream stream = new Base64InputStream(input);

        int result = stream.read();

        assertEquals(72, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        Base64InputStream stream = new Base64InputStream(input);

        byte[] buffer = new byte[5];
        int result = stream.read(buffer, 1, 3);

        assertEquals(3, result);
        assertArrayEquals(new byte[]{0, 72, 101, 108, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream stream = new Base64InputStream(input);

        stream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream stream = new Base64InputStream(input);

        stream.read(new byte[10], -1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream stream = new Base64InputStream(input);

        stream.read(new byte[10], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream stream = new Base64InputStream(input);

        stream.read(new byte[5], 0, 10);
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream stream = new Base64InputStream(input);

        boolean result = stream.markSupported();

        assertFalse(result);
    }
}