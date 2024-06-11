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
     * Test copy method with regular input and output streams
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        Assert.assertEquals("Incorrect number of bytes copied", 13, count);
        Assert.assertEquals("Incorrect content of the output stream", "Hello World", output.toString());
    }

    /**
     * Test copy method with custom buffer size
     */
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output, 10);
        Assert.assertEquals("Incorrect number of bytes copied", 1, count);
        Assert.assertFalse("Buffer should not be empty", output.available() > 0);

        input = new ByteArrayInputStream("Hello World".getBytes());
        output = new ByteArrayOutputStream();
        count = IOUtils.copy(input, output, 13);
        Assert.assertEquals("Incorrect number of bytes copied", 13, count);
        Assert.assertEquals("Incorrect content of the output stream", "Hello World", output.toString());
    }

    /**
     * Test skip method with regular input stream
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello WorldHello World".getBytes());
        long numToSkip = 11;
        long skipped = IOUtils.skip(input, numToSkip);
        Assert.assertEquals("Incorrect number of bytes skipped", 11, skipped);
        Assert.assertEquals("Incorrect content of the input stream after skipping", "WorldWorld", input.toString());
    }

    /**
     * Test skip method with empty input stream
     */
    @Test
    public void testSkipWithEmptyInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        long numToSkip = 10;
        long skipped = IOUtils.skip(input, numToSkip);
        Assert.assertEquals("Incorrect number of bytes skipped", 0, skipped);
    }

    /**
     * Test readFully method with regular input stream and buffer
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] buffer = new byte[13];
        int bytesRead = IOUtils.readFully(input, buffer);
        Assert.assertEquals("Incorrect number of bytes read", 13, bytesRead);
        Assert.assertArrayEquals("Incorrect content of the buffer", "Hello World".getBytes(), buffer);
    }

    /**
     * Test readFully method with input stream that does not have enough data
     */
    @Test
    public void testReadFullyWithInputStreamWithoutEnoughData() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[5]);
        byte[] buffer = new byte[13];
        int bytesRead = IOUtils.readFully(input, buffer);
        Assert.assertEquals("Incorrect number of bytes read", 5, bytesRead);
        Assert.assertArrayEquals("Unexpected content of the buffer", new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    /**
     * Test toByteArray method with regular input stream
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Hello World".getBytes());
        byte[] expected = "Hello World".getBytes();
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Incorrect content of the resulting byte array", expected, result);
    }

    /**
     * Test closeQuietly method with null Closeable
     */
    @Test
    public void testCloseQuietlyWithNullCloseable() {
        IOUtils.closeQuietly(null);
    }

    /**
     * Test closeQuietly method with valid Closeable
     */
    @Test
    public void testCloseQuietlyWithValidCloseable() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output);
    }
}