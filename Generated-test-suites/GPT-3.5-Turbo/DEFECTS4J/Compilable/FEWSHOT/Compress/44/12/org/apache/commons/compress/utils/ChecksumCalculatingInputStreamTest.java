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
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int bytesRead = inputStream.read();

        assertEquals(1, bytesRead);
        assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);

        assertEquals(3, bytesRead);
        assertEquals(1 + 2 + 3, inputStream.getValue());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);

        assertEquals(3, bytesRead);
        assertEquals(0 + 1 + 2, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long bytesSkipped = inputStream.skip(2);

        assertEquals(2, bytesSkipped);
        assertEquals(1 + 2, inputStream.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        inputStream.read();
        inputStream.read();
        inputStream.read();

        assertEquals(1 + 2 + 3, inputStream.getValue());
    }
}