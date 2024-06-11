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
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actualResult = IOUtils.copy(input, output);
        byte[] expectedResult = "Test string".getBytes();
        byte[] actualResultBytes = output.toByteArray();
        Assert.assertEquals("Unexpected number of bytes copied", expectedResult.length, actualResult);
        Assert.assertArrayEquals("Unexpected result of the copied data", expectedResult, actualResultBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        OutputStream output = new ByteArrayOutputStream(5);
        long actualResult = IOUtils.copy(input, output, 5);
        byte[] expectedResult = "Test string".getBytes();
        Assert.assertEquals("Unexpected number of bytes copied", expectedResult.length, actualResult);
    }

    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        InputStream input = null;
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        long numToSkip = 5;
        long actualResult = IOUtils.skip(input, numToSkip);
        Assert.assertEquals("Unexpected number of bytes skipped", numToSkip, actualResult);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        byte[] b = new byte[10];
        int actualResult = IOUtils.readFully(input, b);
        Assert.assertEquals("Unexpected number of bytes read", "Test string".getBytes().length, actualResult);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithInvalidOffsetAndLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        byte[] b = new byte[10];
        IOUtils.readFully(input, b, -1, 5);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test string".getBytes());
        byte[] actualResult = IOUtils.toByteArray(input);
        byte[] expectedResult = "Test string".getBytes();
        Assert.assertArrayEquals("Unexpected result of the toByteArray method", expectedResult, actualResult);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream("Test string".getBytes());
        IOUtils.closeQuietly(input);
        // no exception expected
    }

}