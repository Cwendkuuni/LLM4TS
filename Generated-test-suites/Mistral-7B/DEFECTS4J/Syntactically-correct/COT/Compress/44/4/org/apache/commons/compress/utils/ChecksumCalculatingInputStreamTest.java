package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStreamConstructor() {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        Assert.assertNotNull(checksumCalculatingInputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumCalculatingInputStream.read();
        Assert.assertEquals(1, read);
        Assert.assertEquals("1".getBytes(), checksum.digest());
    }

    @Test
    public void testReadByteArray() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] bytes = new byte[3];
        int read = checksumCalculatingInputStream.read(bytes);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, bytes);
        Assert.assertEquals("1,2,3".getBytes(), checksum.digest());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] bytes = new byte[3];
        int offset = 1;
        int length = 2;
        int read = checksumCalculatingInputStream.read(bytes, offset, length);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{2, 3}, bytes);
        Assert.assertEquals("1,2,3,4,5".getBytes(), checksum.digest());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] bytes = new byte[10];
        checksumCalculatingInputStream.read(bytes);
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        long skipped = checksumCalculatingInputStream.skip(3);
        Assert.assertEquals(3, skipped);
        byte[] bytes = new byte[2];
        int read = checksumCalculatingInputStream.read(bytes);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{4, 5}, bytes);
        Assert.assertEquals("1,2,3,4,5".getBytes(), checksum.digest());
    }

    @Test
    public void testGetValue() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumCalculatingInputStream.read();
        long value = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("1,2,3,4,5".getBytes(), checksum.digest());
        Assert.assertEquals(value, checksum.getValue());
    }

    @Test
    public void testGetValueWithWrongChecksum()
            throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Checksum checksum = new MockChecksum(md.digest());
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream checksumCalculatingInputStream =
                new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumCalculatingInputStream.read();
        long value = checksumCalculatingInputStream.getValue();
        Assert.assertNotEquals(md.digest(), checksum.digest());
        Assert.assertEquals(value, checksum.getValue());
    }

    @Test
    public void testGetValueWithEmptyStream() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream =
                new ChecksumCalculatingInputStream(checksum, inputStream);
        long value = checksumCalculatingInputStream.getValue();
        Assert.assertEquals(value, checksum.getValue());
        byte[] bytes = new byte[1];
        int read = checksumCalculatingInputStream.read(bytes);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testGetValueWithNullChecksum() {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream checksumCalculatingInputStream =
                new ChecksumCalculatingInputStream(null, inputStream);
        long value = checksumCalculatingInputStream.getValue();
        Assert.assertNull(value);
    }

    @Test
    public void testGetValueWithNullInputStream() {
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream checksumCalculatingInputStream =
                new ChecksumCalculatingInputStream(checksum, null);
        long value = checksumCalculatingInputStream.getValue();
        Assert.assertNull(value);
    }

    @Test
    public void testGetValueWithIOException() throws IOException {
        Checksum checksum = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream checksumCalculatingInputStream =
                new ChecksumCalculatingInputStream(checksum, inputStream);
        inputStream.close();
        long value = checksumCalculatingInputStream.getValue();
        Assert.assertNull(value);
    }

    static class MockChecksum implements Checksum {
        private byte[] digest;

        public MockChecksum(byte[] digest) {
            this.digest = digest;
        }

        @Override
        public int getBlockSize() {
            return 0;
        }

        @Override
        public void reset() {

        }

        @Override
        public int update(int b) {
            return 0;
        }

        @Override
        public int update(byte[] b) {
            System.arraycopy(b, 0, digest, 0, b.length);
            return b.length;
        }

        @Override
        public int update(byte[] b, int off, int len) {
            System.arraycopy(b, off, digest, 0, len);
            return len;
        }

        @Override
        public void update(ByteBuffer src) {

        }

        @Override
        public byte[] digest() {
            return digest;
        }

        @Override
        public long getValue() {
            return 0;
        }

        @Override
        public String toString() {
            return new String(digest);
        }
    }
}