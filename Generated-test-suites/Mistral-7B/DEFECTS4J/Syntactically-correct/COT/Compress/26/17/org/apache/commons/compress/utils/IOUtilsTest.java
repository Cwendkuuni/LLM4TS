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
        Assert.assertEquals(5L, actual);

        byte[] expected = new byte[]{'T', 'e', 's', 't', '\n'};
        Assert.assertArrayEquals(expected, output.toByteArray());

        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test\n".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output, 2);
        Assert.assertEquals(5L, actual);

        byte[] expected = new byte[]{'T', 'e', 's', 't', '\n'};
        Assert.assertArrayEquals(expected, output.toByteArray());

        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test\n".getBytes()});

        long actual = IOUtils.skip(input, 3L);
        Assert.assertEquals(3L, actual);

        byte[] expected = new byte[]{'s', 't', '\n'};
        byte[] result = new byte[3];
        int read = IOUtils.readFully(input, result);
        Assert.assertArrayEquals(expected, result);

        IOUtils.closeQuietly(input);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test\n".getBytes()});
        byte[] b = new byte[5];
        IOUtils.readFully(input, b, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test\n".getBytes()});
        byte[] b = new byte[5];
        IOUtils.readFully(input, b, 0, -1);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test\n".getBytes()});
        byte[] b = new byte[5];
        int actual = IOUtils.readFully(input, b);
        Assert.assertEquals(5, actual);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', '\n'}, b);

        IOUtils.closeQuietly(input);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test\n".getBytes()});
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', '\n'}, actual);

        IOUtils.closeQuietly(input);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(new ByteArrayInputStream(new byte[0]), outputStream);
        IOUtils.closeQuietly(outputStream);
    }
}