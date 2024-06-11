import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtilsTest class.
 */
public final class IOUtilsTest {

    /**
     * Test copy method with positive input and output streams.
     */
    @Test
    public void testCopyNormal() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(3, actual);

        byte[] expected = new byte[]{1, 2, 3};
        byte[] actualBytes = output.toByteArray();
        Assert.assertArrayEquals(expected, actualBytes);
    }

    /**
     * Test copy method with null input or output streams.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNull() throws IOException {
        IOUtils.copy(null, new ByteArrayOutputStream());
    }

    /**
     * Test skip method with positive input stream and given number of bytes to skip.
     */
    @Test
    public void testSkipNormal() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});

        long actual = IOUtils.skip(input, 3L);
        Assert.assertEquals(3, actual);

        byte[] expected = new byte[]{4, 5};
        byte[] actualBytes = new byte[2];
        int bytesRead = IOUtils.readFully(input, actualBytes);
        Assert.assertEquals(2, bytesRead);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    /**
     * Test skip method with null input stream or given number of bytes to skip less than zero.
     */
    @Test(expected = NullPointerException.class)
    public void testSkipError() throws IOException {
        IOUtils.skip(null, 3L);

        IOUtils.skip(new ByteArrayInputStream(new byte[]{1, 2, 3}), -1L);
    }

    /**
     * Test readFully method with positive input stream and given buffer.
     */
    @Test
    public void testReadFullyNormal() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];

        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals(5, actual);

        byte[] expected = new byte[]{1, 2, 3, 4, 5};
        Assert.assertArrayEquals(expected, buffer);
    }

    /**
     * Test readFully method with null input stream or given buffer with invalid size.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyError() throws IOException {
        IOUtils.readFully(null, new byte[10]);

        IOUtils.readFully(new ByteArrayInputStream(new byte[]{1, 2, 3}), new byte[50]);
    }

    /**
     * Test toByteArray method with positive input stream.
     */
    @Test
    public void testToByteArrayNormal() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3});

        byte[] expected = new byte[]{1, 2, 3};
        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test toByteArray method with null input stream.
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayError() throws IOException {
        IOUtils.toByteArray(null);
    }

    /**
     * Test closeQuietly method with closeable and no exception.
     */
    @Test
    public void testCloseQuietlyNormal() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output);
    }

    /**
     * Test closeQuietly method with closeable and IOException.
     */
    @Test
    public void testCloseQuietlyException() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException("Closing IOException");
            }
        };
        IOUtils.closeQuietly(output);
    }
}