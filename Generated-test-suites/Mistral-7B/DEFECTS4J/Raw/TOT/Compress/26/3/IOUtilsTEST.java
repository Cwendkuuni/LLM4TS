import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IOUtils Test Cases
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals("Test Data", new String(output.toByteArray()), "Copied data should be the same.");
        Assert.assertEquals(13, actual, "Number of bytes copied should be 13.");
    }

    @Test
    public void testCopyWithCustomBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());

        long actual = IOUtils.copy(input, output, 5);
        Assert.assertEquals("Test Data", new String(output.toByteArray()), "Copied data should be the same.");
        Assert.assertEquals(13, actual, "Number of bytes copied should be 13.");
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());

        long actual = IOUtils.skip(input, 6);
        Assert.assertEquals(7, actual, "Actually skipped 7 bytes, expected 6.");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidParameters() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] b = new byte[10];

        IOUtils.readFully(input, b, 5, 5);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] b = new byte[13];

        IOUtils.readFully(input, b);

        Assert.assertEquals("Test Data", new String(b), "Read data should be the same.");
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());

        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertEquals("Test Data", new String(actual), "Converted data should be the same.");
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());

        IOUtils.closeQuietly(input);
    }
}