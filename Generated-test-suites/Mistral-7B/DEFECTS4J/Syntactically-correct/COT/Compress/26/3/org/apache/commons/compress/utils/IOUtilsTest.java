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
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        byte[] expected = "Test String".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream(5);
        long actual = IOUtils.copy(input, output, 5);
        byte[] expected = "Test String".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testCopyThrowsIOException() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = null;
        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream("Test String".getBytes());
        long numToSkip = 6;
        long actual = IOUtils.skip(is, numToSkip);
        long expected = 6;
        Assert.assertEquals(expected, actual);
        byte[] actualBytes = bos.toByteArray();
        byte[] expectedBytes = "Test      ".getBytes();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[10];
        int actual = IOUtils.readFully(input, buffer);
        int expected = 10;
        Assert.assertEquals(expected, actual);
        byte[] actualBytes = new byte[actual];
        System.arraycopy(buffer, 0, actualBytes, 0, actual);
        byte[] expectedBytes = "Test String".getBytes();
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFullyZeroLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[0];
        int actual = IOUtils.readFully(input, buffer);
        int expected = 0;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadFullyNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[10];
        int actual = IOUtils.readFully(input, buffer, -5);
        int expected = 0;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = "Test String".getBytes();
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testToByteArrayEmptyStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = new byte[0];
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        Closeable c = () -> {};
        IOUtils.closeQuietly(c);
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }

    @Test(expected = NullPointerException.class)
    public void testCloseQuietlyNullStream() {
        Closeable c = null;
        IOUtils.closeQuietly(c);
    }

    @Test
    public void testCloseQuietlyIOException() {
        Closeable c = () -> { throw new IOException(); };
        IOUtils.closeQuietly(c);
    }
}