package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(input));

        int result = inputStream.read();
        assertEquals(1, result);
        assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        byte[] output = new byte[3];
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(input));

        int result = inputStream.read(output);
        assertEquals(3, result);
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        byte[] output = new byte[5];
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(input));

        int result = inputStream.read(output, 1, 3);
        assertEquals(3, result);
        assertEquals(6, inputStream.getValue());
        assertEquals(0, output[0]);
        assertEquals(1, output[1]);
        assertEquals(2, output[2]);
        assertEquals(3, output[3]);
        assertEquals(0, output[4]);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(input));

        long result = inputStream.skip(2);
        assertEquals(2, result);
        assertEquals(0, inputStream.getValue());

        result = inputStream.skip(10);
        assertEquals(0, result);
        assertEquals(0, inputStream.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] input = {1, 2, 3, 4, 5};
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(input));

        inputStream.read();
        assertEquals(1, inputStream.getValue());

        inputStream.read();
        assertEquals(2119576335, inputStream.getValue());
    }
}