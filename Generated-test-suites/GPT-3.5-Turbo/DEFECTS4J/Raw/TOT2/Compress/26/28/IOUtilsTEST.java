import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopyWithDefaultBufferSize() {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            long count = IOUtils.copy(input, output);
            assertEquals("Bytes copied should match input length", inputBytes.length, count);
            assertEquals("Output stream should match input stream", new String(inputBytes), output.toString());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCopyWithCustomBufferSize() {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            long count = IOUtils.copy(input, output, 2);
            assertEquals("Bytes copied should match input length", inputBytes.length, count);
            assertEquals("Output stream should match input stream", new String(inputBytes), output.toString());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        try {
            long skipped = IOUtils.skip(input, 2);
            assertEquals("Bytes skipped should match requested number", 2, skipped);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFully() {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[4];
        try {
            int bytesRead = IOUtils.readFully(input, outputBytes);
            assertEquals("Bytes read should match input length", inputBytes.length, bytesRead);
            assertArrayEquals("Output buffer should match input bytes", inputBytes, outputBytes);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() {
        byte[] inputBytes = "test".getBytes();
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[4];
        try {
            int bytesRead = IOUtils.readFully(input, outputBytes, 1, 2);
            assertEquals("Bytes read should match requested length", 2, bytesRead);
            assertArrayEquals("Output buffer should match input bytes", new byte[]{0, 't', 'e', 0}, outputBytes);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testToByteArray() {
        String testString = "test";
        InputStream input = new ByteArrayInputStream(testString.getBytes());
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertArrayEquals("Result should match input bytes", testString.getBytes(), result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCloseQuietly() {
        // Test with a dummy Closeable that does nothing
        Closeable dummyCloseable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        // No exception should be thrown
        IOUtils.closeQuietly(dummyCloseable);
    }
}