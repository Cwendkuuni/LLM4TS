import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.utils.IOUtils;

public class IOUtilsTest {

    // Test the copy() method with default buffer size
    @Test
    public void testCopyDefaultBufferSize() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        assertEquals(5, count);
        byte[] result = output.toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
        input.close();
        output.close();
    }
    
    // Test the copy() method with custom buffer size
    @Test
    public void testCopyCustomBufferSize() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 16);
        assertEquals(5, count);
        byte[] result = output.toByteArray();
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
        input.close();
        output.close();
    }
    
    // Test the skip() method
    @Test
    public void testSkip() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long numToSkip = IOUtils.skip(input, 2);
        assertEquals(2, numToSkip);
        input.close();
    }

    // Test the readFully() method
    @Test
    public void testReadFully() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] b = new byte[5];
        int count = IOUtils.readFully(input, b);
        assertEquals(5, count);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, b);
        input.close();
    }

    // Test the readFully() method with custom offset and length
    @Test
    public void testReadFullyWithOffsetAndLength() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] b = new byte[5];
        int count = IOUtils.readFully(input, b, 1, 3);
        assertEquals(3, count);
        assertArrayEquals(new byte[]{0, 1, 2, 0, 0}, b);
        input.close();
    }

    // Test the toByteArray() method
    @Test
    public void testToByteArray() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] result = IOUtils.toByteArray(input);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, result);
        input.close();
    }

    // Test the closeQuietly() method with non-null Closeable
    @Test
    public void testCloseQuietlyWithNonNullCloseable() throws Exception {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        IOUtils.closeQuietly(input);
        assertTrue(true); // No exception should be thrown
    }

    // Test the closeQuietly() method with null Closeable
    @Test
    public void testCloseQuietlyWithNullCloseable() throws Exception {
        Closeable c = null;
        IOUtils.closeQuietly(c);
        assertTrue(true); // No exception should be thrown
    }
}