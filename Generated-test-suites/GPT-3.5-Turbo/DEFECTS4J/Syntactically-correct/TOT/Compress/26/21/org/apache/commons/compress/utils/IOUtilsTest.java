import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.compress.utils.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class IOUtilsTest {

    // Testing copy(final InputStream input, final OutputStream output)
    @Test
    public void testCopy() throws IOException {
        // Typical use case: Copying content from one input stream to another
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output);
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, bytesCopied);

        // Edge case: Empty input stream
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();

        bytesCopied = IOUtils.copy(input, output);
        assertArrayEquals(new byte[0], output.toByteArray());
        assertEquals(0, bytesCopied);

        // Error scenario: Null input stream
        try {
            IOUtils.copy(null, output);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Testing copy(final InputStream input, final OutputStream output, int buffersize)
    @Test
    public void testCopyWithBufferSize() throws IOException {
        // Typical use case: Copying content from one input stream to another with a specific buffer size
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output, 4);
        assertArrayEquals(inputBytes, output.toByteArray());
        assertEquals(inputBytes.length, bytesCopied);

        // Edge case: Empty input stream
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();

        bytesCopied = IOUtils.copy(input, output, 4);
        assertArrayEquals(new byte[0], output.toByteArray());
        assertEquals(0, bytesCopied);

        // Error scenario: Null input stream
        try {
            IOUtils.copy(null, output, 4);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Testing skip(InputStream input, long numToSkip)
    @Test
    public void testSkip() throws IOException {
        // Typical use case: Skipping a specified number of bytes in an input stream
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long bytesSkipped = IOUtils.skip(input, 3);
        assertEquals(3, bytesSkipped);
        assertEquals(4, input.read());

        // Edge case: Skipping more bytes than available in the input stream
        input = new ByteArrayInputStream(new byte[5]);

        bytesSkipped = IOUtils.skip(input, 10);
        assertEquals(5, bytesSkipped);
        assertEquals(-1, input.read());

        // Error scenario: Null input stream
        try {
            IOUtils.skip(null, 5);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Testing readFully(InputStream input, byte[] b)
    @Test
    public void testReadFully() throws IOException {
        // Typical use case: Reading as much from input as possible to fill the given array
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int bytesRead = IOUtils.readFully(input, outputBytes);
        assertArrayEquals(inputBytes, outputBytes);
        assertEquals(inputBytes.length, bytesRead);

        // Edge case: Empty input stream
        input = new ByteArrayInputStream(new byte[0]);
        outputBytes = new byte[5];

        bytesRead = IOUtils.readFully(input, outputBytes);
        assertArrayEquals(new byte[5], outputBytes);
        assertEquals(0, bytesRead);

        // Error scenario: Null input stream
        try {
            IOUtils.readFully(null, outputBytes);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Testing readFully(InputStream input, byte[] b, int offset, int len)
    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        // Typical use case: Reading as much from input as possible to fill the given array with the given offset and length
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int bytesRead = IOUtils.readFully(input, outputBytes, 1, 3);
        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, outputBytes);
        assertEquals(3, bytesRead);

        // Edge case: Empty input stream
        input = new ByteArrayInputStream(new byte[0]);
        outputBytes = new byte[5];

        bytesRead = IOUtils.readFully(input, outputBytes, 1, 3);
        assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, outputBytes);
        assertEquals(0, bytesRead);

        // Error scenario: Invalid offset and length
        input = new ByteArrayInputStream(inputBytes);
        outputBytes = new byte[5];

        try {
            IOUtils.readFully(input, outputBytes, -1, 6);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    // Testing toByteArray(final InputStream input)
    @Test
    public void testToByteArray() throws IOException {
        // Typical use case: Getting the contents of an input stream as a byte array
        byte[] inputBytes = {1, 2, 3, 4, 5};
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(inputBytes, outputBytes);

        // Edge case: Empty input stream
        input = new ByteArrayInputStream(new byte[0]);

        outputBytes = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[0], outputBytes);

        // Error scenario: Null input stream
        try {
            IOUtils.toByteArray(null);
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    // Testing closeQuietly(Closeable c)
    @Test
    public void testCloseQuietly() {
        // Typical use case: Closing a closeable object
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        IOUtils.closeQuietly(closeable);

        // Error scenario: Null closeable object
        IOUtils.closeQuietly(null);
    }

}