package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        final int read = testee.read();
        Assert.assertEquals(0x01, read);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, testee.getChecksum().digest());

        final int read2 = testee.read();
        Assert.assertEquals(0x02, read2);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, testee.getChecksum().digest());

        final int read3 = testee.read();
        Assert.assertEquals(0x03, read3);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, testee.getChecksum().digest());

        final int read4 = testee.read();
        Assert.assertEquals(-1, read4);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, testee.getChecksum().digest());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnRead() throws IOException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        input.close();
        testee.read();
    }

    @Test
    public void testReadByteArray() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        final byte[] buffer = new byte[4];
        testee.read(buffer);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, testee.getChecksum().digest());
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, buffer);

        testee.read(buffer, 1, 3);
        Assert.assertArrayEquals(new byte[]{0x02, 0x03, 0x04}, testee.getChecksum().digest());
        Assert.assertArrayEquals(new byte[]{0x02, 0x03, 0x04}, buffer, 1, 3);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnReadByteArray() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        final byte[] buffer = new byte[5];
        testee.read(buffer, 1, 4);
        input.close();
    }

    @Test
    public void testSkip() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        Assert.assertEquals(1, testee.skip(1));
        Assert.assertEquals(1, testee.skip(1));
        Assert.assertEquals(0, testee.skip(1));
        Assert.assertEquals(0, testee.skip(Long.MAX_VALUE));
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03}, testee.getChecksum().digest());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnSkip() throws IOException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        testee.skip(Long.MAX_VALUE + 1);
    }

    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream input = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
        final ChecksumCalculatingInputStream testee = new ChecksumCalculatingInputStream(checksum, input);

        final byte[] buffer = new byte[4];
        testee.read(buffer);
        testee.read(buffer, 1, 3);
        testee.skip(1);

        Assert.assertEquals(16, testee.getValue().bitLength());
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04}, testee.getChecksum().digest());
    }

}