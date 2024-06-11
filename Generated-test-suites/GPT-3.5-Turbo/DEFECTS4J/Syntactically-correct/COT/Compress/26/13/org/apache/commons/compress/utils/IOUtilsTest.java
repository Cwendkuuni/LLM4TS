import org.junit.*;
import java.io.*;

public class IOUtilsTest {

    // Test case for copy(final InputStream input, final OutputStream output) method
    @Test
    public void testCopy() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        long count = IOUtils.copy(input, output);
        
        Assert.assertEquals(inputBytes.length, count);
        Assert.assertEquals(inputBytes.length, output.toByteArray().length);
        Assert.assertArrayEquals(inputBytes, output.toByteArray());
    }

    // Test case for copy(final InputStream input, final OutputStream output, int buffersize) method
    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        int bufferSize = 100;
        long count = IOUtils.copy(input, output, bufferSize);
        
        Assert.assertEquals(inputBytes.length, count);
        Assert.assertEquals(inputBytes.length, output.toByteArray().length);
        Assert.assertArrayEquals(inputBytes, output.toByteArray());
    }

    // Test case for skip(InputStream input, long numToSkip) method
    @Test
    public void testSkip() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        long numToSkip = 7;
        long skipped = IOUtils.skip(input, numToSkip);
        
        Assert.assertEquals(numToSkip, skipped);
        Assert.assertEquals(7, input.available());
    }

    // Test case for readFully(InputStream input, byte[] b) method
    @Test
    public void testReadFully() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[inputBytes.length];
        
        int bytesRead = IOUtils.readFully(input, buffer);
        
        Assert.assertEquals(inputBytes.length, bytesRead);
        Assert.assertArrayEquals(inputBytes, buffer);
    }

    // Test case for readFully(InputStream input, byte[] b, int offset, int len) method
    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        byte[] buffer = new byte[inputBytes.length];
        int offset = 3;
        int len = 5;
        
        int bytesRead = IOUtils.readFully(input, buffer, offset, len);
        
        Assert.assertEquals(len, bytesRead);
        Assert.assertArrayEquals("lo, Wo".getBytes(), buffer);
    }

    // Test case for toByteArray(final InputStream input) method
    @Test
    public void testToByteArray() throws IOException {
        byte[] inputBytes = "Hello, World!".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(inputBytes);
        
        byte[] result = IOUtils.toByteArray(input);
        
        Assert.assertArrayEquals(inputBytes, result);
    }

    // Test case for closeQuietly(Closeable c) method
    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        
        IOUtils.closeQuietly(input);
        
        Assert.assertTrue(input.read() == -1);
    }
}

