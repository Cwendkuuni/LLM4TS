import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for the IOUtils class.
 */
public class IOUtilsTest {

    /**
     * Test the copy method with valid input and output streams and default buffer size.
     */
    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});
        IOUtils.copy(input, output);
        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test the copy method with valid input and output streams and custom buffer size.
     */
    @Test
    public void testCopyCustomBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});
        IOUtils.copy(input, output, 4);
        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = output.toByteArray();
        Assert.assertArrayEquals(expected, actual);

        // Test that the buffer size is respected
        ByteArrayOutputStream output2 = new ByteArrayOutputStream();
        InputStream input2 = new ByteArrayInputStream(new byte[10000]);
        long count = IOUtils.copy(input2, output2, 1);
        Assert.assertEquals(1, count);
    }

    /**
     * Test the skip method with a valid input stream and a given number of bytes to skip.
     */
    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'i', 'm', ' '});
        long numToSkip = 3;
        long skipped = IOUtils.skip(input, numToSkip);
        byte[] expected = new byte[]{'i', 'm', ' '};
        byte[] actual = new byte[3];
        input.read(actual);
        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(numToSkip, skipped);
    }

    /**
     * Test the skip method with a valid input stream and a number of bytes to skip greater than the remaining bytes.
     */
    @Test
    public void testSkipEndOfStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T'});
        long numToSkip = 10;
        long skipped = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(0, skipped);
    }

    /**
     * Test the readFully method with a valid input stream and a given byte array.
     */
    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'i', 'm'});
        byte[] expected = new byte[]{'T', 'e', 's', 't', 'i', 'm'};
        byte[] buffer = new byte[6];
        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, buffer);
    }

    /**
     * Test the readFully method with a valid input stream and a given byte array of insufficient size.
     */
    @Test(expected = java.lang.AssertionError.class)
    public void testReadFullyInsufficientSize() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'i', 'm'});
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer);
    }

    /**
     * Test the toByteArray method with a valid input stream.
     */
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});
        byte[] expected = new byte[]{'T', 'e', 's', 't'};
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test the closeQuietly method with a valid closeable.
     */
    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStream closeable = output;
        IOUtils.closeQuietly(closeable);
    }

    /**
     * Test the closeQuietly method with a null closeable.
     */
    @Test(expected = NullPointerException.class)
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}