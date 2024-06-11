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
        long actual = IOUtils.copy(input, output);
        Assert.assertEquals("The copied size is not expected", "Test String".length(), actual);

        byte[] expected = "Test String".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertArrayEquals("The copied bytes are not the same", expected, actualBytes);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        long actual = IOUtils.copy(input, output, 5);
        Assert.assertEquals("The copied size is not expected", "Test String".length(), actual);

        byte[] expected = "Test String".getBytes();
        byte[] actualBytes = output.toByteArray();
        Assert.assertArrayEquals("The copied bytes are not the same", expected, actualBytes);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());

        long skipped = IOUtils.skip(input, 3);
        Assert.assertEquals("The number of bytes skipped is not expected", 3, skipped);

        long totalBytes = "Test String".length();
        long bytesToSkip = totalBytes - 3;
        skipped = IOUtils.skip(input, bytesToSkip);
        Assert.assertEquals("The number of bytes skipped is not expected", 0, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());

        byte[] expected = "Test String".getBytes();
        byte[] actual = new byte[expected.length];

        int actualLength = IOUtils.readFully(input, actual);
        Assert.assertEquals("The number of bytes read is not expected", expected.length, actualLength);
        Assert.assertArrayEquals("The read bytes are not the same", expected, actual);
    }

    @Test
    public void testReadFullyWithPartialRead() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expected = new byte[6]; // 3 byte read + 3 byte leftover
        byte[] actual = new byte[expected.length];

        int actualLength = IOUtils.readFully(input, actual);
        Assert.assertEquals("The number of bytes read is not expected", 3, actualLength);

        byte[] remaining = new byte[3];
        int remainingLength = IOUtils.readFully(input, remaining);
        Assert.assertEquals("The number of bytes read is not expected", 3, remainingLength);
        Assert.assertArrayEquals("The remaining bytes read are not the same", remaining, new byte[]{"S", "t", " "});

        actualLength += remainingLength;
        Assert.assertArrayEquals("The read bytes are not the same", expected, actual);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream("Test String".getBytes());
        byte[] expected = "Test String".getBytes();
        byte[] actual = IOUtils.toByteArray(input);
        Assert.assertArrayEquals("The toByteArray result is not the same as expected", expected, actual);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (OutputStream os = output) {
            IOUtils.closeQuietly(os);
        }
    }
}