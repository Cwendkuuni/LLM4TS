package org.apache.commons.compress.utils;
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
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});

        long bytesCopied = IOUtils.copy(inputStream, outputStream);
        Assert.assertEquals(3, bytesCopied);
        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, output);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});

        long bytesCopied = IOUtils.copy(inputStream, outputStream, 1);
        Assert.assertEquals(1, bytesCopied);

        bytesCopied = IOUtils.copy(inputStream, outputStream, 2);
        Assert.assertEquals(2, bytesCopied);

        bytesCopied = IOUtils.copy(inputStream, outputStream, 3);
        Assert.assertEquals(1, bytesCopied);

        byte[] output = outputStream.toByteArray();
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, output);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});

        long skipped = IOUtils.skip(inputStream, 3);
        Assert.assertEquals(3, skipped);

        byte[] remainingBytes = new byte[1];
        int read = inputStream.read(remainingBytes);
        Assert.assertEquals(1, read);
        Assert.assertEquals(4, remainingBytes[0]);
    }

    @Test
    public void testReadFully() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[5];

        int bytesReadFully = IOUtils.readFully(inputStream, buffer);
        Assert.assertEquals(5, bytesReadFully);
        Assert.assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
    }

    @Test
    public void testReadFullyWithLessBytesThanAvailable() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        byte[] buffer = new byte[3];

        int bytesReadFully = IOUtils.readFully(inputStream, buffer, 0, 3);
        Assert.assertEquals(3, bytesReadFully);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, byteArray);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        IOUtils.closeQuietly(inputStream);
    }

}