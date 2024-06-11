package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {
    
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        CRC32 checksum = new CRC32();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        inputStream = new ChecksumCalculatingInputStream(checksum, bais);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(1, inputStream.read());
        assertEquals(2, inputStream.read());
        assertEquals(3, inputStream.read());
        assertEquals(4, inputStream.read());
        assertEquals(5, inputStream.read());
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(5, inputStream.read(buffer));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        assertEquals(4, buffer[3]);
        assertEquals(5, buffer[4]);
        assertEquals(-1, inputStream.read(buffer));
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(3, inputStream.read(buffer, 1, 3));
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
        assertEquals(3, buffer[3]);
        assertEquals(0, buffer[4]);
        assertEquals(-1, inputStream.read(buffer, 1, 3));
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, inputStream.skip(1));
        assertEquals(2, inputStream.skip(2));
        assertEquals(2, inputStream.skip(3));
    }

    @Test
    public void testGetValue() {
        assertEquals(0, inputStream.getValue());
    }
}