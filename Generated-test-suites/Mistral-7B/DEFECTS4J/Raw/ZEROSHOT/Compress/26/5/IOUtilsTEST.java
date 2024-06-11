import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Test class for IOUtils class
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long copyBytes = IOUtils.copy(input, output);
        Assert.assertEquals("Incorrect number of bytes copied", "Test String".length(), copyBytes);
        byte[] expectedOutputBytes = "Test String".getBytes();
        byte[] actualOutputBytes = output.toByteArray();
        Assert.assertArrayEquals("Output bytes do not match expected bytes", expectedOutputBytes, actualOutputBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long copyBytes = IOUtils.copy(input, output, 10);
        Assert.assertEquals("Incorrect number of bytes copied", "Test String".length(), copyBytes);
    }

    @Test(expected = IOException.class)
    public void testCopyThrowsIOException() throws IOException {
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException();
            }

            @Override
            public void close() throws IOException {
                // not used
            }
        };
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        IOUtils.copy(input, outputStream);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long skippedBytes = IOUtils.skip(input, 3);
        Assert.assertEquals("Incorrect number of bytes skipped", 3, skippedBytes);
        byte[] expectedOutputBytes = "Test String".getBytes();
        byte[] actualOutputBytes = output.toByteArray();
        Assert.assertArrayEquals("Output bytes do not match expected bytes", expectedOutputBytes, actualOutputBytes);
    }

    @Test
    public void testSkipLessThanRequested() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long skippedBytes = IOUtils.skip(input, 10);
        Assert.assertEquals("Incorrect number of bytes skipped", "Test String".length(), skippedBytes);
        byte[] expectedOutputBytes = "Test String".getBytes();
        byte[] actualOutputBytes = output.toByteArray();
        Assert.assertArrayEquals("Output bytes do not match expected bytes", expectedOutputBytes, actualOutputBytes);
    }

    @Test(expected = IOException.class)
    public void testSkipThrowsIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]) {
            @Override
            public long skip(long n) throws IOException {
                throw new IOException();
            }
        };
        IOUtils.skip(input, 3);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[input.available()];
        int readBytes = IOUtils.readFully(input, buffer);
        Assert.assertEquals("Incorrect number of bytes read", "Test String".length(), readBytes);
    }

    @Test
    public void testReadFullyLessThanAvailable() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] buffer = new byte[5];
        int readBytes = IOUtils.readFully(input, buffer);
        Assert.assertEquals("Incorrect number of bytes read", 5, readBytes);
    }

    @Test(expected = IOException.class)
    public void testReadFullyThrowsIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        byte[] buffer = new byte[5];
        IOUtils.readFully(input, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expectedByteArray = "Test String".getBytes();
        byte[] actualByteArray = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("Output byte array does not match expected byte array", expectedByteArray, actualByteArray);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCloseQuietlyException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCloseQuietlyNull() {
        IOUtils.closeQuietly(null);
    }
}