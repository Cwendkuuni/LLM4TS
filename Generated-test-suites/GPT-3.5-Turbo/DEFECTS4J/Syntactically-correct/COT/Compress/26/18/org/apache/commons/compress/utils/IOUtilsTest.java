import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class IOUtilsTest {

    private ByteArrayInputStream input;
    private ByteArrayOutputStream output;

    @Before
    public void setup() {
        input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        output = new ByteArrayOutputStream();
    }
    
    @Test
    public void testCopy_defaultBuffer() throws IOException {
        assertEquals(5, IOUtils.copy(input, output));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output.toByteArray());
    }
    
    @Test
    public void testCopy_customBuffer() throws IOException {
        assertEquals(5, IOUtils.copy(input, output, 10));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        input.skip(2);
        IOUtils.skip(input, 2);
        byte[] result = new byte[1];
        assertEquals(1, input.read(result));
        assertArrayEquals(new byte[]{4}, result);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] result = new byte[5];
        assertEquals(5, IOUtils.readFully(input, result));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] result = new byte[5];
        assertEquals(3, IOUtils.readFully(input, result, 1, 3));
        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, result);
    }

    @Test
    public void testToByteArray() throws IOException {
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, IOUtils.toByteArray(input));
    }

    @Test
    public void testCloseQuietly() throws IOException {
        // Closeable that throws IOException
        ByteArrayOutputStream testCloseable = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test Exception");
            }
        };

        // Closing normally should not throw an exception
        IOUtils.closeQuietly(output);

        // Closing a Closeable that throws IOException should not throw an exception
        IOUtils.closeQuietly(testCloseable);
    }
}