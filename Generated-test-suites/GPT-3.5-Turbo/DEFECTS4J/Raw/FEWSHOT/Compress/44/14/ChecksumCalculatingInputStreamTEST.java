package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        int result = stream.read();
        assertEquals(1, result);
        assertEquals(1, checksum.getValue());

        result = stream.read();
        assertEquals(2, result);
        assertEquals(3, checksum.getValue());

        // test EOF
        result = stream.read();
        assertEquals(-1, result);
        assertEquals(1201042060, checksum.getValue());

        stream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[3];
        int result = stream.read(buffer);
        assertEquals(3, result);
        assertEquals(6, checksum.getValue());

        // test EOF
        result = stream.read(buffer);
        assertEquals(-1, result);
        assertEquals(1201042060, checksum.getValue());

        stream.close();
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[5];
        int result = stream.read(buffer, 1, 3);
        assertEquals(3, result);
        assertEquals(6, checksum.getValue());

        // test EOF
        result = stream.read(buffer, 1, 3);
        assertEquals(-1, result);
        assertEquals(1201042060, checksum.getValue());

        stream.close();
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        long result = stream.skip(3);
        assertEquals(3, result);
        assertEquals(6, checksum.getValue());

        // test EOF
        result = stream.skip(3);
        assertEquals(0, result);
        assertEquals(1201042060, checksum.getValue());

        stream.close();
    }

    @Test
    public void testGetValue() {
        byte[] data = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, in);

        long value = stream.getValue();
        assertEquals(0, value);

        stream.close();
    }
}