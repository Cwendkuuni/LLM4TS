package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Tests for ChecksumCalculatingInputStream class
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream_singleByte() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        Assert.assertEquals(0x964fe57aL, checksumInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_byteArray() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];
        int len = checksumInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(buffer.length, len);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);
        Assert.assertEquals(0x5b01529cL, checksumInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_byteArrayLongPosition() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = buffer.length;
        int read = checksumInputStream.read(buffer, offset, length);
        Assert.assertEquals(buffer.length, read);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);
        checksumInputStream.skip(1);
        offset += 1;
        length -= 1;
        read = checksumInputStream.read(buffer, offset, length);
        Assert.assertEquals(length, read);
        Assert.assertArrayEquals(new byte[]{0x02, 0x03}, buffer, 0, length);
        checksumInputStream.skip(2);
        Assert.assertEquals(0, checksumInputStream.read());
        Assert.assertEquals(0x242638a4L, checksumInputStream.getValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChecksumCalculatingInputStream_skipAllBytes() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        long skip = 10 * 1024 * 1024;
        checksumInputStream.skip(skip);
    }

    @Test
    public void testChecksumCalculatingInputStream_byteBuffer() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        ByteBuffer buffer = ByteBuffer.allocate(3);
        int read = checksumInputStream.read(buffer);
        buffer.flip();
        Assert.assertEquals(buffer.get(0), (byte) 0x01);
        Assert.assertEquals(buffer.get(1), (byte) 0x02);
        Assert.assertEquals(buffer.get(2), (byte) 0x03);
        buffer.clear();
        Assert.assertEquals(buffer.remaining(), 0);
        Assert.assertEquals(0x5b01529cL, checksumInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_close() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        checksumInputStream.close();
        Assert.assertEquals(0, checksumInputStream.read());
        Assert.assertEquals(0x5b01529cL, checksumInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_multipleInputs() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream1 = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream1);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream2);
        read = checksumInputStream.read();
        Assert.assertEquals(0x04, read);
        Assert.assertEquals(0x29332765L, checksumInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_multipleInputs_close() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream1 = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream1);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        checksumInputStream.close();
        read = checksumInputStream.read();
        Assert.assertEquals(-1, read);
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream2);
        read = checksumInputStream.read();
        Assert.assertEquals(0x04, read);
        Assert.assertEquals(0x29332765L, checksumInputStream.getValue());
        checksumInputStream.close();
    }

    @Test
    public void testChecksumCalculatingInputStream_singleByteWithChecksum() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        long expectedChecksum = 0x964fe57aL;
        long actualChecksum = checksumInputStream.getValue();
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testChecksumCalculatingInputStream_byteArrayWithChecksum() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];
        int len = checksumInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(buffer.length, len);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);
        long expectedChecksum = 0x5b01529cL;
        long actualChecksum = checksumInputStream.getValue();
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testChecksumCalculatingInputStream_byteArrayLongPositionWithChecksum() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] buffer = new byte[3];
        int offset = 0;
        int length = buffer.length;
        int read = checksumInputStream.read(buffer, offset, length);
        Assert.assertEquals(buffer.length, read);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, buffer);
        checksumInputStream.skip(1);
        offset += 1;
        length -= 1;
        read = checksumInputStream.read(buffer, offset, length);
        Assert.assertEquals(length, read);
        Assert.assertArrayEquals(new byte[]{0x02, 0x03}, buffer, 0, length);
        long expectedChecksum = 0x242638a4L;
        long actualChecksum = checksumInputStream.getValue();
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testChecksumCalculatingInputStream_byteBufferWithChecksum() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        ByteBuffer buffer = ByteBuffer.allocate(3);
        int read = checksumInputStream.read(buffer);
        buffer.flip();
        Assert.assertEquals(buffer.get(0), (byte) 0x01);
        Assert.assertEquals(buffer.get(1), (byte) 0x02);
        Assert.assertEquals(buffer.get(2), (byte) 0x03);
        buffer.clear();
        Assert.assertEquals(buffer.remaining(), 0);
        long expectedChecksum = 0x5b01529cL;
        long actualChecksum = checksumInputStream.getValue();
        Assert.assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testChecksumCalculatingInputStream_multipleInputsWithChecksum() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream1 = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream1);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream2);
        read = checksumInputStream.read();
        Assert.assertEquals(0x04, read);
        Assert.assertEquals(0x29332765L, checksumInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_multipleInputsCloseWithChecksum() throws IOException {
        Checksum checksum = new Adler32();
        InputStream inputStream1 = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        InputStream inputStream2 = new ByteArrayInputStream(new byte[]{0x04, 0x05, 0x06});
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream1);
        int read = checksumInputStream.read();
        Assert.assertEquals(0x01, read);
        checksumInputStream.close();
        read = checksumInputStream.read();
        Assert.assertEquals(-1, read);
        checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream2);
        read = checksumInputStream.read();
        Assert.assertEquals(0x04, read);
        Assert.assertEquals(0x29332765L, checksumInputStream.getValue());
        checksumInputStream.close();
    }
}