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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test String".getBytes());

        long copiedBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals("Incorrect number of bytes copied", 14, copiedBytes);

        byte[] expectedOutput = "Test String".getBytes();
        byte[] actualOutput = outputStream.toByteArray();
        Assert.assertArrayEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test String\nTest String".getBytes());

        long skippedBytes = IOUtils.skip(inputStream, 6);
        Assert.assertEquals("Incorrect number of bytes skipped", 6, skippedBytes);

        byte[] actualOutput = outputStream.toByteArray();
        Assert.assertEquals("Output does not match expected", "Test String\n    ", new String(actualOutput));
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test String".getBytes());

        IOUtils.readFully(inputStream, new byte[10]);

        byte[] actualOutput = outputStream.toByteArray();
        Assert.assertEquals("Output does not match expected", "Test String", new String(actualOutput));
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test String".getBytes());

        byte[] expectedOutput = "Test String".getBytes();
        byte[] actualOutput = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test String".getBytes());

        IOUtils.closeQuietly(inputStream);

        try {
            inputStream.read();
            Assert.fail("Expected InputStream to be closed");
        } catch (IOException e) {
            // Expected
        }
    }
}