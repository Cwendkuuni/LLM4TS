package org.apache.commons.compress.utils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream stream;

    @Before
    public void setUp() {
        byte[] data = "test".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        CRC32 crc32 = new CRC32();
        stream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = stream.read();
        Assert.assertEquals(116, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[4];
        int result = stream.read(buffer);
        Assert.assertEquals(4, result);
        Assert.assertEquals(116, buffer[0]);
        Assert.assertEquals(101, buffer[1]);
        Assert.assertEquals(115, buffer[2]);
        Assert.assertEquals(116, buffer[3]);
    }

    @Test
    public void testReadByteArrayOffset() throws IOException {
        byte[] buffer = new byte[4];
        int result = stream.read(buffer, 1, 2);
        Assert.assertEquals(2, result);
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(116, buffer[1]);
        Assert.assertEquals(101, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
    }

    @Test
    public void testSkip() throws IOException {
        long result = stream.skip(2);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testGetValue() {
        long result = stream.getValue();
        Assert.assertEquals(4108050209L, result);
    }
}