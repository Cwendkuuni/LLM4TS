import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(input, output);
        Assert.assertEquals("Test Data".getBytes().length, result);
        Assert.assertEquals("Test Data", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream(50);
        long result = IOUtils.copy(input, output, 25);
        Assert.assertEquals(25, result);
        Assert.assertEquals("Test Data", output.toString());
    }

    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output); // Exception expected
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        long skipped = IOUtils.skip(input, 10);
        Assert.assertEquals(10, skipped);
        Assert.assertEquals("Test Data", input.toString());
    }

    @Test
    public void testSkipWithEof() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        long skipped = IOUtils.skip(input, 20);
        Assert.assertEquals(13, skipped); // 13 bytes left in the stream
        Assert.assertEquals("Data", input.toString());
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] buffer = new byte[10];
        int result = IOUtils.readFully(input, buffer);
        Assert.assertEquals(10, result);
        Assert.assertArrayEquals("Test Data".getBytes(), buffer);
    }

    @Test
    public void testReadFullyWithLessBytes() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] buffer = new byte[5];
        int result = IOUtils.readFully(input, buffer);
        Assert.assertEquals(5, result);
        Assert.assertArrayEquals("Test Data".substring(0, 5).getBytes(), buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Test Data".getBytes(), result);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        IOUtils.closeQuietly(input);
        Assert.assertSame(null, input); // InputStream should be closed
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
        // Nothing to test here, Closeable is null
    }
}