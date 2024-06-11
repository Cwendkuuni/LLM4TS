package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IOUtils test cases
 */
public class IOUtilsTest {

    /**
     * Test copy method
     */
    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals("Unexpected number of bytes copied", 13, bytesCopied);

        byte[] expectedResult = "Test data".getBytes();
        byte[] result = outputStream.toByteArray();
        Assert.assertArrayEquals("Unexpected result of copy method", expectedResult, result);
    }

    /**
     * Test skip method with small number of bytes to skip
     */
    @Test
    public void testSkipSmall() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        long bytesSkipped = IOUtils.skip(inputStream, 3);
        Assert.assertEquals("Unexpected number of bytes skipped", 3, bytesSkipped);

        byte[] expectedResult = "Test data".getBytes();
        byte[] result = outputStream.toByteArray();
        Assert.assertArrayEquals("Unexpected result after skipping bytes", expectedResult, result);
    }

    /**
     * Test skip method with large number of bytes to skip
     */
    @Test
    public void testSkipLarge() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        long bytesToSkip = inputStream.available();
        long bytesSkipped = IOUtils.skip(inputStream, bytesToSkip);

        Assert.assertEquals("Unexpected number of bytes skipped", bytesToSkip, bytesSkipped);

        byte[] expectedResult = new byte[0];
        byte[] result = outputStream.toByteArray();
        Assert.assertArrayEquals("Unexpected result after skipping all bytes", expectedResult, result);
    }

    /**
     * Test readFully method
     */
    @Test
    public void testReadFully() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(inputStream, buffer);

        Assert.assertEquals("Unexpected number of bytes read", 5, bytesRead);

        byte[] expectedResult = "Test data".substring(0, 5).getBytes();
        Assert.assertArrayEquals("Unexpected result of readFully method", expectedResult, buffer);
    }

    /**
     * Test toByteArray method
     */
    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        byte[] result = IOUtils.toByteArray(inputStream);

        byte[] expectedResult = "Test data".getBytes();
        Assert.assertArrayEquals("Unexpected result of toByteArray method", expectedResult, result);
    }

    /**
     * Test closeQuietly method
     */
    @Test
    public void testCloseQuietly() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes());

        IOUtils.closeQuietly(inputStream);
    }
}