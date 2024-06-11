import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    // Test for copy() method
    @Test
    public void testCopy() throws IOException {
        // Test typical use case
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        Assert.assertEquals(13, count);
        Assert.assertEquals("Hello, World!", output.toString());

        // Test edge case with empty input
        input = new ByteArrayInputStream(new byte[0]);
        output = new ByteArrayOutputStream();
        count = IOUtils.copy(input, output);
        Assert.assertEquals(0, count);
        Assert.assertEquals("", output.toString());

        // Test error scenario with null input
        input = null;
        output = new ByteArrayOutputStream();
        try {
            count = IOUtils.copy(input, output);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("input is null", e.getMessage());
        }

        // Test error scenario with null output
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        output = null;
        try {
            count = IOUtils.copy(input, output);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("output is null", e.getMessage());
        }
    }

    // Test for skip() method
    @Test
    public void testSkip() throws IOException {
        // Test typical use case
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        long skipped = IOUtils.skip(input, 7);
        Assert.assertEquals(7, skipped);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        long count = IOUtils.copy(input, output);
        Assert.assertEquals(6, count);
        Assert.assertEquals("World!", output.toString());

        // Test edge case with empty input
        input = new ByteArrayInputStream(new byte[0]);
        skipped = IOUtils.skip(input, 5);
        Assert.assertEquals(0, skipped);
        output = new ByteArrayOutputStream();
        count = IOUtils.copy(input, output);
        Assert.assertEquals(0, count);
        Assert.assertEquals("", output.toString());

        // Test error scenario with null input
        input = null;
        try {
            skipped = IOUtils.skip(input, 5);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("input is null", e.getMessage());
        }

        // Test error scenario with negative numToSkip
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        try {
            skipped = IOUtils.skip(input, -5);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("numToSkip is negative", e.getMessage());
        }
    }

    // Test for readFully() method
    @Test
    public void testReadFully() throws IOException {
        // Test typical use case
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] buffer = new byte[13];
        int count = IOUtils.readFully(input, buffer);
        Assert.assertEquals(13, count);
        Assert.assertEquals("Hello, World!", new String(buffer));

        // Test edge case with empty input
        input = new ByteArrayInputStream(new byte[0]);
        buffer = new byte[10];
        count = IOUtils.readFully(input, buffer);
        Assert.assertEquals(0, count);
        Assert.assertArrayEquals(new byte[10], buffer);

        // Test error scenario with null input
        input = null;
        buffer = new byte[10];
        try {
            count = IOUtils.readFully(input, buffer);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("input is null", e.getMessage());
        }

        // Test error scenario with negative offset
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        buffer = new byte[10];
        try {
            count = IOUtils.readFully(input, buffer, -5, 5);
            Assert.fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Offset is negative", e.getMessage());
        }

        // Test error scenario with invalid length
        input = new ByteArrayInputStream("Hello, World!".getBytes());
        buffer = new byte[10];
        try {
            count = IOUtils.readFully(input, buffer, 5, 10);
            Assert.fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals("Length exceeds buffer bounds", e.getMessage());
        }
    }

    // Test for toByteArray() method
    @Test
    public void testToByteArray() throws IOException {
        // Test typical use case
        InputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        byte[] result = IOUtils.toByteArray(input);
        Assert.assertEquals("Hello, World!", new String(result));

        // Test edge case with empty input
        input = new ByteArrayInputStream(new byte[0]);
        result = IOUtils.toByteArray(input);
        Assert.assertEquals("", new String(result));

        // Test error scenario with null input
        input = null;
        try {
            result = IOUtils.toByteArray(input);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("input is null", e.getMessage());
        }
    }

    // Test for closeQuietly() method
    @Test
    public void testCloseQuietly() throws IOException {
        // Test typical use case
        ByteArrayInputStream input = new ByteArrayInputStream("Hello, World!".getBytes());
        IOUtils.closeQuietly(input); // Should not throw exception

        // Test edge case with null Closeable
        Closeable closeable = null;
        IOUtils.closeQuietly(closeable);

        // Test error scenario with IOException
        input = new ByteArrayInputStream("Hello, World!".getBytes()) {
            @Override
            public void close() throws IOException {
                super.close();
                throw new IOException("Error closing input stream");
            }
        };
        try {
            IOUtils.closeQuietly(input);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Error closing input stream", e.getMessage());
        }
    }
}