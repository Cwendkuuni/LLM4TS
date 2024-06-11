package org.apache.commons.compress.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * IOUtilsTest
 */
public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        byte[] sourceData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(sourceData), target);
        byte[] targetData = target.toByteArray();

        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] sourceData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(sourceData), target, 5);
        byte[] targetData = target.toByteArray();

        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] sourceData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayInputStream input = new ByteArrayInputStream(sourceData);

        long skipped = IOUtils.skip(input, 5);
        byte[] remainingData = new byte[sourceData.length - 5];
        System.arraycopy(sourceData, 5, remainingData, 0, remainingData.length);
        ByteArrayInputStream expectedInput = new ByteArrayInputStream(remainingData);

        Assert.assertEquals(5, skipped);
        byte[] targetData = new byte[remainingData.length];
        IOUtils.copy(expectedInput, new ByteArrayOutputStream().toByteArray());
        Assert.assertArrayEquals(targetData, remainingData);
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] sourceData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(sourceData), target);
        byte[] targetData = target.toByteArray();

        byte[] buffer = new byte[targetData.length];
        int read = IOUtils.readFully(new ByteArrayInputStream(targetData), buffer);

        Assert.assertEquals(targetData.length, read);
        Assert.assertArrayEquals(targetData, buffer);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] sourceData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteArrayOutputStream target = new ByteArrayOutputStream();

        IOUtils.copy(new ByteArrayInputStream(sourceData), target);
        byte[] expectedData = sourceData;
        byte[] targetData = target.toByteArray();

        Assert.assertArrayEquals(expectedData, targetData);
    }

    @Test
    public void testCloseQuietly() {
        ByteArrayInputStream input = new ByteArrayInputStream("Hello World!".getBytes());

        IOUtils.closeQuietly(input);
    }
}