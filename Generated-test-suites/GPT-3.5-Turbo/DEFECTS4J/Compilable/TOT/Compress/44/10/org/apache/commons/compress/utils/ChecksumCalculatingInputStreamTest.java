package org.apache.commons.compress.utils;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream sut;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        CRC32 checksum = new CRC32();
        sut = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = sut.read();
        assertEquals(1, result);
        assertEquals(1, sut.getValue());

        result = sut.read();
        assertEquals(2, result);
        assertEquals(3308255850L, sut.getValue());

        // Test end of stream
        result = sut.read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int result = sut.read(buffer);
        assertEquals(3, result);
        assertEquals(4125284405L, sut.getValue());
    }

    @Test
    public void testReadWithByteArrayOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        int result = sut.read(buffer, 1, 3);
        assertEquals(3, result);
        assertEquals(255L, sut.getValue());

        // Test invalid offset and length
        result = sut.read(buffer, -1, 6);
        assertEquals(-1, result);
    }

    @Test
    public void testSkip() throws IOException {
        long result = sut.skip(2);
        assertEquals(2, result);
        assertEquals(3080717364L, sut.getValue());

        // Test skip beyond available bytes
        result = sut.skip(10);
        assertEquals(0, result);

        sut.read();
        sut.read();
        sut.read();
        sut.read();
        sut.read();
        assertEquals(3735878894L, sut.getValue());
    }
}