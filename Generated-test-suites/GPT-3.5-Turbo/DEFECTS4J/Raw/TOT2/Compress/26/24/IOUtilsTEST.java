import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopyBasic() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, count);
    }

    @Test
    public void testCopyEmptyStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(0, output.size());
        assertEquals(0, count);
    }

    @Test
    public void testSkipZeroBytes() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 0);
        assertEquals(0, skipped);
    }

    @Test
    public void testSkipPartialSkipped() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        long skipped = IOUtils.skip(input, 3);
        assertEquals(3, skipped);
    }

    @Test
    public void testReadFullyBasic() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[inputBytes.length];
        int bytesRead = IOUtils.readFully(input, output);
        assertArrayEquals(inputBytes, output);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testReadFullyPartialRead() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] output = new byte[inputBytes.length + 1];
        int bytesRead = IOUtils.readFully(input, output, 0, inputBytes.length + 1);
        assertArrayEquals(inputBytes, output);
        assertEquals(inputBytes.length, bytesRead);
    }

    @Test
    public void testToByteArrayBasic() throws IOException {
        byte[] inputBytes = {1, 2, 3, 4, 5};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testToByteArrayEmptyStream() throws IOException {
        byte[] inputBytes = {};
        InputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietlyWithNull() {
        IOUtils.closeQuietly(null);
        assertTrue(true);  // No exception should be thrown
    }

    @Test
    public void testCloseQuietlyWithException() {
        CloseableMock c = new CloseableMock();
        assertThrows(IOException.class, () -> {
            IOUtils.closeQuietly(c);
        });
    }

    private class CloseableMock implements Closeable {
        @Override
        public void close() throws IOException {
            throw new IOException();
        }
    }
}