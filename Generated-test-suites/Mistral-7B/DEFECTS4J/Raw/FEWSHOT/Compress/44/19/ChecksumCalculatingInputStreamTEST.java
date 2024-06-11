package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

public class ChecksumCalculatingInputStreamTest {

    private static byte[] getExpectedChecksum(byte[] input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input);
        return md.digest();
    }

    @Test
    public void testChecksumCalculatingInputStreamMD5() throws Exception {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum("MD5"), new ByteArrayInputStream(input));
        byte[] output = new byte[input.length];
        int read = checksumStream.read(output);
        assertArrayEquals(input, output);
        assertNotNull(checksumStream.getValue());
        assertEquals(getExpectedChecksum(input), checksumStream.getValue());
    }

    @Test
    public void testChecksumCalculatingInputStreamMD5WithPartialRead() throws Exception {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum("MD5"), new ByteArrayInputStream(input));
        byte[] output = new byte[2];
        int read = checksumStream.read(output);
        assertArrayEquals(new byte[]{1, 2}, output);
        assertNotNull(checksumStream.getValue());
        byte[] expectedChecksum = getExpectedChecksum(input);
        byte[] checksum = new byte[16]; // assuming MD5 checksum is 16 bytes long
        checksumStream.read(checksum, 0, checksum.length);
        assertArrayEquals(expectedChecksum, Arrays.copyOfRange(checksum, 0, expectedChecksum.length));
    }

    @Test(expected = IOException.class)
    public void testChecksumCalculatingInputStreamIOException() throws Exception {
        byte[] input = new byte[]{1, 2, 3, 4, 5};
        ChecksumCalculatingInputStream checksumStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum("MD5"), new ByteArrayInputStream(input));
        checksumStream.skip(Long.MAX_VALUE);
    }
}