import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals("Number of copied bytes", "Test String".length(), copiedBytes);

        byte[] outputBytes = output.toByteArray();
        String outputString = new String(outputBytes);
        Assert.assertEquals("Copied string", "Test String", outputString);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(input, output, 5);
        Assert.assertEquals("Number of copied bytes", 5, copiedBytes);

        byte[] outputBytes = output.toByteArray();
        String outputString = new String(outputBytes);
        Assert.assertEquals("Copied string", "Test S", outputString);
    }

    @Test(expected = IOException.class)
    public void testCopyWithNegativeBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output, -5);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream("TestString".getBytes());

        long numToSkip = 3;
        long skipped = IOUtils.skip(input, numToSkip);

        Assert.assertEquals("Number of skipped bytes", 3, skipped);

        byte[] remainingBytes = new byte[11];
        int readBytes = IOUtils.readFully(input, remainingBytes, 0, remainingBytes.length);

        Assert.assertArrayEquals("Remaining bytes", "estString".getBytes(), remainingBytes, 0, remainingBytes.length);
    }

    @Test(expected = IOException.class)
    public void testSkipWithNegativeNumToSkip() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes());

        long numToSkip = -3;
        IOUtils.skip(input, numToSkip);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes());
        byte[] buffer = new byte[11];

        int readBytes = IOUtils.readFully(input, buffer, 0, buffer.length);

        Assert.assertEquals("Number of read bytes", 11, readBytes);
        Assert.assertArrayEquals("Read bytes", "TestString".getBytes(), buffer, 0, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeOffset() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes());
        byte[] buffer = new byte[11];

        IOUtils.readFully(input, buffer, -1, 11);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLength() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes());
        byte[] buffer = new byte[11];

        IOUtils.readFully(input, buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithOffsetLengthGreaterThanBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream("TestString".getBytes());
        byte[] buffer = new byte[5];

        IOUtils.readFully(input, buffer, 1, buffer.length);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testCloseQuietlyWithException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };

        IOUtils.closeQuietly(outputStream);
    }
}