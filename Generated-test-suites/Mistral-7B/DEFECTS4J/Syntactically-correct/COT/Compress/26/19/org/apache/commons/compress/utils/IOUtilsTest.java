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
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long count = IOUtils.copy(input, output);
        Assert.assertEquals("Test String".length(), count);
        Assert.assertEquals("Test String".getBytes(), output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long count = IOUtils.copy(input, output, 5);
        Assert.assertNotEquals(0, count);
        Assert.assertEquals("Test String".getBytes().length, count);
    }

    @Test(expected = IOException.class)
    public void testCopyWithNegativeBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();
        IOUtils.copy(input, output, -1);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long skipped = IOUtils.skip(input, 1);
        Assert.assertEquals(1, skipped);
    }

    @Test
    public void testSkipWithZero() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long skipped = IOUtils.skip(input, 0);
        Assert.assertEquals(0, skipped);
    }

    @Test
    public void testSkipWithNegativeNumber() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long skipped = IOUtils.skip(input, -1);
        Assert.assertEquals(0, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(input, buffer);
        Assert.assertArrayEquals("Test String".substring(0, read).getBytes(), buffer);
    }

    @Test
    public void testReadFullyWithNegativeLength() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(input, buffer, -1, 5);
        Assert.assertEquals(0, read);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeOffset() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLengthAndOffset() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer, -1, 5, 3);
    }

    @Test
    public void testReadFullyWithLongerLengthThanAvailable() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[10];
        int read = IOUtils.readFully(input, buffer, 0, "Test String".getBytes().length);
        Assert.assertEquals("Test String".getBytes().length, read);
        Assert.assertArrayEquals("Test String".getBytes(), buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] output = IOUtils.toByteArray(input);
        Assert.assertEquals("Test String".getBytes(), output);
    }

    @Test
    public void testToByteArrayWithNullInput() {
        try {
            IOUtils.toByteArray(null);
            Assert.fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        OutputStream test = output;
        IOUtils.closeQuietly(test);
        Assert.assertNotEquals(output, test);
        Assert.assertNull(test.toString());
    }
}