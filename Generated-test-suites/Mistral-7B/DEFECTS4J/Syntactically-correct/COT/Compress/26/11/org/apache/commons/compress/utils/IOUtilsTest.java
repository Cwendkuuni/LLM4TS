import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils Test cases
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy function".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        String expected = "Test IOUtils copy function";
        byte[] outputBytes = output.toByteArray();
        Assert.assertEquals(expected.length(), actual);
        Assert.assertEquals(expected, new String(outputBytes));
    }

    @Test (expected = IOException.class)
    public void testCopyWithInvalidInput() throws IOException {
        InputStream input = null;
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output);
    }

    @Test
    public void testCopyWithInvalidOutput() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy function".getBytes());
        OutputStream output = null;
        IOUtils.copy(input, output);
    }

    @Test
    public void testCopyWithDifferentBuffersize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy function".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output, 500);
        String expected = "Test IOUtils copy function";
        byte[] outputBytes = output.toByteArray();
        Assert.assertEquals(expected.length(), actual);
        Assert.assertEquals(expected, new String(outputBytes));
    }

    @Test
    public void testSkip() throws IOException {
        String testString = "Test IOUtils skip function";
        InputStream input = new ByteArrayInputStream(testString.getBytes());
        long numToSkip = 5;
        long actual = IOUtils.skip(input, numToSkip);
        long expected = testString.length() - numToSkip;
        Assert.assertEquals(expected, actual);
    }

    @Test (expected = IOException.class)
    public void testSkipWithInvalidInput() throws IOException {
        InputStream input = null;
        long numToSkip = 5;
        IOUtils.skip(input, numToSkip);
    }

    @Test
    public void testReadFully() throws IOException {
        String testString = "Test IOUtils readFully function";
        InputStream input = new ByteArrayInputStream(testString.getBytes());
        byte[] buffer = new byte[testString.length()];
        int actual = IOUtils.readFully(input, buffer);
        int expected = testString.length();
        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(testString.getBytes(), buffer);
    }

    @Test (expected = IOException.class)
    public void testReadFullyWithInvalidInput() throws IOException {
        InputStream input = null;
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testReadFullyWithInvalidBuffer() throws IOException {
        String testString = "Test IOUtils readFully function";
        InputStream input = new ByteArrayInputStream(testString.getBytes());
        byte[] buffer = new byte[testString.length() + 1];
        int actual = IOUtils.readFully(input, buffer);
        int expected = testString.length();
        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(testString.getBytes(), buffer, 0, testString.length());
    }

    @Test
    public void testToByteArray() throws IOException {
        String testString = "Test IOUtils toByteArray function";
        InputStream input = new ByteArrayInputStream(testString.getBytes());
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = testString.getBytes();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test (expected = IOException.class)
    public void testToByteArrayWithInvalidInput() throws IOException {
        InputStream input = null;
        IOUtils.toByteArray(input);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output);
    }

    @Test
    public void testCloseQuietlyWithException() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test IOUtils closeQuietly function with exception");
            }
        };
        IOUtils.closeQuietly(output);
    }
}