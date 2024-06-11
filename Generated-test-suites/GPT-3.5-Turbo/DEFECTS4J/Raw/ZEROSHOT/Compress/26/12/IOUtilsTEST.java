import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {
    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testCopyCustomBufferSize() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 1024;
        long count = IOUtils.copy(input, output, bufferSize);

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long skipCount = 5;
        long actualSkipped = IOUtils.skip(input, skipCount);

        assertEquals(skipCount, actualSkipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);

        assertEquals(buffer.length, bytesRead);
        assertArrayEquals("Test ".getBytes(), buffer);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer, 1, 4);

        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[] {0, 'T', 'e', 's', 't'}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] actualBytes = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, actualBytes);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        // Test closing a valid Closeable
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output);
        assertTrue(output.isClosed());

        // Test closing a null Closeable
        IOUtils.closeQuietly(null);  // Should not throw any exception
    }
}