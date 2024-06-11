import org.apache.commons.compress.utils.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = new byte[] { 1, 2, 3, 4, 5 };
        byte[] outputData = new byte[] { 6, 7, 8, 9, 10 };

        InputStream inputStream = new ByteArrayInputStream(inputData);
        OutputStream outputStream = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        byte[] outputArray = outputStream.toByteArray();

        // Test if correct number of bytes are copied
        assertEquals(inputData.length, copiedBytes);

        // Test if data is copied correctly
        assertArrayEquals(inputData, outputArray);

        // Test edge case: InputStream and OutputStream are the same
        InputStream inputSameOutput = new ByteArrayInputStream(inputData);
        OutputStream outputSameInput = new ByteArrayOutputStream();
        IOUtils.copy(inputSameOutput, outputSameInput);
        assertArrayEquals(inputData, outputSameInput.toByteArray());
        assertEquals(inputData.length, IOUtils.copy(inputSameOutput, outputSameInput));
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = new byte[] { 1, 2, 3, 4, 5 };

        InputStream inputStream = new ByteArrayInputStream(inputData);

        // Test if correct number of bytes are skipped
        long skippedBytes = IOUtils.skip(inputStream, 5);
        long expectedSkippedBytes = inputData.length - 5;
        assertEquals(expectedSkippedBytes, skippedBytes);

        // Test edge case: Negative number of bytes to skip
        skippedBytes = IOUtils.skip(inputStream, -5);
        assertEquals(0, skippedBytes);

        // Test edge case: Number of bytes to skip greater than available bytes
        skippedBytes = IOUtils.skip(inputStream, inputData.length + 1);
        assertEquals(inputData.length, skippedBytes);
    }

    @Test
    public void testCopyWithIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Error test".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();

        try {
            IOUtils.copy(inputStream, outputStream);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}