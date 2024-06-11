package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        CRC32 crc32 = new CRC32();
        inputStream = new ChecksumCalculatingInputStream(crc32, byteArrayInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int bytesRead = inputStream.read();
        assertEquals(1, bytesRead);
        long checksum = inputStream.getValue();
        assertEquals(0x32ad3e6, checksum);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = inputStream.read(buffer);
        assertEquals(3, bytesRead);
        long checksum = inputStream.getValue();
        assertEquals(0x32ad3e6, checksum);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[5];
        int bytesRead = inputStream.read(buffer, 1, 3);
        assertEquals(3, bytesRead);
        long checksum = inputStream.getValue();
        assertEquals(0x32ad3e6, checksum);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = inputStream.skip(3);
        assertEquals(3, bytesSkipped);
        long checksum = inputStream.getValue();
        assertEquals(0x32ad3e6, checksum);
    }

    @Test
    public void testGetValue() {
        long checksum = inputStream.getValue();
        assertEquals(0, checksum);
    }

}