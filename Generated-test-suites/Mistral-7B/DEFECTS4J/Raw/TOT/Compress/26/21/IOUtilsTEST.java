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
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());

        long actual = IOUtils.copy(input, output);
        long expected = input.available();

        Assert.assertEquals("Incorrect number of bytes copied", expected, actual);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());

        long actual = IOUtils.copy(input, output, 10);
        long expected = input.available();

        Assert.assertEquals("Incorrect number of bytes copied", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testCopyWithIOException() throws IOException {
        ByteArrayOutputStream output = null;
        InputStream input = new ByteArrayInputStream("Test String".getBytes());

        IOUtils.copy(input, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test String".getBytes());

        long actual = IOUtils.skip(input, 10);
        long expected = input.available() - 10;

        Assert.assertEquals("Incorrect number of bytes skipped", expected, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[10];

        int actual = IOUtils.readFully(input, buffer);
        int expected = 10;

        Assert.assertArrayEquals("Incorrect number of bytes read", expected, buffer);
    }

    @Test
    public void testReadFullyLessBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[20];

        int actual = IOUtils.readFully(input, buffer, 10, 10);
        int expected = 10;

        Assert.assertEquals("Incorrect number of bytes read", expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithIOException() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = null;
        byte[] buffer = new byte[10];

        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expected = "Test String".getBytes();

        byte[] actual = IOUtils.toByteArray(input);

        Assert.assertArrayEquals("Incorrect byte array", expected, actual);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (OutputStream os = output) {
            IOUtils.copy(new ByteArrayInputStream("Test String".getBytes()), os);
        }

        IOUtils.closeQuietly(output);
    }
}