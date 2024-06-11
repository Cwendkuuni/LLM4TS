package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        CRC32 checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        int result = stream.read();
        assertEquals(1, result);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testReadStream() throws IOException {
        CRC32 checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[5];
        int result = stream.read(buffer);
        assertEquals(5, result);
        assertEquals(325_352_459, checksum.getValue()); // CRC32 value for {1, 2, 3, 4, 5}
    }

    @Test
    public void testSkip() throws IOException {
        CRC32 checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        long result = stream.skip(2);
        assertEquals(2, result);
        assertEquals(238_887_593, checksum.getValue()); // CRC32 value for {3, 4, 5}
    }

    @Test
    public void testGetValue() throws IOException {
        CRC32 checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        int readByte;
        while ((readByte = stream.read()) != -1) {}

        long result = stream.getValue();
        assertEquals(325_352_459, result); // CRC32 value for {1, 2, 3, 4, 5}
    }

    @Test(expected = IOException.class)
    public void testReadBeyondEOF() throws IOException {
        CRC32 checksum = new CRC32();
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        int readByte;
        while ((readByte = stream.read()) != -1) {}
        // Try reading beyond EOF
        stream.read();
    }
}