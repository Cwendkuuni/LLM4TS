package org.apache.commons.compress.utils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class IOUtilsTest {

    private final String testString = "This is a test string";
    private final InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

    @Test
    public void testCopy() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long copyCount = IOUtils.copy(testInputStream, outputStream);
        Assert.assertEquals(testString.length(), copyCount);
        Assert.assertEquals(testString, outputStream.toString());
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bufferSize = 4;
        long copyCount = IOUtils.copy(testInputStream, outputStream, bufferSize);
        Assert.assertEquals(testString.length(), copyCount);
        Assert.assertEquals(testString, outputStream.toString());
    }

    @Test
    public void testCopyWithEmptyInput() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long copyCount = IOUtils.copy(emptyInputStream, outputStream);
        Assert.assertEquals(0, copyCount);
        Assert.assertEquals("", outputStream.toString());
    }

    @Test
    public void testSkip() throws IOException {
        long numToSkip = 5;
        long skipped = IOUtils.skip(testInputStream, numToSkip);
        Assert.assertEquals(numToSkip, skipped);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(testString.substring(5), outputStream.toString());
    }

    @Test
    public void testSkipWithZeroSkip() throws IOException {
        long numToSkip = 0;
        long skipped = IOUtils.skip(testInputStream, numToSkip);
        Assert.assertEquals(numToSkip, skipped);
    }

    @Test
    public void testSkipWithNegativeSkip() throws IOException {
        long numToSkip = -5;
        long skipped = IOUtils.skip(testInputStream, numToSkip);
        Assert.assertEquals(0, skipped);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(testString, outputStream.toString());
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] buffer = new byte[testString.length()];
        int bytesRead = IOUtils.readFully(testInputStream, buffer);
        Assert.assertEquals(testString.length(), bytesRead);
        Assert.assertEquals(testString, new String(buffer));
    }

    @Test
    public void testReadFullyWithOffsetAndLen() throws IOException {
        byte[] buffer = new byte[testString.length() * 2];
        int offset = testString.length() / 2;
        int len = testString.length();
        int bytesRead = IOUtils.readFully(testInputStream, buffer, offset, len);
        Assert.assertEquals(len, bytesRead);
        Assert.assertEquals(testString, new String(buffer, offset, len));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeLen() throws IOException {
        byte[] buffer = new byte[testString.length()];
        int offset = 0;
        int len = -1;
        IOUtils.readFully(testInputStream, buffer, offset, len);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[testString.length()];
        int offset = -1;
        int len = testString.length();
        IOUtils.readFully(testInputStream, buffer, offset, len);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFullyWithOffsetAndLenOutOfBounds() throws IOException {
        byte[] buffer = new byte[testString.length()];
        int offset = testString.length() / 2;
        int len = testString.length() * 2;
        IOUtils.readFully(testInputStream, buffer, offset, len);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] byteArray = IOUtils.toByteArray(testInputStream);
        Assert.assertEquals(testString, new String(byteArray));
    }

    @Test
    public void testToByteArrayWithEmptyInput() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream("".getBytes());
        byte[] byteArray = IOUtils.toByteArray(emptyInputStream);
        Assert.assertEquals(0, byteArray.length);
    }

    @Test
    public void testCloseQuietly() {
        Closeable closeable = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        IOUtils.closeQuietly(closeable);
        // No exception should be thrown
    }
}