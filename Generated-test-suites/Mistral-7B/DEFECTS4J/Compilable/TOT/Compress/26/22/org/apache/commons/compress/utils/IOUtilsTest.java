package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtilsTest.java
 */
public class IOUtilsTest {

    /**
     * Test copy method with valid InputStream and OutputStream
     */
    @Test
    public void testCopyValidStreams() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        long copiedBytes = IOUtils.copy(input, output);
        Assert.assertEquals(13, copiedBytes);
        Assert.assertEquals("test data", output.toString());
    }

    /**
     * Test copy method with valid InputStream, OutputStream and different buffer sizes
     */
    @Test
    public void testCopyWithDifferentBufferSizes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        long copiedBytes = IOUtils.copy(input, output, 1024);
        Assert.assertEquals(13, copiedBytes);
        Assert.assertEquals("test data", output.toString());

        copiedBytes = IOUtils.copy(input, output, 512);
        Assert.assertEquals(13, copiedBytes);
        Assert.assertEquals("test data", output.toString());

        copiedBytes = IOUtils.copy(input, output, 2048);
        Assert.assertEquals(13, copiedBytes);
        Assert.assertEquals("test data", output.toString());
    }

    /**
     * Test skip method with valid InputStream and different numbers of bytes to skip
     */
    @Test
    public void testSkipWithValidInputStreamAndDifferentNumbersOfBytesToSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("test data one two three four five six seven eight nine ten".getBytes());

        long skippedBytes = IOUtils.skip(input, 5);
        Assert.assertEquals(5, skippedBytes);
        byte[] remainingData = output.toByteArray();
        Assert.assertArrayEquals("test data six seven eight nine ten".getBytes(), remainingData);

        skippedBytes = IOUtils.skip(input, 20);
        Assert.assertEquals(15, skippedBytes);
        remainingData = output.toByteArray();
        Assert.assertArrayEquals("two three four five six seven eight nine ten".getBytes(), remainingData);

        skippedBytes = IOUtils.skip(input, 25);
        Assert.assertEquals(18, skippedBytes);
        remainingData = output.toByteArray();
        Assert.assertArrayEquals("four five six seven eight nine ten".getBytes(), remainingData);
    }

    /**
     * Test skip method with valid InputStream and end of stream reached
     */
    @Test
    public void testSkipWithValidInputStreamAndEndOfStreamReached() throws IOException {
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        long skippedBytes = IOUtils.skip(input, 10);
        Assert.assertEquals(13, skippedBytes);
    }

    /**
     * Test readFully method with valid InputStream and byte array
     */
    @Test
    public void testReadFullyWithValidInputStreamAndByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        IOUtils.copy(input, output);
        byte[] expectedData = output.toByteArray();

        byte[] readData = new byte[expectedData.length];
        int bytesRead = IOUtils.readFully(input, readData);
        Assert.assertEquals(expectedData.length, bytesRead);
        Assert.assertArrayEquals(expectedData, readData);
    }

    /**
     * Test toByteArray method with valid InputStream
     */
    @Test
    public void testToByteArrayWithValidInputStream() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream("test data".getBytes());
        byte[] expectedData = output.toByteArray();
        byte[] readData = IOUtils.toByteArray(input);
        Assert.assertArrayEquals(expectedData, readData);
    }
}