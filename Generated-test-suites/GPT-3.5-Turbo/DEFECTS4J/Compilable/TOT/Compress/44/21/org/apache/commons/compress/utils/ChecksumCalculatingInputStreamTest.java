package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccris = new ChecksumCalculatingInputStream(crc32, bais);

        // Read one byte and verify the checksum is updated
        int value = ccris.read();
        assertEquals(0x01, value & 0xFF);
        assertEquals(0x01, crc32.getValue());
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccris = new ChecksumCalculatingInputStream(crc32, bais);

        // Read byte array and verify the checksum is updated
        byte[] buffer = new byte[3];
        int bytesRead = ccris.read(buffer);
        assertEquals(3, bytesRead);

        assertEquals(0x01, buffer[0] & 0xFF);
        assertEquals(0x02, buffer[1] & 0xFF);
        assertEquals(0x03, buffer[2] & 0xFF);

        assertEquals(0x06, crc32.getValue());
    }

    @Test
    public void testRead_byteArrayWithOffsetAndLength() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccris = new ChecksumCalculatingInputStream(crc32, bais);

        // Read byte array with offset and length, and verify the checksum is updated
        byte[] buffer = new byte[5];
        int bytesRead = ccris.read(buffer, 1, 3);
        assertEquals(3, bytesRead);

        assertEquals(0, buffer[0] & 0xFF);
        assertEquals(0x01, buffer[1] & 0xFF);
        assertEquals(0x02, buffer[2] & 0xFF);
        assertEquals(0x03, buffer[3] & 0xFF);
        assertEquals(0, buffer[4] & 0xFF);

        assertEquals(0x06, crc32.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccris = new ChecksumCalculatingInputStream(crc32, bais);

        // Skip one byte
        long skipped = ccris.skip(1);
        assertEquals(1, skipped);
        assertEquals(0x01, crc32.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = {0x01, 0x02, 0x03, 0x04, 0x05};
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        CRC32 crc32 = new CRC32();
        ChecksumCalculatingInputStream ccris = new ChecksumCalculatingInputStream(crc32, bais);

        // Read the entire stream and verify the checksum value
        byte[] buffer = new byte[5];
        int bytesRead = ccris.read(buffer);
        assertEquals(5, bytesRead);

        assertEquals(0x01, buffer[0] & 0xFF);
        assertEquals(0x02, buffer[1] & 0xFF);
        assertEquals(0x03, buffer[2] & 0xFF);
        assertEquals(0x04, buffer[3] & 0xFF);
        assertEquals(0x05, buffer[4] & 0xFF);

        assertEquals(0x1506185L, ccris.getValue());
    }
}