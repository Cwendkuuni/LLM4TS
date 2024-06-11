import static org.junit.Assert.*;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        // Typical use case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Executing the method
        long bytesCopied = IOUtils.copy(input, output);

        // Verifying the result
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, bytesCopied);
    }

    @Test
    public void testCopyWithBufferSize() {
        // Edge case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        // Executing the method
        int bufferSize = 2;
        long bytesCopied = IOUtils.copy(input, output, bufferSize);

        // Verifying the result
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, bytesCopied);
    }

    @Test
    public void testSkip() {
        // Typical use case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        // Executing the method
        long bytesSkipped = IOUtils.skip(input, 4);

        // Verifying the result
        assertEquals(4, bytesSkipped);
    }

    @Test
    public void testSkipNotEnoughBytes() {
        // Edge case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        // Executing the method
        long bytesSkipped = IOUtils.skip(input, 20);

        // Verifying the result
        assertEquals(inputBytes.length, bytesSkipped);
    }

    @Test
    public void testReadFully() {
        // Typical use case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[5];

        // Executing the method
        int bytesRead = IOUtils.readFully(input, buffer);

        // Verifying the result
        assertArrayEquals(inputBytes, buffer);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testReadFullyNotEnoughBytes() {
        // Edge case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[20];

        // Executing the method
        int bytesRead = IOUtils.readFully(input, buffer, 0, 20);

        // Verifying the result
        assertArrayEquals(inputBytes, buffer);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testToByteArray() {
        // Typical use case
        // Setting up the test
        byte[] inputBytes = "test input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        // Executing the method
        byte[] result = IOUtils.toByteArray(input);

        // Verifying the result
        assertArrayEquals(inputBytes, result);
    }

    @Test
    public void testCloseQuietly() {
        // Error scenario
        // Setting up the test
        // Closing a null stream should not throw an exception
        Closeable closeable = null;

        // Executing the method
        IOUtils.closeQuietly(closeable);
    }
}