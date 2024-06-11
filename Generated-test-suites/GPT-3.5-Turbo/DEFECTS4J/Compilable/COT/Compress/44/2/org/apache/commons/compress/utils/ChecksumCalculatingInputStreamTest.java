package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() {
        CRC32 crc32 = new CRC32();
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        CheckedInputStream checkedInputStream = new CheckedInputStream(byteArrayInputStream, crc32);
        inputStream = new ChecksumCalculatingInputStream(crc32, checkedInputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(4, inputStream.read());
        assertEquals(5, inputStream.read());
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        assertEquals(3, inputStream.read(buffer));
        assertEquals(4, inputStream.read(buffer));
        assertEquals(-1, inputStream.read(buffer));
        assertEquals(32, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(3, inputStream.read(buffer, 1, 3));
        assertEquals(4, inputStream.read(buffer, 1, 3));
        assertEquals(-1, inputStream.read(buffer, 1, 3));
        assertEquals(32, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, inputStream.skip(1));
        assertEquals(1, inputStream.skip(1));
        assertEquals(1, inputStream.skip(1));
        assertEquals(1, inputStream.skip(1));
        assertEquals(1, inputStream.skip(1));
        assertEquals(0, inputStream.skip(1));
        assertEquals(32, inputStream.getValue());
    }
}