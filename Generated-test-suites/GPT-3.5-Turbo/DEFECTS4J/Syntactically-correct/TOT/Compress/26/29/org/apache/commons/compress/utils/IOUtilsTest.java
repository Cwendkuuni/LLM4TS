import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.io.*;

public class IOUtilsTest {

    //Test copy method with typical use case
    @Test
    public void testCopyTypical() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(12, count);
        assertEquals("Sample input", output.toString());
    }

    //Test copy method with edge case of empty input stream
    @Test
    public void testCopyEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream("".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(0, count);
        assertEquals("", output.toString());
    }

    //Test copy method with error scenario of null input
    @Test(expected = NullPointerException.class)
    public void testCopyNullInput() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    //Test skip method with typical use case
    @Test
    public void testSkipTypical() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        long skipped = IOUtils.skip(input, 7);
        assertEquals(7, skipped);
        byte[] b = new byte[5];
        int bytesRead = input.read(b);
        assertEquals("input", new String(b));
    }

    //Test skip method with edge case of skipping 0 bytes
    @Test
    public void testSkipZeroBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        long skipped = IOUtils.skip(input, 0);
        assertEquals(0, skipped);
        byte[] b = new byte[5];
        int bytesRead = input.read(b);
        assertEquals("Sampl", new String(b));
    }

    //Test skip method with error scenario of null input
    @Test(expected = NullPointerException.class)
    public void testSkipNullInput() throws IOException {
        IOUtils.skip(null, 5);
    }

    //Test readFully method with typical use case
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        byte[] b = new byte[6];
        int bytesRead = IOUtils.readFully(input, b, 0, 6);
        assertEquals(6, bytesRead);
        assertEquals("Sample", new String(b));
    }

    //Test readFully method with edge case of reading 0 bytes
    @Test
    public void testReadFullyZeroBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        byte[] b = new byte[6];
        int bytesRead = IOUtils.readFully(input, b, 0, 0);
        assertEquals(0, bytesRead);
        assertEquals(0, input.available());
    }

    //Test readFully method with error scenario of negative length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        byte[] b = new byte[6];
        IOUtils.readFully(input, b, 0, -6);
    }

    //Test toByteArray method with typical use case
    @Test
    public void testToByteArrayTypical() throws IOException {
        InputStream input = new ByteArrayInputStream("Sample input".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        assertArrayEquals("Sample input".getBytes(), byteArray);
    }

    //Test toByteArray method with edge case of empty input stream
    @Test
    public void testToByteArrayEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream("".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        assertArrayEquals("".getBytes(), byteArray);
    }

    //Test closeQuietly method with typical use case
    @Test
    public void testCloseQuietlyTypical() {
        Closeable closeable = mock(Closeable.class);
        assertDoesNotThrow(() -> IOUtils.closeQuietly(closeable));
    }

    //Test closeQuietly method with error scenario of null Closeable
    @Test
    public void testCloseQuietlyNullCloseable() {
        assertDoesNotThrow(() -> IOUtils.closeQuietly(null));
    }
}