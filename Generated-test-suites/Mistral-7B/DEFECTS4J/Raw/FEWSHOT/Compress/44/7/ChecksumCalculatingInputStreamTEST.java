package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Test for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        assertEquals(inputStream, checksumCalculatingInputStream.in);
        assertEquals(md5, checksumCalculatingInputStream.checksum);
    }

    @Test
    public void testReadByte() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        final byte expected = (byte) 1;
        final int readByte = checksumCalculatingInputStream.read();
        assertTrue(readByte >= 0);
        assertEquals(expected, readByte);

        final byte nextExpected = (byte) 2;
        final int readByteNext = checksumCalculatingInputStream.read();
        assertEquals(nextExpected, readByteNext);

        // Test reading past the end of the stream
        final int readByteEndOfStream = checksumCalculatingInputStream.read();
        assertEquals(-1, readByteEndOfStream);
    }

    @Test
    public void testReadByteArray() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        final int readLength = checksumCalculatingInputStream.read(expected, 0, expected.length);
        assertEquals(expected.length, readLength);
        assertArrayEquals(expected, expected);

        // Test reading less than the length of the buffer
        final byte[] buffer = new byte[5];
        final int readLengthLessThanBufferLength = checksumCalculatingInputStream.read(buffer, 0, 3);
        assertEquals(3, readLengthLessThanBufferLength);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);

        // Test reading past the end of the stream
        final int readLengthPastEndOfStream = checksumCalculatingInputStream.read(new byte[1], 0, 1);
        assertEquals(-1, readLengthPastEndOfStream);
    }

    @Test
    public void testReadByteArrayWithChecksum() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        final byte[] buffer = new byte[expected.length];
        final int readLength = checksumCalculatingInputStream.read(buffer);
        assertEquals(expected.length, readLength);
        assertArrayEquals(expected, buffer);
        final byte[] checksumValue = md5.getValue();

        final long expectedChecksum = ChecksumUtils.byteArrayToChecksum(expected);
        assertEquals(expectedChecksum, checksumValue.longValue());
    }

    @Test
    public void testSkip() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        // Test skipping zero bytes
        final long skipped = checksumCalculatingInputStream.skip(0);
        assertEquals(0, skipped);

        // Test skipping all bytes
        final long skippedAllBytes = checksumCalculatingInputStream.skip(Long.MAX_VALUE);
        assertEquals(-1, skippedAllBytes);

        // Test skipping some bytes
        final long skippedSomeBytes = checksumCalculatingInputStream.skip(4);
        assertEquals(4, skippedSomeBytes);

        // Test reading past the end of the stream
        final int readByteEndOfStream = checksumCalculatingInputStream.read();
        assertEquals(-1, readByteEndOfStream);
    }

    @Test
    public void testGetValue() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        checksumCalculatingInputStream.read(expected);
        final long expectedChecksum = ChecksumUtils.byteArrayToChecksum(expected);
        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testGetValueWithDigestChecksum() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);
        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        final byte[] buffer = new byte[expected.length];
        checksumCalculatingInputStream.read(buffer);

        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(buffer);
        final byte[] checksumValue = messageDigest.digest();
        final long expectedChecksum = ChecksumUtils.byteArrayToChecksum(expected);
        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertEquals(expectedChecksum, ChecksumUtils.byteArrayToChecksum(checksumValue));
        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testGetValueWithChecksumChecksum() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);
        final byte[] expected = new byte[]{1, 2, 3, 4, 5};
        final byte[] buffer = new byte[expected.length];
        checksumCalculatingInputStream.read(buffer);

        final long expectedChecksum = checksumCalculatingInputStream.getValue();
        final long actualChecksum = checksumCalculatingInputStream.getValue();

        assertEquals(expectedChecksum, actualChecksum);
    }

    @Test
    public void testClose() throws IOException {
        final Checksum md5 = ChecksumUtils.getChecksumInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.close();

        final InputStream inputStreamClosed = checksumCalculatingInputStream.in;
        assertNull(inputStreamClosed);
    }
}