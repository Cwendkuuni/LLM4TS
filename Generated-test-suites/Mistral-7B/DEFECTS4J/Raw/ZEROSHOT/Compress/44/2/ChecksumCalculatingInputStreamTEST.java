package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksum;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException {
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int read = checksumCalculatingInputStream.read();
        Assert.assertEquals(1, read);
        Assert.assertEquals(1L, checksumCalculatingInputStream.getValue());

        byte[] buffer = new byte[3];
        int readLength = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, readLength);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, buffer);
        Assert.assertEquals(1L * 3 + 1L, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStreamWithInvalidChecksum() throws IOException, NoSuchAlgorithmException {
        Checksum checksum = new MessageDigestChecksum("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int read = checksumCalculatingInputStream.read();
        Assert.assertEquals(1, read);

        byte[] buffer = new byte[3];
        int readLength = checksumCalculatingInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, readLength);

        checksumCalculatingInputStream.skip(1);
        Assert.assertNotEquals(checksum.getValue(), 1L * 3 + 1L);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithZeroLength() throws IOException {
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        Assert.assertEquals(0L, checksumCalculatingInputStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStreamWithLongRead() throws IOException {
        Checksum checksum = new Adler32Checksum();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024 * 1024];
        outputStream.write(data, 0, data.length);
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
        byteBuffer.put(data);
        byteBuffer.flip();

        int read = 0;
        byte[] readBuffer = new byte[1024];
        while (read > -1) {
            read = checksumCalculatingInputStream.read(readBuffer, 0, readBuffer.length);
            if (read > -1) {
                byteBuffer.position(byteBuffer.position() + read);
            }
        }

        Assert.assertEquals(data.length, checksumCalculatingInputStream.getValue());
        Assert.assertEquals(byteBuffer.getLong(0), checksumCalculatingInputStream.getValue());
    }

    private static class Adler32Checksum implements Checksum {
        private long adler = 1L;
        private long buflen = 0;

        @Override
        public long getValue() {
            // byte order is big-endian as per the Javadoc
            long b1 = (adler & 0xff000000L) >>> 24;
            long b2 = (adler & 0x00ff0000L) >>> 16;
            long b3 = (adler & 0x0000ff00L) >>> 8;
            long b4 = adler & 0x000000ffL;
            return (b1 << 24) | (b2 << 16) | (b3 << 8) | b4;
        }

        @Override
        public void reset() {
            adler = 1L;
            buflen = 0;
        }

        @Override
        public void update(int b) {
            adler = (adler * 1L + b + 1L) % 0xffffffffL;
            buflen++;
        }

        @Override
        public void update(byte[] b) {
            for (int i = 0; i < b.length; i++) {
                update(b[i]);
            }
        }

        @Override
        public void update(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) {
                update(b[i]);
            }
        }
    }

    private static class MessageDigestChecksum implements Checksum {
        private MessageDigest messageDigest;

        public MessageDigestChecksum(String algorithm) throws NoSuchAlgorithmException {
            messageDigest = MessageDigest.getInstance(algorithm);
        }

        @Override
        public long getValue() {
            byte[] digest = messageDigest.digest();
            long value = 0;
            for (byte b : digest) {
                value <<= 8;
                value |= b & 0xFF;
            }
            return value;
        }

        @Override
        public void reset() {
            messageDigest.reset();
        }

        @Override
        public void update(int b) {
            messageDigest.update((byte) b);
        }

        @Override
        public void update(byte[] b) {
            messageDigest.update(b);
        }

        @Override
        public void update(byte[] b, int off, int len) {
            messageDigest.update(b, off, len);
        }
    }
}