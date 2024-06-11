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
        long expected = 5L;

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(new byte[]{1, 2, 3, 4, 5}, output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 3);
        long expected = 5L;

        Assert.assertEquals(expected, actual);
        Assert.assertEquals(new byte[]{1, 2, 3, 4, 5}, output.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long actual = IOUtils.skip(input, 3L);
        long expected = 2L;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSkipWithNegativeNumber() {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        Assert.expectThrows(IndexOutOfBoundsException.class, () -> IOUtils.skip(input, -1L));
    }

    @Test
    public void testSkipWithEndOfStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long actual = IOUtils.skip(input, 10L);
        long expected = 0L;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] output = new byte[5];

        int actual = IOUtils.readFully(input, output);
        int expected = 5;

        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output);
    }

    @Test
    public void testReadFullyWithNegativeNumber() {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        Assert.expectThrows(IndexOutOfBoundsException.class, () -> IOUtils.readFully(input, -1, 0, 0));
    }

    @Test
    public void testReadFullyWithEndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] output = new byte[10];

        int actual = IOUtils.readFully(input, output, 0, 5);
        int expected = 5;

        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, output, 0, 5);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] expected = new byte[]{1, 2, 3, 4, 5};

        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArrayWithNullInputStream() {
        Assert.assertNull(IOUtils.toByteArray(null));
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream("Testing".getBytes());

        IOUtils.closeQuietly(input);
        Assert.assertFalse(input.isOpen());
    }
}