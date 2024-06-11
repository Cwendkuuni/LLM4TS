import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test cases for IOUtils class
 */
public class IOUtilsTest {

    /**
     * Test copy method with default buffer size.
     * @throws IOException
     */
    @Test
    public void testCopyWithDefaultBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' '});
        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(5L, copiedBytes);
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', ' '}, result);
    }

    /**
     * Test copy method with custom buffer size.
     * @throws IOException
     */
    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' '});
        long copiedBytes = IOUtils.copy(input, output, 3);
        Assert.assertEquals(3L, copiedBytes);
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result);
    }

    /**
     * Test skip method with positive value.
     * @throws IOException
     */
    @Test
    public void testSkipWithPositiveValue() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' ', 't', 'e', 's', 't'});
        long skippedBytes = IOUtils.skip(input, 5L);
        Assert.assertEquals(5L, skippedBytes);
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'t', 'e', 's', 't'}, result);
    }

    /**
     * Test skip method with zero value.
     * @throws IOException
     */
    @Test
    public void testSkipWithZeroValue() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' ', 't', 'e', 's', 't'});
        long skippedBytes = IOUtils.skip(input, 0L);
        Assert.assertEquals(0L, skippedBytes);
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', ' ', 't', 'e', 's', 't'}, result);
    }

    /**
     * Test skip method with negative value.
     * @throws IOException
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSkipWithNegativeValue() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' ', 't', 'e', 's', 't'});
        long skippedBytes = IOUtils.skip(input, -1L);
    }

    /**
     * Test readFully method with default buffer size.
     * @throws IOException
     */
    @Test
    public void testReadFullyWithDefaultBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' '});
        int readBytes = IOUtils.readFully(input, new byte[5]);
        Assert.assertEquals(5, readBytes);
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', ' '}, result);
    }

    /**
     * Test readFully method with custom buffer size.
     * @throws IOException
     */
    @Test
    public void testReadFullyWithCustomBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' '});
        int readBytes = IOUtils.readFully(input, new byte[3]);
        Assert.assertEquals(3, readBytes);
        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, result);
    }

    /**
     * Test toByteArray method.
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', ' '});
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't', ' '}, result);
    }

    /**
     * Test closeQuietly method.
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException("Close failed.");
            }
        };
        IOUtils.closeQuietly(output);
    }
}