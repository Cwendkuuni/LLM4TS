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
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(5, actual);

        byte[] expected = new byte[]{"Test".getBytes()};
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 1);
        Assert.fail("Expected IOException with message 'Buffer size must be greater than 0'");
    }

    @Test
    public void testCopyWithValidBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, IOUtils.COPY_BUF_SIZE);
        Assert.assertEquals(5, actual);

        byte[] expected = new byte[]{"Test".getBytes()};
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        long numToSkip = 2;

        long actual = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(3, actual);

        byte[] expected = new byte[]{"st".getBytes()};
        byte[] result = new byte[2];
        int readBytes = IOUtils.readFully(input, result);
        Assert.assertEquals(2, readBytes);
        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testSkipEndOfStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        long numToSkip = 7;

        long actual = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[5];

        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals(5, actual);

        byte[] expected = new byte[]{"Test".getBytes()};
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testReadFullyWithInvalidLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[3];

        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals(3, actual);

        byte[] expected = new byte[]{'T', 'e', ' '};
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] expected = new byte[]{"Test".getBytes()};

        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArrayWithNullInputStream() {
        // Expected exception.
        Assert.assertThrows(NullPointerException.class, () -> IOUtils.toByteArray(null));
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(new byte[0]), outputStream);
            IOUtils.closeQuietly(outputStream);
        } catch (IOException e) {
            // Expected exception.
        }
    }
}