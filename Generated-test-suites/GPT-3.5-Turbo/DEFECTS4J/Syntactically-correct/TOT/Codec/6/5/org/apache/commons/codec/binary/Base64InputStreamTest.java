import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream("Hello, World!".getBytes(StandardCharsets.UTF_8));
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead_oneByte() throws IOException {
        int result = base64InputStream.read();
        assertEquals(72, result);
    }

    @Test
    public void testRead_multipleBytes() throws IOException {
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 0, 10);

        assertEquals(11, result);
        assertArrayEquals("SGVsbG8sIFdvcmxkIQ==".getBytes(StandardCharsets.UTF_8), buffer);
    }

    @Test
    public void testRead_emptyBuffer() throws IOException {
        byte[] buffer = new byte[0];
        int result = base64InputStream.read(buffer, 0, 0);

        assertEquals(0, result);
    }

    @Test
    public void testRead_nullBuffer_shouldThrowException() {
        byte[] buffer = null;

        assertThrows(NullPointerException.class, () -> {
            base64InputStream.read(buffer, 0, 10);
        });
    }

    @Test
    public void testRead_negativeOffset_shouldThrowException() {
        byte[] buffer = new byte[10];

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, -1, 10);
        });
    }

    @Test
    public void testRead_negativeLength_shouldThrowException() {
        byte[] buffer = new byte[10];

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, 0, -1);
        });
    }

    @Test
    public void testRead_offsetOutOfBounds_shouldThrowException() {
        byte[] buffer = new byte[10];

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(buffer, 11, 5);
        });
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testRead_multipleReads() throws IOException {
        byte[] buffer = new byte[10];
        int result1 = base64InputStream.read(buffer, 0, 5);
        int result2 = base64InputStream.read(buffer, 5, 5);

        assertEquals(5, result1);
        assertEquals(5, result2);
        assertArrayEquals("SGVsbG8sIFdv".getBytes(StandardCharsets.UTF_8), buffer);
    }

    @Test
    public void testRead_noData_shouldReturnNegativeOne() throws IOException {
        byte[] buffer = new byte[10];
        // Consume all data from the input stream
        while (base64InputStream.read(buffer, 0, 10) > 0) {
        }
        int result = base64InputStream.read(buffer, 0, 10);

        assertEquals(-1, result);
    }

    @Test
    public void testRead_largeBuffer() throws IOException {
        byte[] buffer = new byte[100];
        int result = base64InputStream.read(buffer, 0, 100);

        assertEquals(22, result);
        assertArrayEquals("SGVsbG8sIFdvcmxkIQ==\n".getBytes(StandardCharsets.UTF_8), buffer);
    }

    // Add more test cases as needed

}