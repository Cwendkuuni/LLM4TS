package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    private static final byte[] TEST_DATA = new byte[]{'T', 'e', 's', 't', 'D', 'a', 't', 'a'};
    private static final long TEST_CHECKSUM = 5467621685L;

    private ChecksumCalculatingInputStream checksumStream;
    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        inputStream = new ByteArrayInputStream(TEST_DATA);
        checksumStream = new ChecksumCalculatingInputStream(messageDigest, inputStream);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int readByte = checksumStream.read();
        Assert.assertEquals(TEST_DATA[0], readByte);
        Assert.assertEquals(TEST_CHECKSUM, checksumStream.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[TEST_DATA.length];
        int readLength = checksumStream.read(buffer);
        Assert.assertEquals(TEST_DATA.length, readLength);
        Assert.assertArrayEquals(TEST_DATA, buffer);
        Assert.assertEquals(TEST_CHECKSUM, checksumStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] buffer = new byte[TEST_DATA.length + 1];
        int offset = 1;
        int readLength = checksumStream.read(buffer, offset, TEST_DATA.length);
        Assert.assertEquals(TEST_DATA.length, readLength);
        Assert.assertArrayEquals(Arrays.copyOfRange(TEST_DATA, 1, TEST_DATA.length), Arrays.copyOfRange(buffer, offset, offset + TEST_DATA.length - 1));
        Assert.assertEquals(TEST_CHECKSUM, checksumStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        checksumStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumStream.read();
    }

    @After
    public void cleanUp() throws IOException {
        checksumStream.close();
        outputStream.close();
        inputStream.close();
    }
}