import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    // Test case for copy(InputStream, OutputStream)
    @Test
    public void testCopy() {
        // Typical use case: copying input stream to output stream
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        try {
            long result = IOUtils.copy(input, output);
            assertEquals("Copy count did not match expected value", 13L, result);
            assertEquals("Output stream contents did not match expected value", "Hello, World!", output.toString());
        } catch (IOException e) {
            fail("Failed to copy input stream to output stream: " + e.getMessage());
        }
    }

    // Test case for copy(InputStream, OutputStream, int)
    @Test
    public void testCopyWithBufferSize() {
        // Typical use case: copying input stream to output stream with specified buffer size
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        try {
            long result = IOUtils.copy(input, output, 8);
            assertEquals("Copy count did not match expected value", 13L, result);
            assertEquals("Output stream contents did not match expected value", "Hello, World!", output.toString());
        } catch (IOException e) {
            fail("Failed to copy input stream to output stream with specified buffer size: " + e.getMessage());
        }
    }

    // Test case for skip(InputStream, long)
    @Test
    public void testSkip() {
        // Typical use case: skip specified number of bytes in input stream
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        try {
            long result = IOUtils.skip(input, 7);
            assertEquals("Bytes skipped did not match expected value", 7L, result);

            // Verify that skipped bytes are no longer in the input stream
            byte[] buffer = new byte[1024];
            int bytesRead = input.read(buffer);
            assertEquals("Byte read count did not match expected value", 6, bytesRead);
            assertEquals("Input stream contents after skip did not match expected value", "World!", new String(buffer, 0, bytesRead));
        } catch (IOException e) {
            fail("Failed to skip bytes in input stream: " + e.getMessage());
        }
    }

    // Test case for readFully(InputStream, byte[])
    @Test
    public void testReadFully() {
        // Typical use case: read as much from input stream as possible to fill given array
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[7];
        try {
            int result = IOUtils.readFully(input, buffer);
            assertEquals("Byte read count did not match expected value", 7, result);
            assertEquals("Buffer contents did not match expected value", "Hello, ", new String(buffer));
        } catch (IOException e) {
            fail("Failed to read from input stream to fill given array: " + e.getMessage());
        }
    }

    // Test case for readFully(InputStream, byte[], int, int)
    @Test
    public void testReadFullyWithOffsetAndLength() {
        // Typical use case: read as much from input stream as possible to fill given array with specified offset and length
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[14];
        try {
            int result = IOUtils.readFully(input, buffer, 6, 8);
            assertEquals("Byte read count did not match expected value", 8, result);
            assertEquals("Buffer contents did not match expected value", "World!", new String(buffer, 6, 8));
        } catch (IOException e) {
            fail("Failed to read from input stream to fill given array with specified offset and length: " + e.getMessage());
        }
    }

    // Test case for toByteArray(InputStream)
    @Test
    public void testToByteArray() {
        // Typical use case: get the contents of input stream as byte array
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        try {
            byte[] result = IOUtils.toByteArray(input);
            assertArrayEquals("Byte array contents did not match expected value", "Hello, World!".getBytes(), result);
        } catch (IOException e) {
            fail("Failed to get contents of input stream as byte array: " + e.getMessage());
        }
    }

    // Test case for closeQuietly(Closeable)
    @Test
    public void testCloseQuietly() {
        // Typical use case: close a closeable without throwing IOException
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // do nothing
            }
        };
        IOUtils.closeQuietly(closeable);

        // No failure implies success
    }

}