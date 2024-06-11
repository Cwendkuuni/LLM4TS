import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(input, output);
        assertEquals(13, result);
        assertEquals("Hello, World!", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(input, output, 16);
        assertEquals(13, result);
        assertEquals("Hello, World!", output.toString());
    }
    
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        long result = IOUtils.skip(input, 7);
        assertEquals(7, result);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] result = new byte[5];
        int count = IOUtils.readFully(input, result);
        assertEquals(5, count);
        assertArrayEquals("Hello".getBytes(), result);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] result = new byte[10];
        int count = IOUtils.readFully(input, result, 1, 9);
        assertEquals(9, count);
        assertArrayEquals(new byte[]{0, 'H', 'e', 'l', 'l', 'o', ',', ' ', 'W', 'o'}, result);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals("Hello, World!".getBytes(), result);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {}
        };
        IOUtils.closeQuietly(closeable);
        // No assert statement as this method only checks for exceptions
    }

}