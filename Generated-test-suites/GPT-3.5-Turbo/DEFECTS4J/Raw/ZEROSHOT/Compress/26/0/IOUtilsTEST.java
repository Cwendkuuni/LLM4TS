import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.*;

public class IOUtilsTest {

    private IOUtils ioUtils;

    @Before
    public void setUp() {
        ioUtils = new IOUtils();
    }

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long result = ioUtils.copy(input, output);
        assertEquals(4, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long result = ioUtils.copy(input, output, 4096);
        assertEquals(4, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        long result = ioUtils.skip(input, 2);
        assertEquals(2, result);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        int result = ioUtils.readFully(input, buffer);
        assertEquals(4, result);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] buffer = new byte[4];
        int result = ioUtils.readFully(input, buffer, 0, 4);
        assertEquals(4, result);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        byte[] result = ioUtils.toByteArray(input);
        assertArrayEquals("test".getBytes(), result);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        InputStream input = new ByteArrayInputStream("test".getBytes());
        ioUtils.closeQuietly(input);
        // no exception should be thrown
    }

}