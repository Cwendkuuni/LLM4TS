import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        long count = IOUtils.copy(input, output);

        Assert.assertEquals("Number of bytes copied should be equal to input length", inputBytes.length, count);
        Assert.assertEquals("Output should be equal to input", "Hello World", output.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int bufferSize = 4;
        long count = IOUtils.copy(input, output, bufferSize);

        Assert.assertEquals("Number of bytes copied should be equal to input length", inputBytes.length, count);
        Assert.assertEquals("Output should be equal to input", "Hello World", output.toString());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        long numToSkip = 6;
        long skipped = IOUtils.skip(input, numToSkip);
        
        Assert.assertEquals("Number of bytes skipped should be equal to numToSkip", numToSkip, skipped);
        Assert.assertEquals("Remaining bytes should be 'World'", "World", input.toString());
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[11];

        int count = IOUtils.readFully(input, outputBytes);

        Assert.assertEquals("Number of bytes read should be equal to outputBytes length", outputBytes.length, count);
        Assert.assertEquals("Output should be equal to input", "Hello World", new String(outputBytes));
    }

    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] outputBytes = new byte[5];

        int offset = 6;
        int len = 5;
        int count = IOUtils.readFully(input, outputBytes, offset, len);

        Assert.assertEquals("Number of bytes read should be equal to len", len, count);
        Assert.assertEquals("Output should be equal to 'World'", "World", new String(outputBytes));
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello World".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);

        byte[] outputBytes = IOUtils.toByteArray(input);

        Assert.assertArrayEquals("Output should be equal to inputBytes", inputBytes, outputBytes);
    }

    @Test
    public void testCloseQuietly() {
        Closeable c = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException("Test Exception");
            }
        };

        IOUtils.closeQuietly(c);
        // No exception should be thrown when trying to close a Closeable
    }
}