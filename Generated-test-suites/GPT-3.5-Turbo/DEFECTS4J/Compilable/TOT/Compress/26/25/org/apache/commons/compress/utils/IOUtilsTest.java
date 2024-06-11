package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class IOUtilsTest {

    // Test for the copy() method

    @Test
    public void testCopyTypical() throws IOException {
        String inputString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        long copyBytes = IOUtils.copy(inputStream, outputStream);
        String outputString = outputStream.toString();
        Assert.assertEquals(inputString.length(), copyBytes);
        Assert.assertEquals(inputString, outputString);
    }

    @Test
    public void testCopyEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        OutputStream outputStream = new ByteArrayOutputStream();
        long copyBytes = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(0, copyBytes);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullInputStream() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(null, outputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyNullOutputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        IOUtils.copy(inputStream, null);
    }

    // Test for the skip() method

    @Test
    public void testSkipTypical() throws IOException {
        String inputString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        long skippableBytes = 10;
        long skippedBytes = IOUtils.skip(inputStream, skippableBytes);
        Assert.assertEquals(skippableBytes, skippedBytes);
        byte[] skippedBytesArray = new byte[inputString.length() - (int) skippableBytes];
        inputStream.read(skippedBytesArray, 0, skippedBytesArray.length);
        String remainingString = new String(skippedBytesArray);
        Assert.assertEquals("test", remainingString);
    }

    @Test
    public void testSkipLessThanNumToSkip() throws IOException {
        String inputString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        long skippableBytes = 20;
        long skippedBytes = IOUtils.skip(inputStream, skippableBytes);
        Assert.assertEquals(inputString.length(), skippedBytes);
        byte[] skippedBytesArray = new byte[inputString.length()];
        int bytesRead = inputStream.read(skippedBytesArray, 0, skippedBytesArray.length);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testSkipNullInputStream() throws IOException {
        IOUtils.skip(null, 10);
    }

    // Test for the readFully() method

    @Test
    public void testReadFullyTypical() throws IOException {
        String inputString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        Assert.assertEquals(5, bytesRead);
        String actualString = new String(buffer);
        Assert.assertEquals("This ", actualString);
    }

    @Test
    public void testReadFullyEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[5];
        int bytesRead = IOUtils.readFully(inputStream, buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidOffset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[5];
        IOUtils.readFully(inputStream, buffer, -1, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyInvalidBufferSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] buffer = new byte[5];
        IOUtils.readFully(inputStream, buffer, 0, buffer.length + 1);
    }

    @Test(expected = NullPointerException.class)
    public void testReadFullyNullInputStream() throws IOException {
        byte[] buffer = new byte[5];
        IOUtils.readFully(null, buffer);
    }

    // Test for the toByteArray() method

    @Test
    public void testToByteArrayTypical() throws IOException {
        String inputString = "This is a test";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes());
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        String outputString = new String(byteArray);
        Assert.assertEquals(inputString, outputString);
    }

    @Test
    public void testToByteArrayEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        Assert.assertEquals(0, byteArray.length);
    }

    @Test(expected = NullPointerException.class)
    public void testToByteArrayNullInputStream() throws IOException {
        IOUtils.toByteArray(null);
    }

    // Test for the closeQuietly() method

    @Test
    public void testCloseQuietlyTypical() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        IOUtils.closeQuietly(closeable);
        // No assertion as this is just to check if there is any exception
    }

    @Test
    public void testCloseQuietlyNullCloseable() {
        IOUtils.closeQuietly(null);
        // No assertion as this is just to check if there is any exception
    }

}

