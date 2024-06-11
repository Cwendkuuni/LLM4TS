package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculation() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        final byte[] expectedChecksum = md.digest(new byte[]{'T', 'e', 's', 't', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm', 'C', 'a', 'l', 'c', 'u', 'l', 'a', 't', 'i', 'n', 'g', 'S', 't', 'r', 'e', 'a', 'm', 'I', 'n', 'p', 'S', 't', 'r', 'e', 'a', 'm'});
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm', 'C', 'a', 'l', 'c', 'u', 'l', 'a', 't', 'i', 'n', 'g', 'S', 't', 'r', 'e', 'a', 'm', 'I', 'n', 'p', 'S', 't', 'r', 'e', 'a', 'm'});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md, inputStream);
        final byte[] readBytes = IOUtils.toByteArray(checksumCalculatingInputStream);
        final byte[] expectedBytes = new byte[]{'T', 'e', 's', 't', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm', 'C', 'a', 'l', 'c', 'u', 'l', 'a', 't', 'i', 'n', 'g', 'S', 't', 'r', 'e', 'a', 'm', 'I', 'n', 'p', 'S', 't', 'r', 'e', 'a', 'm'};
        Assert.assertArrayEquals(expectedBytes, readBytes);
        final long calculatedChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertArrayEquals(expectedChecksum, MessageDigest.getInstance("MD5").digest(expectedBytes));
        Assert.assertEquals(calculatedChecksum, expectedChecksum.longValue());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumCalculatingInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnChecksum() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'T', 'e', 's', 't', 'C', 'h', 'e', 'c', 'k', 's', 'u', 'm', 'C', 'a', 'l', 'c', 'u', 'l', 'a', 't', 'i', 'n', 'g', 'S', 't', 'r', 'e', 'a', 'm', 'I', 'n', 'p', 'S', 't', 'r', 'e', 'a', 'm'});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        checksumCalculatingInputStream.read();
    }
}