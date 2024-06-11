import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils test cases
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 'st'});
        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(3, copiedBytes);
        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, outputBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        long copiedBytes = IOUtils.copy(input, output, 1);
        Assert.assertEquals(3, copiedBytes);
        byte[] outputBytes = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testCopyThrowsIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output, 1); // no exception thrown in test case above, throw it here
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        long numToSkip = 2;
        long skippedBytes = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(1, skippedBytes); // only one byte is skipped in this case
        byte[] skippedBytesArray = new byte[1];
        int read = input.read(skippedBytesArray);
        Assert.assertEquals('s', skippedBytesArray[0]);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] buffer = new byte[3];
        int readBytes = IOUtils.readFully(input, buffer);
        Assert.assertEquals(3, readBytes);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, buffer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] buffer = new byte[10];
        IOUtils.readFully(input, buffer); // no exception thrown in test case above, throw it here
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's'});
        byte[] toByteArray = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's'}, toByteArray);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.closeQuietly((Closeable) output);
        } finally {
            // no op
        }
    }

    @Test
    public void testCloseQuietlyWithIOException() {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly((Closeable) output); // no exception thrown, swallow it here
    }
}