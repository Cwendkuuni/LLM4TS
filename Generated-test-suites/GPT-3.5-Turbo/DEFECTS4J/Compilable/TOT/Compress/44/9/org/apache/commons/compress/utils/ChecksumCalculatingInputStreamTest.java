package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    private Checksum checksum;
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setup() {
        checksum = new CRC32();
    }

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
        int byteRead = inputStream.read();
        Assert.assertEquals(0x01, byteRead);
        Assert.assertEquals(0x01, inputStream.getValue());

        int byteRead2 = inputStream.read();
        Assert.assertEquals(0x02, byteRead2);
        Assert.assertEquals(0x352441C2, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
        byte[] buffer = new byte[2];
        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(2, bytesRead);
        Assert.assertEquals(0x03, buffer[0]);
        Assert.assertEquals(0x04, buffer[1]);
        Assert.assertEquals(0x3D4CAA8B, inputStream.getValue());
    }

    @Test
    public void testReadOffsetLength() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer, 2, 2);
        Assert.assertEquals(2, bytesRead);
        Assert.assertEquals(0x00, buffer[0]);
        Assert.assertEquals(0x00, buffer[1]);
        Assert.assertEquals(0x01, buffer[2]);
        Assert.assertEquals(0x02, buffer[3]);
        Assert.assertEquals(0xCD51A3FF, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
        long skipCount = inputStream.skip(2);
        Assert.assertEquals(2, skipCount);
        Assert.assertEquals(0x04, inputStream.read());
        Assert.assertEquals(0xB7EF0021, inputStream.getValue());
    }

    @Test
    public void testErrorScenario() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03, 0x04 };
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));

        // Read beyond the available bytes in the stream
        byte[] buffer = new byte[10];
        int bytesRead = inputStream.read(buffer);
        Assert.assertEquals(4, bytesRead);

        // Verify that the checksum doesn't match
        Assert.assertNotEquals(0x01, inputStream.getValue());

        // Test skipping when there are no more bytes in the stream
        long skipCount = inputStream.skip(10);
        Assert.assertEquals(0, skipCount);
    }
}

