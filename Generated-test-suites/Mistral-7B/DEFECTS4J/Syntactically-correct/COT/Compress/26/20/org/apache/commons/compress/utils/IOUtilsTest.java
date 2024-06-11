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

    /**
     * Test copy method with valid InputStream and OutputStream
     * @throws IOException
     */
    @Test
    public void testCopyValidInputOutput() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        long actual = IOUtils.copy(input, output);
        byte[] expected = "Test Data".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    /**
     * Test copy method with valid InputStream, OutputStream and custom buffer size
     * @throws IOException
     */
    @Test
    public void testCopyValidInputOutputCustomBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        OutputStream output = new ByteArrayOutputStream(10);
        long actual = IOUtils.copy(input, output, 5);
        byte[] expected = "Test Data".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertEquals(expected.length, actual);
        Assert.assertArrayEquals(expected, actualBytes);
    }

    /**
     * Test skip method with valid InputStream and given number of bytes to skip
     * @throws IOException
     */
    @Test
    public void testSkipValidInputStreamAndBytesToSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        long numToSkip = 5;
        long actual = IOUtils.skip(input, numToSkip);
        long expected = 5;
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test readFully method with valid InputStream and given byte array
     * @throws IOException
     */
    @Test
    public void testReadFullyValidInputStreamAndByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] b = new byte[10];
        int actual = IOUtils.readFully(input, b);
        int expected = 10;
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test readFully method with valid InputStream, given byte array and custom offset and length
     * @throws IOException
     */
    @Test
    public void testReadFullyValidInputStreamByteArrayOffsetLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] b = new byte[10];
        int offset = 3;
        int len = 4;
        int actual = IOUtils.readFully(input, b, offset, len);
        int expected = 4;
        byte[] expectedBytes = new byte[]{'T', 'e', 's', 't'};
        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(expectedBytes, new byte[]{b[offset], b[offset + 1], b[offset + 2], b[offset + 3]});
    }

    /**
     * Test toByteArray method with valid InputStream
     * @throws IOException
     */
    @Test
    public void testToByteArrayValidInputStream() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        byte[] actual = IOUtils.toByteArray(input);
        byte[] expected = "Test Data".getBytes();
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test closeQuietly method with valid Closeable
     */
    @Test
    public void testCloseQuietlyValidCloseable() {
        ByteArrayInputStream input = new ByteArrayInputStream("Test Data".getBytes());
        IOUtils.closeQuietly(input);
        //assert no exception is thrown
    }

    /**
     * Test copy method with null InputStream or OutputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testCopyNullInputStream() throws IOException {
        IOUtils.copy(null, new ByteArrayOutputStream());
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullOutputstream() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        IOUtils.copy(input, null);
    }

    /**
     * Test skip method with null InputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testSkipNullInputStream() throws IOException {
        IOUtils.skip(null, 1);
    }

    /**
     * Test readFully method with null InputStream or byte array
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInputStream() throws IOException {
        IOUtils.readFully(null, new byte[10]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyNullByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        IOUtils.readFully(input, null);
    }

    /**
     * Test toByteArray method with null InputStream
     * @throws IOException
     */
    @Test(expected = NullPointerException.class)
    public void testToByteArrayNullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }
}