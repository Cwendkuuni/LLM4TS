package org.apache.commons.compress.utils;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream cciStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int data = cciStream.read();
        assertEquals(0x01, data);
        assertEquals(1, cciStream.getValue());

        data = cciStream.read();
        assertEquals(0x02, data);
        assertEquals(0x77073096, cciStream.getValue());

        data = cciStream.read();
        assertEquals(0x03, data);
        assertEquals(0x8099c06c, cciStream.getValue());

        data = cciStream.read();
        assertEquals(0x04, data);
        assertEquals(0x40368a64, cciStream.getValue());

        data = cciStream.read();
        assertEquals(-1, data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream cciStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[2];

        int bytesRead = cciStream.read(buffer);
        assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
        assertEquals(0x77073096, cciStream.getValue());
        assertEquals(2, bytesRead);

        bytesRead = cciStream.read(buffer);
        assertArrayEquals(new byte[]{0x03, 0x04}, buffer);
        assertEquals(0x40368a64, cciStream.getValue());
        assertEquals(2, bytesRead);

        bytesRead = cciStream.read(buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream cciStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[4];

        int bytesRead = cciStream.read(buffer, 1, 2);
        assertArrayEquals(new byte[]{0, 0x01, 0x02, 0}, buffer);
        assertEquals(0x77073096, cciStream.getValue());
        assertEquals(2, bytesRead);

        bytesRead = cciStream.read(buffer, 2, 2);
        assertArrayEquals(new byte[]{0, 0x01, 0x03, 0x04}, buffer);
        assertEquals(0x40368a64, cciStream.getValue());
        assertEquals(2, bytesRead);

        bytesRead = cciStream.read(buffer, 3, 1);
        assertArrayEquals(new byte[]{0, 0x01, 0x03, 0x4}, buffer);
        assertEquals(-1, bytesRead);
    }

    @Test
    public void testSkip() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream cciStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skipped = cciStream.skip(1);
        assertEquals(1, skipped);
        assertEquals(0x77073096, cciStream.getValue());

        skipped = cciStream.skip(2);
        assertEquals(2, skipped);
        assertEquals(0x40368a64, cciStream.getValue());

        skipped = cciStream.skip(1);
        assertEquals(0, skipped);
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] testData = {0x01, 0x02, 0x03, 0x04};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream cciStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        assertEquals(0, cciStream.getValue());

        cciStream.read();

        assertEquals(0x77073096, cciStream.getValue());

        byte[] buffer = new byte[2];
        cciStream.read(buffer);

        assertEquals(0x40368a64, cciStream.getValue());
    }
}