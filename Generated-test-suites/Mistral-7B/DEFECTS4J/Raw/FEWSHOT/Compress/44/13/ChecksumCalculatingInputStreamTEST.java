package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        Checksum checksum = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        assertNull(testStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStream_read() throws IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Checksum checksum = new Checksum() {
            @Override
            public int update(byte b) {
                md.update(new byte[]{b});
                return 1;
            }

            @Override
            public int update(byte[] b, int offset, int len) {
                md.update(b, offset, len);
                return len;
            }

            @Override
            public long value() {
                byte[] digest = md.digest();
                return getLongValue(digest);
            }

            private long getLongValue(byte[] bytes) {
                long value = 0;
                for (byte b : bytes) {
                    value = (value << 8) | (b & 0xFF);
                }
                return value;
            }
        };
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] expectedOutput = new byte[]{1, 2, 3};
        byte[] output = new byte[3];
        int read = testStream.read(output, 0, 3);
        assertEquals(3, read);
        assertArrayEquals(expectedOutput, output);
        long checksumValue = testStream.getValue();
        assertEquals(-1425926862367263232L, checksumValue);
    }

    @Test
    public void testChecksumCalculatingInputStream_readArray() throws IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Checksum checksum = new Checksum() {
            @Override
            public int update(byte b) {
                md.update(new byte[]{b});
                return 1;
            }

            @Override
            public int update(byte[] b, int offset, int len) {
                md.update(b, offset, len);
                return len;
            }

            @Override
            public long value() {
                byte[] digest = md.digest();
                return getLongValue(digest);
            }

            private long getLongValue(byte[] bytes) {
                long value = 0;
                for (byte b : bytes) {
                    value = (value << 8) | (b & 0xFF);
                }
                return value;
            }
        };
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] expectedOutput = new byte[]{1, 2, 3};
        byte[] output = new byte[3];
        int read = testStream.read(output, 0, 3);
        assertEquals(3, read);
        assertArrayEquals(expectedOutput, output);
        long checksumValue = testStream.getValue();
        assertEquals(-1425926862367263232L, checksumValue);
    }

    @Test
    public void testChecksumCalculatingInputStream_readArrayWithOffset() throws IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Checksum checksum = new Checksum() {
            @Override
            public int update(byte b) {
                md.update(new byte[]{b});
                return 1;
            }

            @Override
            public int update(byte[] b, int offset, int len) {
                md.update(b, offset, len);
                return len;
            }

            @Override
            public long value() {
                byte[] digest = md.digest();
                return getLongValue(digest);
            }

            private long getLongValue(byte[] bytes) {
                long value = 0;
                for (byte b : bytes) {
                    value = (value << 8) | (b & 0xFF);
                }
                return value;
            }
        };
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] expectedOutput = new byte[]{1, 2, 3};
        byte[] output = new byte[3];
        int read = testStream.read(output, 1, 2);
        assertEquals(2, read);
        assertArrayEquals(new byte[]{2, 3}, output);
        long checksumValue = testStream.getValue();
        assertEquals(-1425926862367263232L, checksumValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChecksumCalculatingInputStream_skip() throws IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        Checksum checksum = new Checksum() {
            @Override
            public int update(byte b) {
                md.update(new byte[]{b});
                return 1;
            }

            @Override
            public int update(byte[] b, int offset, int len) {
                md.update(b, offset, len);
                return len;
            }

            @Override
            public long value() {
                byte[] digest = md.digest();
                return getLongValue(digest);
            }

            private long getLongValue(byte[] bytes) {
                long value = 0;
                for (byte b : bytes) {
                    value = (value << 8) | (b & 0xFF);
                }
                return value;
            }
        };
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        testStream.skip(1);
        assertNotNull(testStream.getValue());
    }
}