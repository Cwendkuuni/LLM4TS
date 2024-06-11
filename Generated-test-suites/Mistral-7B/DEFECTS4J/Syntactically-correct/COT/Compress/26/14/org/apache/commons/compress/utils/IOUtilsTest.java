import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils test cases
 */
public class IOUtilsTest {

    /**
     * Test IOUtils.copy method
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils.copy".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        String expected = "Test IOUtils.copy";
        byte[] outputBytes = output.toByteArray();
        Assert.assertEquals(expected.length(), actual);
        Assert.assertEquals(expected, new String(outputBytes));
    }

    /**
     * Test IOUtils.copy method with different buffer size
     * @throws IOException
     */
    @Test
    public void testCopyWithDifferentBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils.copyWithDifferentBufferSize".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output, 2048);
        String expected = "Test IOUtils.copyWithDifferentBufferSize";
        byte[] outputBytes = output.toByteArray();
        Assert.assertEquals(expected.length(), actual);
        Assert.assertEquals(expected, new String(outputBytes));
    }

    /**
     * Test IOUtils.skip method
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test IOUtils.skip".getBytes());
        long numToSkip = 5;
        long actual = IOUtils.skip(input, numToSkip);
        long expected = numToSkip - (output.size() % numToSkip);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test IOUtils.skip method when reaching end of stream
     * @throws IOException
     */
    @Test
    public void testSkipWhenEndOfStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test IOUtils.skipWhenEndOfStream".getBytes());
        long numToSkip = input.available();
        long actual = IOUtils.skip(input, numToSkip);
        long expected = 0;
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test IOUtils.readFully method
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test IOUtils.readFully".getBytes());
        byte[] buffer = new byte[10];
        int actual = IOUtils.readFully(input, buffer);
        int expected = 10;
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test IOUtils.readFully method when reaching end of stream
     * @throws IOException
     */
    @Test
    public void testReadFullyWhenEndOfStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test IOUtils.readFullyWhenEndOfStream".getBytes());
        byte[] buffer = new byte[10];
        int actual = IOUtils.readFully(input, buffer);
        int expected = 9; // 1 byte is not read due to end of the stream
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test IOUtils.toByteArray method
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils.toByteArray".getBytes());
        byte[] actual = IOUtils.toByteArray(input);
        String expected = "Test IOUtils.toByteArray";
        Assert.assertEquals(expected.length(), actual.length);
        Assert.assertEquals(expected, new String(actual));
    }

    /**
     * Test IOUtils.closeQuietly method
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(output); // No exception should be thrown
    }
}