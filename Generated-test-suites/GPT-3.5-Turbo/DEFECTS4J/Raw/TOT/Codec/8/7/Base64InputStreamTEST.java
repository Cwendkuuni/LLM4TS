import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    // Test for read() method

    @Test
    public void testReadOneByte() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        assertEquals(72, result);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        base64InputStream.read(result, 0, 5);
        assertArrayEquals(new byte[]{72, 101, 108, 108, 111}, result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        base64InputStream.read(result, 1, 3);
        assertArrayEquals(new byte[]{0, 72, 101, 108, 0}, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        base64InputStream.read(result, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[5];
        base64InputStream.read(result, 0, 10);
    }

    // Additional tests for other methods

    @Test
    public void testDoEncode() {
        byte[] input = "Hello World".getBytes();
        InputStream inputStream = new ByteArrayInputStream(input);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        assertEquals(true, base64InputStream.doEncode);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionInConstructor() throws IOException {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Mock IOException");
            }
        };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(); // This should throw IOException
    }

    // More tests can be added here to cover all scenarios

}