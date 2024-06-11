import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtilsTest
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals("Test String".length(), actual);

        String expected = "Test String";
        String actualString = new String(output.toByteArray());
        Assert.assertEquals(expected, actualString);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream(5);

        long actual = IOUtils.copy(input, output, 5);
        Assert.assertEquals(5, actual);

        String actualString = new String(output.toByteArray());
        Assert.assertEquals("Test S", actualString);
    }

    @Test(expected = IOException.class)
    public void testCopyWithNegativeBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output, -5);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test String".getBytes());

        long actual = IOUtils.skip(inputStream, 8);
        Assert.assertEquals(8L, actual);

        String expected = "Test ";
        String actualString = new String(outputStream.toByteArray());
        Assert.assertEquals(expected, actualString);

        actual = IOUtils.skip(inputStream, 8);
        Assert.assertEquals(0L, actual);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] output = new byte[7];

        int actual = IOUtils.readFully(input, output);
        Assert.assertEquals(7, actual);

        String expected = "Test Str";
        String actualString = new String(output, 0, actual);
        Assert.assertEquals(expected, actualString);

        input = new ByteArrayInputStream("Test String".getBytes());
        output = new byte[5];

        actual = IOUtils.readFully(input, output);
        Assert.assertEquals(5, actual);

        expected = "Test St";
        actualString = new String(output, 0, actual);
        Assert.assertEquals(expected, actualString);

        input = new ByteArrayInputStream("Test String".getBytes());
        output = new byte[10];

        actual = IOUtils.readFully(input, output);
        Assert.assertEquals(7, actual);
    }

    @Test
    public void testReadFullyWithNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] output = new byte[7];

        IOUtils.readFully(input, output, -1);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] output = IOUtils.toByteArray(input);

        String expected = "Test String";
        String actualString = new String(output);
        Assert.assertEquals(expected, actualString);

        byte[] expectedByteArray = "Test String".getBytes();
        Assert.assertArrayEquals(expectedByteArray, output);
    }

    @Test
    public void testToByteArrayWithEmptyInputStream() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] output = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(new byte[0], output);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test String".getBytes());
        IOUtils.closeQuietly(input);
        Assert.assertNull(input.in);
    }
}