import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void testCopyBasic() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output);
        
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, bytesCopied);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long bytesCopied = IOUtils.copy(input, output, 3);
        
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, bytesCopied);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        long bytesSkipped = IOUtils.skip(input, 3);
        
        assertEquals(3, bytesSkipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[inputBytes.length];
        
        int bytesRead = IOUtils.readFully(input, output);
        
        assertArrayEquals(inputBytes, output);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[inputBytes.length];
        
        int bytesRead = IOUtils.readFully(input, output, 1, 3);
        
        byte[] expectedOutput = {0, 1, 2, 3, 0};
        
        assertArrayEquals(expectedOutput, output);
        assertEquals(3, bytesRead);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] outputBytes = IOUtils.toByteArray(input);
        
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        // Test with non-null Closeable
        CloseableMock mock = new CloseableMock();
        IOUtils.closeQuietly(mock);
        assertTrue(mock.isClosed());

        // Test with null Closeable
        IOUtils.closeQuietly(null);
    }

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