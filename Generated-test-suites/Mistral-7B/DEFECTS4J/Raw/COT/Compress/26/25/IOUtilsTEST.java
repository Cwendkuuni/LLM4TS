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
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(5L, actual);

        byte[] expected = new byte[]{1, 2, 3, 4, 5};
        byte[] actualBytes = output.toByteArray();
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 1);
        Assert.assertEquals(5L, actual);

        byte[] expected = new byte[]{1, 2, 3, 4, 5};
        byte[] actualBytes = output.toByteArray();
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNullInputStream() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyWithNullOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        IOUtils.copy(input, null);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        long actual = IOUtils.skip(input, 3L);
        Assert.assertEquals(3L, actual);

        byte[] expected = new byte[]{4, 5};
        byte[] actualBytes = new byte[2];
        int read = IOUtils.readFully(input, actualBytes, 0, 2);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];
        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals(5, actual);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithNullInputStream() throws IOException {
        byte[] buffer = new byte[5];
        IOUtils.readFully(null, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        IOUtils.readFully(input, null);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testToByteArrayWithNullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1});
        IOUtils.closeQuietly(input);
        Assert.assertFalse(input.isOpen());
    }
}