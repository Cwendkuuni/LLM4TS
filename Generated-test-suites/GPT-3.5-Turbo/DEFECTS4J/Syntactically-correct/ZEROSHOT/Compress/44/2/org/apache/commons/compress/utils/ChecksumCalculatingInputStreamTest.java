package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByteChecksumUpdated() throws IOException {
        byte[] data = {1, 2, 3};

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int b = calculatingInputStream.read();

        assertEquals(1, b);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testRead_byteArrayChecksumUpdated() throws IOException {
        byte[] data = {1, 2, 3};

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[2];
        int read = calculatingInputStream.read(buffer);

        assertEquals(2, read);
        assertEquals(0x82F63FDB, checksum.getValue());
    }

    @Test
    public void testRead_byteArrayWithOffsetChecksumUpdated() throws IOException {
        byte[] data = {1, 2, 3};

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];
        int read = calculatingInputStream.read(buffer, 1, 2);

        assertEquals(2, read);
        assertEquals(0x82F63FDB, checksum.getValue());
        assertEquals(0, buffer[0]);
        assertEquals(1, buffer[1]);
        assertEquals(2, buffer[2]);
    }

    @Test
    public void testSkip_oneByteChecksumUpdated() throws IOException {
        byte[] data = {1, 2, 3};

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = calculatingInputStream.skip(1);

        assertEquals(1, skipped);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testSkip_multipleBytesChecksumUpdated() throws IOException {
        byte[] data = {1, 2, 3};

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = calculatingInputStream.skip(2);

        assertEquals(2, skipped);
        assertEquals(0x82F63FDB, checksum.getValue());
    }

    @Test
    public void testGetValue_returnsChecksumValue() throws IOException {
        byte[] data = {1, 2, 3};

        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long value = calculatingInputStream.getValue();

        assertEquals(0, value);

        calculatingInputStream.read();

        value = calculatingInputStream.getValue();

        assertEquals(1, value);
    }
}