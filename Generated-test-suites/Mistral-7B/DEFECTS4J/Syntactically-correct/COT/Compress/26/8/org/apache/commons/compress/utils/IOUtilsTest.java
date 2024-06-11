import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy method".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        byte[] expected = "Test IOUtils copy method".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy method with buffer size".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output, 5);
        byte[] expected = "Test IOUtils copy method with buffer size".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy method with IOException".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output, 8024); // Intentionally causing IOException
    }

    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils skip method".getBytes());
        long numToSkip = 4;
        long actualSkipped = IOUtils.skip(input, numToSkip);
        byte[] expectedBytes = new byte[11];
        input.read(expectedBytes);
        byte[] actualBytes = new byte[11];
        input.read(actualBytes, 4);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
        Assert.assertEquals(numToSkip - 3, actualSkipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils readFully method".getBytes());
        byte[] expected = "Test IOUtils readFully method".getBytes();
        byte[] buffer = new byte[expected.length];
        int actual = IOUtils.readFully(input, buffer);
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, buffer);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithIOException() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils readFully method with IOException".getBytes());
        byte[] buffer = new byte[10];
        IOUtils.readFully(input, buffer); // Intentionally causing IOException
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils toByteArray method".getBytes());
        byte[] expected = "Test IOUtils toByteArray method".getBytes();
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtils.closeQuietly(outputStream);
        } finally {
            Assert.assertFalse(outputStream.isOpen());
        }
    }
}