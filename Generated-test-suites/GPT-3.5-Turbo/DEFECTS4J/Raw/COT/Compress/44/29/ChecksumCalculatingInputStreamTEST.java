
package org.apache.commons.compress.utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() {
        Checksum checksum = new CRC32();
        InputStream in = new ByteArrayInputStream("test data".getBytes());
        inputStream = new ChecksumCalculatingInputStream(checksum, in);
    }

    @Test
    public void testRead_singleByte() throws IOException {
        Assert.assertEquals('t', inputStream.read());
        Assert.assertEquals('e', inputStream.read());
        Assert.assertEquals('s', inputStream.read());
        Assert.assertEquals('t', inputStream.read());
        Assert.assertEquals(' ', inputStream.read());
        Assert.assertEquals('d', inputStream.read());
        Assert.assertEquals('a', inputStream.read());
        Assert.assertEquals('t', inputStream.read());
        Assert.assertEquals('a', inputStream.read());
        Assert.assertEquals(-1, inputStream.read()); // end of stream
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] buffer = new byte[5];
        Assert.assertEquals(5, inputStream.read(buffer));
        Assert.assertArrayEquals("test ".getBytes(), buffer);
        Assert.assertEquals(3, inputStream.read(buffer));
        Assert.assertArrayEquals("data\0".getBytes(), buffer);
        Assert.assertEquals(-1, inputStream.read(buffer)); // end of stream
    }

    @Test
    public void testRead_byteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[10];
        Assert.assertEquals(5, inputStream.read(buffer, 0, 5));
        Assert.assertArrayEquals("test ".getBytes(), buffer);
        Assert.assertEquals(3, inputStream.read(buffer, 5, 3));
        Assert.assertArrayEquals("data\0\0\0\0\0".getBytes(), buffer);
        Assert.assertEquals(-1, inputStream.read(buffer, 8, 2)); // end of stream
    }

    @Test
    public void testSkip() throws IOException {
        Assert.assertEquals(1, inputStream.skip(1));
        Assert.assertEquals('e', inputStream.read());
        Assert.assertEquals(2, inputStream.skip(2));
        Assert.assertEquals('t', inputStream.read());
        Assert.assertEquals(4, inputStream.skip(4));
        Assert.assertEquals(-1, inputStream.skip(1)); // end of stream
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals(0, inputStream.getValue());
    }
}

