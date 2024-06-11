import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] outputData = new byte[inputData.length];

        InputStream input = new ByteArrayInputStream(inputData);
        OutputStream output = new ByteArrayOutputStream(inputData.length);
        long count = IOUtils.copy(input, output);
        Assert.assertEquals(inputData.length, count);

        byte[] resultData = output.toByteArray();
        Assert.assertArrayEquals(inputData, resultData);
    }

    @Test
    public void testCopyWithBuffersize() throws IOException {
        byte[] inputData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] outputData = new byte[inputData.length];

        InputStream input = new ByteArrayInputStream(inputData);
        OutputStream output = new ByteArrayOutputStream(inputData.length);
        long count = IOUtils.copy(input, output, 5);
        Assert.assertEquals(inputData.length, count);

        byte[] resultData = output.toByteArray();
        Assert.assertArrayEquals(inputData, resultData);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] outputData = new byte[inputData.length];

        InputStream input = new ByteArrayInputStream(inputData);
        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);
        Assert.assertEquals(numToSkip, skipped + inputData.length - 5);

        int read = IOUtils.readFully(input, outputData, 0, outputData.length);
        Assert.assertEquals(inputData.length - 5, read);

        byte[] resultData = new byte[inputData.length - 5];
        System.arraycopy(outputData, 0, resultData, 0, resultData.length);
        Assert.assertArrayEquals(resultData, inputData, 5);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] outputData = new byte[inputData.length];

        InputStream input = new ByteArrayInputStream(inputData);
        int read = IOUtils.readFully(input, outputData);
        Assert.assertEquals(inputData.length, read);

        Assert.assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        byte[] outputData = IOUtils.toByteArray(new ByteArrayInputStream(inputData));
        Assert.assertArrayEquals(inputData, outputData);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }

    @Test(expected = IOException.class)
    public void testCloseQuietlyWithError() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(outputStream);
    }
}