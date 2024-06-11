package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setup() {
        byte[] data = new byte[10];
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = inputStream.read();
        assertEquals(0, result);
        assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer);
        assertEquals(5, result);
        assertEquals(5, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer, 0, 10);
        assertEquals(10, result);
        assertEquals(10, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(5);
        assertEquals(1, result);
        assertEquals(5, inputStream.getValue());
    }

    @Test
    public void testGetValue() {
        long result = inputStream.getValue();
        assertEquals(0, result);
    }
}