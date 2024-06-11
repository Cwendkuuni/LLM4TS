import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {

    private IOUtils ioUtils;

    @Before
    public void setUp() {
        ioUtils = new IOUtils();
    }

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = ioUtils.copy(input, output);
        assertEquals(10, count);
        assertEquals("Test Input", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = ioUtils.copy(input, output, 10);
        assertEquals(10, count);
        assertEquals("Test Input", output.toString());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        long numToSkip = 5;
        long count = ioUtils.skip(input, numToSkip);
        assertEquals(5, count);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        byte[] b = new byte[4];
        int count = ioUtils.readFully(input, b);
        assertEquals(4, count);
        assertArrayEquals(new byte[] {'T', 'e', 's', 't'}, b);
    }

    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        byte[] b = new byte[6];
        int offset = 1;
        int len = 4;
        int count = ioUtils.readFully(input, b, offset, len);
        assertEquals(4, count);
        assertArrayEquals(new byte[] {0, 'T', 'e', 's', 0, 0}, b);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Input".getBytes());
        byte[] result = ioUtils.toByteArray(input);
        assertArrayEquals("Test Input".getBytes(), result);
    }

    @Test
    public void testCloseQuietly() {
        // Need to find a way to test this method as it is void and swallows exceptions
    }
}