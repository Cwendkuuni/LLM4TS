import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils Test Case
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid arguments
     * @throws IOException
     */
    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy method".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        long result = IOUtils.copy(input, output);
        Assert.assertEquals(result, 38L); // length of "Test IOUtils copy method"

        byte[] expected = "Test IOUtils copy method".getBytes();
        byte[] resultByteArray = output.toByteArray();
        Assert.assertArrayEquals(expected, resultByteArray);
    }

    /**
     * Test copy method with null InputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullInputStream() throws IOException {
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(null, output);
    }

    /**
     * Test copy method with null OutputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testCopyWithNullOutputStream() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy method".getBytes());
        IOUtils.copy(input, null);
    }

    /**
     * Test copy method with different InputStream and OutputStream
     * @throws IOException
     */
    @Test
    public void testCopyWithDifferentStreams() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils copy method".getBytes());
        OutputStream output1 = new ByteArrayOutputStream();
        OutputStream output2 = new ByteArrayOutputStream();

        long result = IOUtils.copy(input, output1);
        Assert.assertEquals(result, 38L); // length of "Test IOUtils copy method"

        byte[] expected = "Test IOUtils copy method".getBytes();
        byte[] output1ByteArray = output1.toByteArray();
        byte[] output2ByteArray = output2.toByteArray();

        Assert.assertArrayEquals(expected, output1ByteArray);
        Assert.assertNotEquals(output1ByteArray, output2ByteArray);

        OutputStream output3 = new ByteArrayOutputStream();
        IOUtils.copy(output1, output3);
        Assert.assertArrayEquals(expected, output3.toByteArray());
    }

    /**
     * Test skip method with valid arguments
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils skip method".getBytes());
        long expected = 16L; // length of "Test IOUtils skip method" minus 5

        long skipped = IOUtils.skip(input, expected);
        Assert.assertEquals(skipped, expected);

        long remaining = 5L;
        long skipped2 = IOUtils.skip(input, remaining);
        Assert.assertEquals(skipped2, remaining);
    }

    /**
     * Test skip method with null InputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testSkipWithNullInputStream() throws IOException {
        IOUtils.skip(null, 0);
    }

    /**
     * Test readFully method with valid arguments
     * @throws IOException
     */
    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils readFully method".getBytes());
        byte[] buffer = new byte[10];
        int expected = 11; // length of "Test IOUtils readFully method" minus 2

        int result = IOUtils.readFully(input, buffer, 0, buffer.length);
        Assert.assertEquals(result, expected);

        byte[] resultByteArray = new byte[expected];
        System.arraycopy(buffer, 0, resultByteArray, 0, expected);
        byte[] expectedByteArray = "Test IOUtils readFully method".getBytes();

        Assert.assertArrayEquals(expectedByteArray, resultByteArray);
    }

    /**
     * Test readFully method with null InputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullInputStream() throws IOException {
        byte[] buffer = new byte[10];
        IOUtils.readFully(null, buffer, 0, buffer.length);
    }

    /**
     * Test readFully method with null buffer
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyWithNullBuffer() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils readFully method".getBytes());
        IOUtils.readFully(input, null, 0, 10);
    }

    /**
     * Test readFully method with less bytes than buffer size
     * @throws IOException
     */
    @Test
    public void testReadFullyWithLessBytes() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils readFully method".getBytes());
        byte[] buffer = new byte[10];
        int offset = 0;
        int length = 5;

        int result = IOUtils.readFully(input, buffer, offset, length);
        Assert.assertEquals(result, length);

        byte[] resultByteArray = new byte[5];
        System.arraycopy(buffer, offset, resultByteArray, 0, length);
        byte[] expectedByteArray = "Test IOUtils readFully method".substring(0, 5).getBytes();

        Assert.assertArrayEquals(expectedByteArray, resultByteArray);
    }

    /**
     * Test toByteArray method with valid arguments
     * @throws IOException
     */
    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test IOUtils toByteArray method".getBytes());
        byte[] expected = "Test IOUtils toByteArray method".getBytes();

        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test toByteArray method with null InputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayWithNullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }

    /**
     * Test closeQuietly method with null Closeable
     */
    @Test
    public void testCloseQuietlyWithNullCloseable() {
        IOUtils.closeQuietly(null);
    }
}