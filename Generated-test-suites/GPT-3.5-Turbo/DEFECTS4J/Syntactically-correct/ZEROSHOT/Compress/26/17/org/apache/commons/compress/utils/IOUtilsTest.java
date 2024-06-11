import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        byte[] inputBytes = "Test input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        byte[] outputBytes = output.toByteArray();

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        byte[] inputBytes = "Test input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output, 1024);

        byte[] outputBytes = output.toByteArray();

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        long skipped = IOUtils.skip(input, 5);

        assertEquals(5, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length];

        int count = IOUtils.readFully(input, outputBytes);

        assertEquals(inputBytes.length, count);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testReadFullyWithOffset() throws IOException {
        byte[] inputBytes = "Test input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[inputBytes.length - 4];

        int count = IOUtils.readFully(input, outputBytes, 2, outputBytes.length);

        assertEquals(outputBytes.length, count);
        assertArrayEquals("st input".getBytes(), outputBytes);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test input".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);

        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        // No assertion, just test that there are no exceptions thrown
        IOUtils.closeQuietly(null);
        IOUtils.closeQuietly(new ByteArrayInputStream(new byte[0]));
    }
}