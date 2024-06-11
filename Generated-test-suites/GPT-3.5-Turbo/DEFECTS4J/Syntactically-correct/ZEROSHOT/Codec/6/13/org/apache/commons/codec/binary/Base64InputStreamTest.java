import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        base64InputStream.close();
        assertEquals(72, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 1, 3);
        base64InputStream.close();
        assertEquals(3, result);
        assertEquals(72, buffer[1]);
        assertEquals(101, buffer[2]);
        assertEquals(108, buffer[3]);
    }

    @Test
    public void testReadWithDataToRead() throws IOException {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[20];
        int result = base64InputStream.read(buffer, 0, buffer.length);
        base64InputStream.close();
        assertEquals(13, result);
        assertEquals(72, buffer[0]);
        assertEquals(101, buffer[1]);
        assertEquals(108, buffer[2]);
        assertEquals(108, buffer[3]);
        assertEquals(111, buffer[4]);
        assertEquals(44, buffer[5]);
        assertEquals(32, buffer[6]);
        assertEquals(87, buffer[7]);
        assertEquals(111, buffer[8]);
        assertEquals(114, buffer[9]);
        assertEquals(108, buffer[10]);
        assertEquals(100, buffer[11]);
        assertEquals(33, buffer[12]);
    }

    @Test
    public void testMarkSupported() {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        boolean result = base64InputStream.markSupported();
        base64InputStream.close();
        assertFalse(result);
    }

    @Test
    public void testReadWithNullArray() {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = null;
        try {
            base64InputStream.read(buffer, 0, 10);
        } catch (NullPointerException e) {
            assertEquals("null", e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidOffset() {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        try {
            base64InputStream.read(buffer, -1, 10);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(null, e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidLength() {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        try {
            base64InputStream.read(buffer, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(null, e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidBufferSize() {
        byte[] data = "Hello, World!".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        try {
            base64InputStream.read(buffer, 0, 10);
        } catch (IndexOutOfBoundsException e) {
            assertEquals(null, e.getMessage());
        }
    }
}