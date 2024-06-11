package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.util.ChecksumCalculatingInputStream;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream csis;
    private Checksum checksum;

    @Before
    public void setUp() {
        checksum = new CRC32();
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        csis = new ChecksumCalculatingInputStream(checksum, input);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(1, csis.read());
        assertEquals(2, csis.read());
        assertEquals(3, csis.read());
        assertEquals(4, csis.read());
        assertEquals(5, csis.read());
        assertEquals(-1, csis.read());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        byte[] buffer = new byte[2];
        assertEquals(2, csis.read(buffer));
        assertEquals(3, csis.read(buffer));
        assertEquals(0, csis.read(buffer));
        assertEquals(-1, csis.read(buffer));
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[5];
        assertEquals(3, csis.read(buffer, 1, 3));
        assertEquals(0, csis.read(buffer, 2, 5));
        assertEquals(-1, csis.read(buffer, 0, 0));
    }

    @Test
    public void testSkip() throws IOException {
        assertEquals(1, csis.skip(1));
        assertEquals(0, csis.skip(0));
        assertEquals(-1, csis.skip(5));
    }

    @Test
    public void testGetValue() {
        assertEquals(0, csis.getValue());
        csis.read();
        assertEquals(1, csis.getValue());
    }

    @Test
    public void testCompleteStream() throws IOException {
        byte[] buffer = new byte[5];
        csis.read(buffer);
        assertEquals(0x8b9967f0, csis.getValue());
    }
}