package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IOUtilsTest class to test IOUtils methods
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("TestString".getBytes());

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals("Incorrect number of bytes copied", 13, bytesCopied);

        byte[] expectedOutput = "TestString".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("TestString".getBytes());

        long bytesCopied = IOUtils.copy(inputStream, outputStream, 5);
        Assert.assertEquals("Incorrect number of bytes copied", 3, bytesCopied);

        byte[] expectedOutput = " Tes".getBytes();
        byte[] actualOutput = outputStream.toByteArray();

        Assert.assertArrayEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());

        long bytesSkipped = IOUtils.skip(inputStream, 7);
        Assert.assertEquals("Incorrect number of bytes skipped", 3, bytesSkipped);

        byte[] expectedOutput = "Test".getBytes();
        byte[] actualOutput = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("TestString".getBytes());

        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(inputStream, buffer);

        Assert.assertEquals("Incorrect number of bytes read", 13, bytesRead);
        byte[] expectedOutput = "TestString".getBytes();

        Assert.assertArrayEquals("Output does not match expected", expectedOutput, buffer);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("TestString12345678901234567890".getBytes());

        byte[] buffer = new byte[10];
        int bytesRead = IOUtils.readFully(inputStream, buffer, 3, 5);

        Assert.assertEquals("Incorrect number of bytes read", 5, bytesRead);

        String expectedOutput = "est5";
        String actualOutput = new String(buffer, 0, bytesRead);

        Assert.assertEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("TestString".getBytes());

        byte[] expectedOutput = "TestString".getBytes();
        byte[] actualOutput = IOUtils.toByteArray(inputStream);

        Assert.assertArrayEquals("Output does not match expected", expectedOutput, actualOutput);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());

        IOUtils.closeQuietly(inputStream);

        // No exception thrown, so test passed
    }

    @Test(expected = IOException.class)
    public void testCloseQuietlyWithIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());
        throw new IOException("Test IOException");
    }
}