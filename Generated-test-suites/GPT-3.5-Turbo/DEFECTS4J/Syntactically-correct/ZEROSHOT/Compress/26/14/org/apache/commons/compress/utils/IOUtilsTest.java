import org.junit.Test;
import static org.junit.Assert.*;

public class IOUtilsTest {

    @Test
    public void testCopyDefaultBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        assertEquals(4L, IOUtils.copy(input, output));
        assertArrayEquals("test".getBytes(), output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        assertEquals(4L, IOUtils.copy(input, output, 10));
        assertArrayEquals("test".getBytes(), output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        assertEquals(4L, IOUtils.skip(input, 4L));
        assertEquals(-1L, input.skip(1L));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[4];
        InputStream input = new ByteArrayInputStream("test".getBytes());
        assertEquals(4, IOUtils.readFully(input, buffer));
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[4];
        InputStream input = new ByteArrayInputStream("test".getBytes());
        assertEquals(2, IOUtils.readFully(input, buffer, 1, 2));
        byte[] expected = { 0, 't', 'e', 0 };
        assertArrayEquals(expected, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals("test".getBytes(), result);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("test exception");
            }
        };
        IOUtils.closeQuietly(closeable); // should not throw any exception
    }
}