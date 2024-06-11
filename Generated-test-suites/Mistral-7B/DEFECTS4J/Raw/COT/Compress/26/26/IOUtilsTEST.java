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
    public void testCopyMethod() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(input, output);
        Assert.assertEquals("Test".length(), result);
        Assert.assertArrayEquals("Test".getBytes(), output.toByteArray());
    }

    @Test
    public void testCopyMethodWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();
        long result = IOUtils.copy(input, output, 5);
        Assert.assertEquals("Test".length(), result);
        Assert.assertArrayEquals("Test".getBytes(), output.toByteArray());
    }

    @Test
    public void testNegativeBufferSize() {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        OutputStream output = new ByteArrayOutputStream();
        try {
            IOUtils.copy(input, output, -5);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSkipMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(("Test".getBytes()).concat(new byte[]{0x0A}));
        long skipped = IOUtils.skip(inputStream, 2);
        long expectedSkipped = 2;
        Assert.assertEquals(expectedSkipped, skipped);
        Assert.assertEquals("Test\n".getBytes().length, inputStream.available());
    }

    @Test
    public void testReadFullyMethod() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer);
        String expected = new String(buffer, 0, bytesRead);
        Assert.assertEquals("Test", expected);
        Assert.assertEquals(5, bytesRead);
    }

    @Test
    public void testReadFullyMethodWithWrongLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(input, buffer, 6, 5);
        Assert.assertEquals(5, bytesRead);
        Assert.assertEquals("Test", new String(buffer));
    }

    @Test
    public void testReadFullyMethodWithNegativeOffset() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] buffer = new byte[5];
        try {
            IOUtils.readFully(input, buffer, 5, -1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testToByteArrayMethod() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[]{"Test".getBytes()});
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Test".getBytes(), result);
    }

    @Test
    public void testCloseQuietlyMethod() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(new byte[]{}), outputStream);
            IOUtils.closeQuietly(outputStream);
            Assert.assertTrue(outputStream.size() == 0);
        } catch (IOException e) {
            // ignored
        }
    }

    @Test
    public void testCloseQuietlyMethodWithNullCloseable() {
        IOUtils.closeQuietly(null);
    }
}