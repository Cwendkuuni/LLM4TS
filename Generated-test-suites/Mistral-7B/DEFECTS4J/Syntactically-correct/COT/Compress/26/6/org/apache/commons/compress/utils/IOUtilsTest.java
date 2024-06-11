import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(4L, copiedBytes); // 'T' takes up 1 byte, 'e' takes up 1 byte, 's' takes up 1 byte, 't' takes up 1 byte

        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't'}, result);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(input, output, 1);
        Assert.assertEquals(4L, copiedBytes);

        byte[] result = output.toByteArray();
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't'}, result);
    }

    @Test(expected = IOException.class)
    public void testCopyThrowsIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(null, output); // null input stream should throw an IOException
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        long numToSkip = 1;

        long skippedBytes = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(1L, skippedBytes);

        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[]{'e', 's', 't'}, result);
    }

    @Test
    public void testSkipWithLargeNumber() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        long numToSkip = Long.MAX_VALUE;

        long skippedBytes = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(0L, skippedBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[4];

        int bytesRead = IOUtils.readFully(input, buffer);
        Assert.assertEquals(4, bytesRead);

        byte[] result = new byte[4];
        System.arraycopy(buffer, 0, result, 0, 4);
        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't'}, result);
    }

    @Test
    public void testReadFullyWithNegativeLength() {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[4];

        try {
            IOUtils.readFully(input, buffer);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            IOUtils.readFully(input, buffer, 5, 4);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }

        try {
            IOUtils.readFully(input, null, 0, 4);
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] result = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(new byte[]{'T', 'e', 's', 't'}, result);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStream finalOutput = output;

        IOUtils.copy(new ByteArrayInputStream(new byte[0]), finalOutput);
        IOUtils.closeQuietly(finalOutput);

        Assert.assertEquals(0L, finalOutput.size());
    }
}