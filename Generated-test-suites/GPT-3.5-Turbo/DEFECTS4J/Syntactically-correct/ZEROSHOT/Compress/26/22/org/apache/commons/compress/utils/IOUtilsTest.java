import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.Closeable;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        Assert.assertArrayEquals(inputBytes, output.toByteArray());
        Assert.assertEquals(inputBytes.length, count);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 1024;
        long count = IOUtils.copy(input, output, bufferSize);

        Assert.assertArrayEquals(inputBytes, output.toByteArray());
        Assert.assertEquals(inputBytes.length, count);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        long numToSkip = 5;
        long skipped = IOUtils.skip(input, numToSkip);

        Assert.assertEquals(inputBytes.length - numToSkip, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] buffer = new byte[inputBytes.length];
        int bytesRead = IOUtils.readFully(input, buffer);

        Assert.assertEquals(inputBytes.length, bytesRead);
        Assert.assertArrayEquals(inputBytes, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] buffer = new byte[inputBytes.length];
        int offset = 0;
        int len = inputBytes.length;
        int bytesRead = IOUtils.readFully(input, buffer, offset, len);

        Assert.assertEquals(inputBytes.length, bytesRead);
        Assert.assertArrayEquals(inputBytes, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Test Input".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);

        Assert.assertArrayEquals(inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = null;
        
        IOUtils.closeQuietly(closeable);
        
        // No exception should be thrown
        Assert.assertTrue(true);
    }
}