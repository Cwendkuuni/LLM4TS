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

        long bytesCopied = IOUtils.copy(input, output);
        Assert.assertEquals(5L, bytesCopied);

        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = new ByteArrayOutputStream();

        long bytesCopied = IOUtils.copy(input, output, 3);
        Assert.assertEquals(5L, bytesCopied);

        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, outputBytes);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyWithNullInput() {
        OutputStream output = new ByteArrayOutputStream();
        InputStream input = null;

        IOUtils.copy(input, output);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyWithNullOutput() {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        OutputStream output = null;

        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long numToSkip = 3;
        long bytesSkipped = IOUtils.skip(input, numToSkip);

        Assert.assertEquals(3L, bytesSkipped);

        int skippedBytes = IOUtils.readFully(input, new byte[3]);
        Assert.assertEquals(3, skippedBytes);

        byte[] remainingBytes = new byte[2];
        IOUtils.readFully(input, remainingBytes);
        Assert.assertArrayEquals(new byte[]{4, 5}, remainingBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer);

        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[3];

        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] expectedByteArray = new byte[]{1, 2, 3, 4, 5};

        byte[] actualByteArray = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expectedByteArray, actualByteArray);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }
}