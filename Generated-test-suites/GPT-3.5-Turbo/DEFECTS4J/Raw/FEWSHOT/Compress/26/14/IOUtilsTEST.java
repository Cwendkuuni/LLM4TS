import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopyUsingDefaultBufferSize() throws Exception {
        // Test input
        String inputValue = "This is a test input string.";
        byte[] inputBuffer = inputValue.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer);

        // Test output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Perform copy
        IOUtils.copy(inputStream, outputStream);

        // Check result
        byte[] outputBuffer = outputStream.toByteArray();
        assertEquals(inputBuffer.length, outputBuffer.length);
        assertArrayEquals(inputBuffer, outputBuffer);
    }

    @Test
    public void testCopyWithCustomBufferSize() throws Exception {
        // Test input
        String inputValue = "This is a test input string.";
        byte[] inputBuffer = inputValue.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer);

        // Test output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Perform copy
        int bufferSize = 10;
        IOUtils.copy(inputStream, outputStream, bufferSize);

        // Check result
        byte[] outputBuffer = outputStream.toByteArray();
        assertEquals(inputBuffer.length, outputBuffer.length);
        assertArrayEquals(inputBuffer, outputBuffer);
    }

    @Test
    public void testSkip() throws Exception {
        // Test input
        String inputValue = "This is a test input string.";
        byte[] inputBuffer = inputValue.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer);

        // Perform skip
        int numToSkip = 10;
        long numSkipped = IOUtils.skip(inputStream, numToSkip);

        // Check result
        assertEquals(numToSkip, numSkipped);
        assertEquals(inputBuffer.length - numToSkip, inputStream.available());
    }

    @Test
    public void testReadFully() throws Exception {
        // Test input
        String inputValue = "This is a test input string.";
        byte[] inputBuffer = inputValue.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer);

        // Test output
        byte[] outputBuffer = new byte[inputBuffer.length];

        // Perform readFully
        int bytesRead = IOUtils.readFully(inputStream, outputBuffer);

        // Check result
        assertEquals(inputBuffer.length, bytesRead);
        assertArrayEquals(inputBuffer, outputBuffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws Exception {
        // Test input
        String inputValue = "This is a test input string.";
        byte[] inputBuffer = inputValue.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer);

        // Test output
        byte[] outputBuffer = new byte[inputBuffer.length];

        // Perform readFully
        int offset = 5;
        int length = 10;
        int bytesRead = IOUtils.readFully(inputStream, outputBuffer, offset, length);

        // Check result
        assertEquals(length, bytesRead);
        byte[] expectedOutputBuffer = inputValue.substring(0, length).getBytes();
        for (int i = 0; i < length; i++) {
            assertEquals(expectedOutputBuffer[i], outputBuffer[offset + i]);
        }
    }

    @Test
    public void testToByteArray() throws Exception {
        // Test input
        String inputValue = "This is a test input string.";
        byte[] inputBuffer = inputValue.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBuffer);

        // Perform toByteArray
        byte[] outputBuffer = IOUtils.toByteArray(inputStream);

        // Check result
        assertArrayEquals(inputBuffer, outputBuffer);
    }

    @Test
    public void testCloseQuietly() throws Exception {
        // Test input
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test exception");
            }
        };

        // Perform closeQuietly
        IOUtils.closeQuietly(closeable);

        // Check that no exception was thrown
        assertTrue(true);
    }
}