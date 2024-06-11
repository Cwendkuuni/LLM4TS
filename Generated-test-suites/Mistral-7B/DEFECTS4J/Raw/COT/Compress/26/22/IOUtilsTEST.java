import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtils test class
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        long actual = IOUtils.copy(input, output);
        Assert.assertEquals(4, actual);
        Assert.assertEquals("Test".getBytes(), output.toByteArray());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        long actual = IOUtils.copy(input, output, 2);
        Assert.assertEquals(4, actual);
        Assert.assertEquals("Test".getBytes(), output.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testCopyWithNullArguments() {
        IOUtils.copy(null, null);
    }

    @Test
    public void testSkip() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'I', 'O', 'U', 'T', 'I', 'L', 'S'});

        long actual = IOUtils.skip(input, 2L);
        Assert.assertEquals(10L, actual);
    }

    @Test
    public void testSkipWithNegativeNumberToSkip() {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        Assert.assertThrows(IndexOutOfBoundsException.class, () -> IOUtils.skip(input, -1L));
    }

    @Test
    public void testReadFully() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        IOUtils.readFully(input, output.toByteArray());
        Assert.assertEquals("Test".getBytes(), output.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testReadFullyWithNullArguments() {
        IOUtils.readFully(null, null);
    }

    @Test
    public void testToByteArray() throws IOException {
        final ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't'});

        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Test".getBytes(), actual);
    }

    @Test(expected = IOException.class)
    public void testToByteArrayWithNullArguments() {
        IOUtils.toByteArray(null);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        IOUtils.closeQuietly(output::close);
    }

    @Test
    public void testCloseQuietlyWithNullStream() {
        IOUtils.closeQuietly(null);
    }
}