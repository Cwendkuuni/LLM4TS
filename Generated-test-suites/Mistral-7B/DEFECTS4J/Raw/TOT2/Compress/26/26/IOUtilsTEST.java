package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class IOUtilsTest {

    @Test
    public void testCopyMethod() throws IOException {
        String inputText = "This is a test.";
        byte[] inputBytes = inputText.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(inputBytes), outputStream);
        byte[] outputBytes = outputStream.toByteArray();

        Assert.assertEquals(inputBytes.length, copiedBytes);
        Assert.assertArrayEquals(inputBytes, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testCopyMethodWithIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        OutputStream output = new ByteArrayOutputStream();

        IOUtils.copy(input, output);
    }

    @Test
    public void testCopyMethodWithBufferSize() throws IOException {
        String inputText = "This is a test.";
        byte[] inputBytes = inputText.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(inputBytes), outputStream, 256);
        byte[] outputBytes = outputStream.toByteArray();

        Assert.assertEquals(inputBytes.length, copiedBytes);
        Assert.assertArrayEquals(inputBytes, outputBytes);
    }

    @Test(expected = IOException.class)
    public void testCopyMethodWithIOExceptionAndBufferSize() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        IOUtils.copy(input, outputStream, 256);
    }

    @Test
    public void testSkipMethod() throws IOException {
        String inputText = "This is a test.";
        byte[] inputBytes = inputText.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        long skippedBytes = IOUtils.skip(inputStream, 10);
        Assert.assertEquals(10, skippedBytes);
    }

    @Test(expected = IOException.class)
    public void testSkipMethodWithIOException() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);

        IOUtils.skip(input, 5);
    }

    @Test
    public void testSkipMethodWithEndOfStream() throws IOException {
        String inputText = "This is a test.";
        byte[] inputBytes = inputText.getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);

        long skippedBytes = IOUtils.skip(inputStream, inputBytes.length + 1);
        Assert.assertEquals(inputBytes.length, skippedBytes);
    }
}