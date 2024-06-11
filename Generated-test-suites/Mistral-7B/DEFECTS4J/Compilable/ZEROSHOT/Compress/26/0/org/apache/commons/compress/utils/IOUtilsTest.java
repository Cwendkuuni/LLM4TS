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
        byte[] sourceData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        ByteArrayOutputStream targetOutputStream = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(sourceData), targetOutputStream);
        Assert.assertEquals(sourceData.length, copiedBytes);

        byte[] targetData = targetOutputStream.toByteArray();
        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        byte[] sourceData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        ByteArrayOutputStream targetOutputStream = new ByteArrayOutputStream();

        long copiedBytes = IOUtils.copy(new ByteArrayInputStream(sourceData), targetOutputStream, 256);
        Assert.assertEquals(sourceData.length, copiedBytes);

        byte[] targetData = targetOutputStream.toByteArray();
        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] sourceData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4, (byte) 0x5, (byte) 0x6};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);

        long skippedBytes = IOUtils.skip(inputStream, 7);
        Assert.assertEquals(sourceData.length - 7, skippedBytes);

        int bytesRead = IOUtils.readFully(inputStream, new byte[sourceData.length - 7]);
        Assert.assertEquals(sourceData.length - 7, bytesRead);

        byte[] targetData = new byte[sourceData.length - 7];
        System.arraycopy(sourceData, 0, targetData, 0, sourceData.length - 7);
        Assert.assertArrayEquals(targetData, IOUtils.toByteArray(inputStream));
    }

    @Test
    public void testReadFully() throws IOException {
        byte[] sourceData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);

        byte[] targetData = new byte[sourceData.length];
        int bytesRead = IOUtils.readFully(inputStream, targetData);
        Assert.assertEquals(sourceData.length, bytesRead);

        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testReadFullyWithOffsetAndLength() throws IOException {
        byte[] sourceData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);

        byte[] targetData = new byte[sourceData.length];
        IOUtils.readFully(inputStream, targetData, 0, sourceData.length);

        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testToByteArray() throws IOException {
        byte[] sourceData = new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3, (byte) 0x4};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);

        byte[] targetData = IOUtils.toByteArray(inputStream);
        Assert.assertArrayEquals(sourceData, targetData);
    }

    @Test
    public void testCloseQuietly() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
        IOUtils.closeQuietly(inputStream);
    }
}