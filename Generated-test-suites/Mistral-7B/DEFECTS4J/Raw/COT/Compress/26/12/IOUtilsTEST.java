import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        long actual = IOUtils.copy(input, output);
        Assert.assertEquals("Unexpected number of bytes copied.", 13, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        long actual = IOUtils.copy(input, output, 5);
        Assert.assertEquals("Unexpected number of bytes copied.", 1, actual);
    }

    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        OutputStream output = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Test Exception");
            }
        };
        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        long actual = IOUtils.skip(input, 7L);
        Assert.assertEquals("Unexpected number of bytes skipped.", 7, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        IOUtils.copy(input, output);
        byte[] result = output.toByteArray();
        byte[] expected = "Test Data".getBytes();
        Assert.assertArrayEquals("Unexpected byte array.", expected, result);
    }

    @Test
    public void testReadFullyWithNegativeLength() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        int actual = IOUtils.readFully(input, new byte[5], 10, 15);
        Assert.assertEquals("Unexpected number of bytes read.", -1, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] expected = "Test Data".getBytes();
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Unexpected byte array.", expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        IOUtils.closeQuietly(input);
        // No need to assert anything here as we're swallowing any IOException
    }
}