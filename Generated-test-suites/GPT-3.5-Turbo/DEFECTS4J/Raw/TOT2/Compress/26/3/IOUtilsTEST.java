import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class IOUtilsTest {

    @Test
    public void testCopyWithDefaultBufferSize() {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            long bytesCopied = IOUtils.copy(input, output);
            assertEquals(4, bytesCopied);
            assertEquals("test", output.toString());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCopyWithCustomBufferSize() {
        InputStream input = new ByteArrayInputStream("custom".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            long bytesCopied = IOUtils.copy(input, output, 3);
            assertEquals(6, bytesCopied);
            assertEquals("custom", output.toString());
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testSkip() {
        InputStream input = new ByteArrayInputStream("skip test".getBytes());
        try {
            long bytesSkipped = IOUtils.skip(input, 5);
            assertEquals(5, bytesSkipped);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadFully() {
        byte[] expected = "full read".getBytes();
        InputStream input = new ByteArrayInputStream(expected);
        byte[] actual = new byte[8];
        try {
            int bytesRead = IOUtils.readFully(input, actual);
            assertEquals(8, bytesRead);
            assertArrayEquals(expected, actual);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadFullyWithOffsetAndLength() {
        byte[] expected = "full read".getBytes();
        InputStream input = new ByteArrayInputStream(expected);
        byte[] actual = new byte[8];
        try {
            int bytesRead = IOUtils.readFully(input, actual, 2, 3);
            assertEquals(3, bytesRead);
            assertArrayEquals(new byte[]{0, 0, 'f', 'u', 'l', 0, 0, 0}, actual);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testToByteArray() {
        byte[] expected = "to byte array".getBytes();
        InputStream input = new ByteArrayInputStream(expected);
        try {
            byte[] actual = IOUtils.toByteArray(input);
            assertArrayEquals(expected, actual);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // do nothing
            }
        };
        IOUtils.closeQuietly(closeable);
        assertTrue(true);
    }
}