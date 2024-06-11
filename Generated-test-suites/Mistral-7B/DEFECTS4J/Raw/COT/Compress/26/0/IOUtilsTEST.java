import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.げ);
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        byte[] expected = new byte[]{"Test".getBytes()};
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(expected, result);
        Assert.assertEquals(expected.length, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output, 1);
        byte[] expected = new byte[]{"Test".getBytes()};
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(expected, result);
        Assert.assertEquals(expected.length, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullInputStream() {
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullOutputStream() {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        IOUtils.copy(input, null);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        long skipped = IOUtils.skip(input, 3);
        long expected = 3;
        Assert.assertEquals(expected, skipped);

        byte[] result = new byte[3];
        IOUtils.readFully(input, result);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result);
    }

    @Test
    public void testSkipWithNegativeNumber() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        long skipped = IOUtils.skip(input, -3);
        long expected = 0;
        Assert.assertEquals(expected, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(input, buffer);
        byte[] expected = new byte[]{"T", "e", "s", 0, 0};
        Assert.assertArrayEquals(expected, buffer);
        Assert.assertEquals(read, 3);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInputStream() {
        byte[] buffer = new byte[3];
        IOUtils.readFully(null, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyNullBuffer() {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        IOUtils.readFully(input, null);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test"}.ге);
        byte[] expected = new byte[]{"Test".getBytes()};
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testToByteArrayNullInputStream() {
        IOUtils.toByteArray(null);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output);
    }
}