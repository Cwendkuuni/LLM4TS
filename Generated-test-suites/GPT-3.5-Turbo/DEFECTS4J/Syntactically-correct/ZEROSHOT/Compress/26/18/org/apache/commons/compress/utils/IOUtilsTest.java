import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        long result = IOUtils.copy(input, output);

        assertEquals(inputBytes.length, result);
        assertArrayEquals(inputBytes, ((ByteArrayOutputStream) output).toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        OutputStream output = new ByteArrayOutputStream();

        int bufferSize = 1024;
        long result = IOUtils.copy(input, output, bufferSize);

        assertEquals(inputBytes.length, result);
        assertArrayEquals(inputBytes, ((ByteArrayOutputStream) output).toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        long result = IOUtils.skip(input, 7);

        assertEquals(7, result);
        assertEquals('W', input.read());
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int result = IOUtils.readFully(input, outputBytes);

        assertEquals(5, result);
        assertArrayEquals("Hello".getBytes(), outputBytes);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int result = IOUtils.readFully(input, outputBytes, 1, 3);

        assertEquals(3, result);
        assertArrayEquals("ell".getBytes(), outputBytes);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        byte[] result = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, result);
    }

    @Test
    public void testCloseQuietly() {
        // Create a mock Closeable that doesn't throw an exception
        Closeable mockCloseable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };

        IOUtils.closeQuietly(mockCloseable);
        // No assert needed, just checking that it doesn't throw an exception
    }
}