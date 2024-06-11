import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() {
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            long count = IOUtils.copy(input, output);
            assertEquals(data.length, count);
            assertEquals("Hello, world!", output.toString());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCopyWithBufferSize() {
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 1024;

        try {
            long count = IOUtils.copy(input, output, bufferSize);
            assertEquals(data.length, count);
            assertEquals("Hello, world!", output.toString());
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);
        long numToSkip = 7;

        try {
            long skipped = IOUtils.skip(input, numToSkip);
            assertEquals(numToSkip, skipped);

            // Read the remaining bytes
            byte[] remainingBytes = new byte[data.length - (int) numToSkip];
            int bytesRead = input.read(remainingBytes);
            assertEquals(data.length - numToSkip, bytesRead);
            assertEquals("world!", new String(remainingBytes));
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadFully() {
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        byte[] buffer = new byte[5];

        try {
            int bytesRead = IOUtils.readFully(input, buffer);
            assertEquals(buffer.length, bytesRead);
            assertEquals("Hello", new String(buffer));
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() {
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        byte[] buffer = new byte[5];

        try {
            int bytesRead = IOUtils.readFully(input, buffer, 0, buffer.length);
            assertEquals(buffer.length, bytesRead);
            assertEquals("Hello", new String(buffer));
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testToByteArray() {
        byte[] data = "Hello, world!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(data);

        try {
            byte[] result = IOUtils.toByteArray(input);
            assertArrayEquals(data, result);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testCloseQuietly() {
        // Closeable object that does nothing
        Closeable closeable = new Closeable() {
            public void close() throws IOException {
                // Do nothing
            }
        };

        // Try to close the Closeable object
        try {
            IOUtils.closeQuietly(closeable);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }

        // Closeable object that throws an IOException
        Closeable closeableWithException = new Closeable() {
            public void close() throws IOException {
                throw new IOException("Exception in close()");
            }
        };

        // Try to close the Closeable object that throws an exception
        try {
            IOUtils.closeQuietly(closeableWithException);
        } catch (Exception e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}