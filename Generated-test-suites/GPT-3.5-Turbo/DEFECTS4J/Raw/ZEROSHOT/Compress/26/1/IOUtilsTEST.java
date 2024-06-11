import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 8);
        assertEquals(13, count);
        assertEquals("Hello, World!", output.toString());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("abcdefghijkl".getBytes());
        long skipped = IOUtils.skip(input, 5);
        assertEquals(5, skipped);
        byte[] buffer = new byte[6];
        int bytesRead = input.read(buffer);
        assertEquals(6, bytesRead);
        assertEquals("ghijkl", new String(buffer));
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        assertEquals(5, bytesRead);
        assertEquals("Hello", new String(buffer));
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[8];
        int bytesRead = IOUtils.readFully(input, buffer, 2, 6);
        assertEquals(6, bytesRead);
        assertEquals("\u0000\u0000Hello,", new String(buffer));
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        assertEquals("Hello, World!", new String(byteArray));
    }

    @Test
    public void testCloseQuietly() throws IOException {
        Closeable closeable = new Closeable() {
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(closeable);
    }
}