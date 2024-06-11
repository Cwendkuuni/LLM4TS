package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
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
    public void testChecksumCalculatingInputStream() {
        // Test empty stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Checksum checksum = new Adler32(); // or any other Checksum implementation
        ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, emptyInputStream);
        Assert.assertEquals(0, checksumInputStream.read());
        Assert.assertEquals(0, checksumInputStream.getValue());

        // Test single byte stream
        byte[] testByte = {0x01};
        InputStream inputStream = new ByteArrayInputStream(testByte);
        checksum = new Adler32();
        ChecksumCalculatingInputStream checksumInputStream1 = new ChecksumCalculatingInputStream(checksum, inputStream);
        int read = checksumInputStream1.read();
        Assert.assertEquals(testByte[0], read);
        Assert.assertEquals(testByte[0], checksum.getValue());

        // Test byte array stream
        byte[] testBytes = {0x01, 0x02, 0x03};
        InputStream inputStream1 = new ByteArrayInputStream(testBytes);
        checksum = new Adler32();
        ChecksumCalculatingInputStream checksumInputStream2 = new ChecksumCalculatingInputStream(checksum, inputStream1);
        int readLength = checksumInputStream2.read(new byte[3], 0, 3);
        Assert.assertEquals(testBytes.length, readLength);
        Assert.assertArrayEquals(testBytes, new byte[3]);
        Assert.assertEquals(testBytes, checksum.getValue().toString().getBytes());

        // Test skip
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("Adler-32");
        } catch (NoSuchAlgorithmException e) {
            Assert.fail("Adler-32 algorithm not supported");
        }
        InputStream inputStream2 = new ByteArrayInputStream(testBytes);
        checksum = new ChecksumCalculatingInputStream(md, inputStream2);
        long skipped = checksum.skip(10);
        Assert.assertEquals(1, skipped);
        Assert.assertArrayEquals(testBytes, md.digest());

        // Test getValue
        ByteArrayInputStream inputStream3 = new ByteArrayInputStream(testBytes);
        checksum = new Adler32();
        ChecksumCalculatingInputStream checksumInputStream3 = new ChecksumCalculatingInputStream(checksum, inputStream3);
        Assert.assertEquals(testBytes, checksumInputStream3.getValue().toString().getBytes());
    }
}