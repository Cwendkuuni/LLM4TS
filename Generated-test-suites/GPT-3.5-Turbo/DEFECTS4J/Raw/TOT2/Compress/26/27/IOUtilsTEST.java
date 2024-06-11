import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        byte[] inputData = "Test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output);

        assertEquals(inputData.length, bytesCopied);
        assertArrayEquals(inputData, output.toByteArray());
    }

    @Test
    public void testCopyCustomBufferSize() throws IOException {
        byte[] inputData = "Custom buffer size test".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 10;
        long bytesCopied = IOUtils.copy(input, output, bufferSize);

        assertEquals(inputData.length, bytesCopied);
        assertArrayEquals(inputData, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = "Test skip method".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        long bytesSkipped = IOUtils.skip(input, 5);

        assertEquals(5, bytesSkipped);

        byte[] remainingData = new byte[data.length - 5];
        input.read(remainingData);

        assertArrayEquals("skip method".getBytes(), remainingData);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] data = "Test readFully method".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        byte[] buffer = new byte[data.length];

        int bytesRead = IOUtils.readFully(input, buffer);

        assertEquals(data.length, bytesRead);
        assertArrayEquals(data, buffer);
    }

    @Test
    public void testCloseQuietly() {
        // Test closing a Closeable object
        CloseableMock closeableMock = new CloseableMock();
        IOUtils.closeQuietly(closeableMock);
        assertTrue(closeableMock.isClosed());

        // Test closing a null object
        IOUtils.closeQuietly(null); // Should not throw an exception
    }

    // Mock Closeable class for testing
    private class CloseableMock implements Closeable {
        private boolean closed = false;

        @Override
        public void close() throws IOException {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}