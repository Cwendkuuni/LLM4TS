import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        // Typical usage scenario
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        Assert.assertEquals(13, count);
        Assert.assertEquals("Hello, World!", output.toString());
        
        // Edge cases
        input = new ByteArrayInputStream(new byte[0]); // Empty input stream
        output = new ByteArrayOutputStream();
        count = IOUtils.copy(input, output);
        Assert.assertEquals(0, count);
        Assert.assertEquals("", output.toString());
        
        input = new ByteArrayInputStream("1234567890".getBytes()); // Large input
        output = new ByteArrayOutputStream();
        count = IOUtils.copy(input, output, 5);
        Assert.assertEquals(10, count);
        Assert.assertEquals("1234567890", output.toString());
        
        // Error scenarios
        input = null; // Null input stream
        output = new ByteArrayOutputStream();
        try {
            count = IOUtils.copy(input, output);
            Assert.fail("copy() should throw IOException for null input stream");
        } catch (IOException e) {
            // Expected
        }
        
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        output = null; // Null output stream
        try {
            count = IOUtils.copy(input, output);
            Assert.fail("copy() should throw IOException for null output stream");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testSkip() throws IOException {
        // Typical usage scenario
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        long skipped = IOUtils.skip(input, 7);
        Assert.assertEquals(7, skipped);
        
        // Edge cases
        input = new ByteArrayInputStream(new byte[0]); // Empty input stream
        skipped = IOUtils.skip(input, 10);
        Assert.assertEquals(0, skipped);
        
        input = new ByteArrayInputStream("1234567890".getBytes()); // Large input
        skipped = IOUtils.skip(input, 12);
        Assert.assertEquals(10, skipped);
        
        // Error scenarios
        input = null; // Null input stream
        try {
            skipped = IOUtils.skip(input, 5);
            Assert.fail("skip() should throw IOException for null input stream");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testReadFully() throws IOException {
        // Typical usage scenario
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[5];
        int read = IOUtils.readFully(input, buffer);
        Assert.assertEquals(5, read);
        Assert.assertEquals("Hello", new String(buffer));
        
        // Edge cases
        input = new ByteArrayInputStream(new byte[0]); // Empty input stream
        buffer = new byte[10];
        read = IOUtils.readFully(input, buffer);
        Assert.assertEquals(0, read);
        Assert.assertEquals("", new String(buffer));
        
        input = new ByteArrayInputStream("12345".getBytes()); // Small input
        buffer = new byte[10];
        read = IOUtils.readFully(input, buffer, 5, 5);
        Assert.assertEquals(5, read);
        Assert.assertEquals("0000012345", new String(buffer));
        
        // Error scenarios
        input = null; // Null input stream
        buffer = new byte[5];
        try {
            read = IOUtils.readFully(input, buffer);
            Assert.fail("readFully() should throw IOException for null input stream");
        } catch (IOException e) {
            // Expected
        }
        
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        buffer = null; // Null buffer
        try {
            read = IOUtils.readFully(input, buffer);
            Assert.fail("readFully() should throw IOException for null buffer");
        } catch (IOException e) {
            // Expected
        }
        
        buffer = new byte[10];
        int offset = -1; // Negative offset
        int length = 5;
        try {
            read = IOUtils.readFully(input, buffer, offset, length);
            Assert.fail("readFully() should throw IndexOutOfBoundsException for negative offset");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        
        offset = 0;
        length = 15; // Offset + Length exceeds buffer length
        try {
            read = IOUtils.readFully(input, buffer, offset, length);
            Assert.fail("readFully() should throw IndexOutOfBoundsException for offset + length > buffer length");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testToByteArray() throws IOException {
        // Typical usage scenario
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] byteArray = IOUtils.toByteArray(input);
        Assert.assertEquals("Hello, World!", new String(byteArray));
        
        // Edge cases
        input = new ByteArrayInputStream(new byte[0]); // Empty input stream
        byteArray = IOUtils.toByteArray(input);
        Assert.assertEquals("", new String(byteArray));
        
        // Error scenarios
        input = null; // Null input stream
        try {
            byteArray = IOUtils.toByteArray(input);
            Assert.fail("toByteArray() should throw IOException for null input stream");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCloseQuietly() {
        // Typical usage scenario
        Closeable closeable = new Closeable() {
            public void close() throws IOException {
                // Do nothing
            }
        };
        IOUtils.closeQuietly(closeable);
        // No assertion needed as it should not throw any exception
        
        // Edge cases
        closeable = null; // Null closeable
        IOUtils.closeQuietly(closeable);
        // No assertion needed as it should not throw any exception
    }
}