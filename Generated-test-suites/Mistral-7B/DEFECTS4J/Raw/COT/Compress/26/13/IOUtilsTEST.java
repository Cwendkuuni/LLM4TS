import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IOUtils Test Class
 */
public class IOUtilsTest {

    /**
     * Test method for {@link IOUtils#copy(InputStream, OutputStream)}
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long expected = "Test String".length();
        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals("Test String", output.toString());
    }

    /**
     * Test method for {@link IOUtils#copy(InputStream, OutputStream, int)}
     * @throws IOException
     */
    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long expected = "Test String".length();
        long actual = IOUtils.copy(input, output, IOUtils.COPY_BUF_SIZE);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals("Test String", output.toString());
    }

    /**
     * Test method for {@link IOUtils#skip(InputStream, long)}
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test".getBytes());
        long expectedSkipped = 3;
        long skipped = IOUtils.skip(input, 6);
        long actualSkipped = tempOutputStream.size();
        tempOutputStream.write("Test".getBytes());
        tempOutputStream.close();

        Assert.assertEquals(expectedSkipped, skipped);
        Assert.assertEquals(3, actualSkipped);
    }

    /**
     * Test method for {@link IOUtils#readFully(InputStream, byte[], int)}
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        expectedOutput.write("Test String".getBytes());
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[8];
        int expectedLength = "Test String".length();
        int actualLength = IOUtils.readFully(input, buffer, 0, buffer.length);

        Assert.assertEquals(expectedLength, actualLength);
        Assert.assertArrayEquals("Test String".getBytes(), buffer);
    }

    /**
     * Test method for {@link IOUtils#toByteArray(InputStream)}
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expected = "Test String".getBytes();
        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test method for {@link IOUtils#closeQuietly(Closeable)}
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }
}