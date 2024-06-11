import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    // Test copy method with default buffer size
    @Test
    public void testCopy() {
        // Create a sample input stream and output stream
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            IOUtils.copy(input, output);
        } catch (IOException e) {
            // Failed to copy
            fail("Failed to copy the input to output");
        }

        // Check if the output matches the input
        assertEquals("test", output.toString());
    }

    // Test copy method with specified buffer size
    @Test
    public void testCopyWithBufferSize() {
        // Create a sample input stream and output stream
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            IOUtils.copy(input, output, 100);
        } catch (IOException e) {
            // Failed to copy
            fail("Failed to copy the input to output");
        }

        // Check if the output matches the input
        assertEquals("test", output.toString());
    }

    // Test skip method
    @Test
    public void testSkip() {
        // Create a sample input stream
        InputStream input = new ByteArrayInputStream("test".getBytes());

        try {
            long count = IOUtils.skip(input, 2);
            assertEquals(2, count);
        } catch (IOException e) {
            // Failed to skip
            fail("Failed to skip bytes");
        }
    }

    // Test readFully method
    @Test
    public void testReadFully() {
        // Create a sample input stream and buffer
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];

        try {
            int count = IOUtils.readFully(input, buffer);
            assertEquals(4, count);
        } catch (IOException e) {
            // Failed to read fully
            fail("Failed to read fully");
        }
    }

    // Test toByteArray method
    @Test
    public void testToByteArray() {
        // Create a sample input stream
        InputStream input = new ByteArrayInputStream("test".getBytes());

        try {
            byte[] byteArray = IOUtils.toByteArray(input);
            assertArrayEquals("test".getBytes(), byteArray);
        } catch (IOException e) {
            // Failed to convert to byte array
            fail("Failed to convert to byte array");
        }
    }

    // Test closeQuietly method
    @Test
    public void testCloseQuietly() {
        // Create a sample closeable object
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };

        // Perform the test
        IOUtils.closeQuietly(closeable);
    }

}