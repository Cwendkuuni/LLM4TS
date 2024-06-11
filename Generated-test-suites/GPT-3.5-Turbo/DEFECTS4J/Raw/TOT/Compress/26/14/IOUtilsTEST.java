import org.junit.Test;
import java.io.*;

import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        try {
            // Typical use case - copying from one stream to another
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long count = IOUtils.copy(input, output);
            assertEquals("Copy count should be equal to input size", 13, count);
            assertEquals("Output should be equal to input", "Hello, World!", output.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testCopyWithBufferSize() {
        try {
            // Typical use case - copying from one stream to another with a specific buffer size
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long count = IOUtils.copy(input, output, 1024);
            assertEquals("Copy count should be equal to input size", 13, count);
            assertEquals("Output should be equal to input", "Hello, World!", output.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testCopyEmptyStream() {
        try {
            // Edge case - copying from an empty stream
            ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            long count = IOUtils.copy(input, output);
            assertEquals("Copy count should be 0", 0, count);
            assertEquals("Output should be empty", "", output.toString());
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testSkip() {
        try {
            // Typical use case - skipping bytes in an input stream
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            long skipped = IOUtils.skip(input, 7);
            assertEquals("Skipped count should be equal to specified value", 7, skipped);
            assertEquals("Remaining input should be \"World!\"", "World!", new String(input.readAllBytes()));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testSkipZeroBytes() {
        try {
            // Edge case - skipping 0 bytes in an input stream
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            long skipped = IOUtils.skip(input, 0);
            assertEquals("Skipped count should be 0", 0, skipped);
            assertEquals("Input should remain the same", "Hello, World!", new String(input.readAllBytes()));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testSkipMoreThanAvailableBytes() {
        try {
            // Edge case - skipping more bytes than available in an input stream
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            long skipped = IOUtils.skip(input, 15);
            assertEquals("Skipped count should be equal to available bytes", 13, skipped);
            assertEquals("Input should be empty", "", new String(input.readAllBytes()));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testReadFully() {
        try {
            // Typical use case - reading bytes from an input stream to fill a buffer
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            byte[] buffer = new byte[12];
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals("Bytes read should be equal to buffer size", 12, bytesRead);
            assertEquals("Buffer should contain \"Hello, World\"", "Hello, World", new String(buffer));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testReadFullyWithOffset() {
        try {
            // Typical use case - reading bytes from an input stream to fill a buffer with an offset
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            byte[] buffer = new byte[16];
            int bytesRead = IOUtils.readFully(input, buffer, 4, 12);
            assertEquals("Bytes read should be equal to length parameter", 12, bytesRead);
            assertEquals("Buffer should contain \"----Hello, World\"", "----Hello, World", new String(buffer));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidParameters() throws IOException {
        // Error scenario - reading bytes from an input stream with invalid length and offset parameters
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[12];
        IOUtils.readFully(input, buffer, -1, 14);
    }

    @Test
    public void testToByteArray() {
        try {
            // Typical use case - converting an input stream to byte array
            ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
            byte[] result = IOUtils.toByteArray(input);
            assertEquals("Byte array should be equal to input", "Hello, World!", new String(result));
        } catch (IOException e) {
            fail("Should not throw an IOException");
        }
    }

    @Test
    public void testCloseQuietly() {
        // Typical use case - closing a closeable object quietly
        Closeable closeable = new ByteArrayInputStream("Hello, World!".getBytes());
        IOUtils.closeQuietly(closeable);
        // No assertion necessary, just checking for exceptions
    }
}