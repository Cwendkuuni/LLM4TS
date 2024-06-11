package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    private static final String ALGORITHM = "MD5";
    private static final byte[] TEST_DATA = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final byte[] TEST_DATA_LONG = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private ChecksumCalculatingInputStream input;
    private Checksum checksum;
    private ByteArrayOutputStream output;
    private ByteArrayInputStream testInputStream;
    private MessageDigest messageDigest;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        checksum = new Adler32(); // Replace with your chosen checksum algorithm
        messageDigest = MessageDigest.getInstance(ALGORITHM);
        output = new ByteArrayOutputStream();
        testInputStream = new ByteArrayInputStream(TEST_DATA);
        input = new ChecksumCalculatingInputStream(checksum, testInputStream);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        assertEquals(TEST_DATA[0], input.read());
        checksumVerification();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[TEST_DATA.length];
        int numRead = input.read(buffer);
        assertEquals(TEST_DATA.length, numRead);
        assertArrayEquals(TEST_DATA, buffer);
        checksumVerification();
    }

    @Test
    public void testReadByteArrayLongLength() throws IOException {
        byte[] buffer = new byte[TEST_DATA_LONG.length];
        int numRead = input.read(buffer);
        assertEquals(TEST_DATA.length, numRead);
        assertArrayEquals(TEST_DATA, buffer, 0, TEST_DATA.length);
        checksumVerification();
    }

    @Test
    public void testReadLong() throws IOException {
        long numRead = input.read(ByteBuffer.allocate(TEST_DATA_LONG.length).array());
        assertEquals(TEST_DATA_LONG.length, numRead);
        checksumVerification();
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        InputStream testInput = new IOExceptionInputStream();
        input = new ChecksumCalculatingInputStream(checksum, testInput);
        input.read();
    }

    @Test
    public void testValue() throws IOException {
        int numRead = TEST_DATA.length;
        byte[] buffer = new byte[numRead];
        input.read(buffer);
        checksumVerification();
        assertEquals(checksum.getValue(), messageDigest.digest(TEST_DATA));
    }

    private void checksumVerification() throws IOException {
        long expectedChecksum = checksum.getValue();
        long actualChecksum = input.getValue();
        assertEquals("Unexpected checksum value.", expectedChecksum, actualChecksum);
    }

}

class Adler32 implements Checksum {
    private static final int initConst = 0x6545ea3b;
    private static final int magic = 0x1a2b3c4d;

    private int s1 = initConst;
    private int s2 = 0;
    private int count = 0;

    @Override
    public int update(byte b) {
        s1 = (s1 * 1566083941L + b) & 0xffffffff;
        s2 = (s2 + 1) & 0xffffffff;
        count++;
        return 1;
    }

    @Override
    public int update(byte[] b, int off, int len) {
        long old = s1;
        for (int i = 0; i < len; i++) {
            s1 = (s1 * 1566083941L + b[off++]) & 0xffffffff;
            s2 = (s2 + 1) & 0xffffffff;
        }
        s1 += (((old >>> 32) & 0xffffffff) + ((s2 >>> 32) & 0xffffffff)) & 0xffffffff;
        s2 = ((s2 >>> 32) & 0xffffffff) + count;
        count += len;
        return len;
    }

    @Override
    public int value() {
        int b1 = (int) (s1 & 0xffffffff);
        int b2 = (int) (s2 & 0xffffffff);
        return b1 < 0 ? b1 + 0x100000000 : b1;
        // Swap the most and least significant 32 bits
        return (b2 << 32) | b1;
    }

}

class IOExceptionInputStream extends InputStream {
    @Override
    public int read() throws IOException {
        throw new IOException("Test input stream");
    }

    @Override
    public int read(byte[] b) throws IOException {
        throw new IOException("Test input stream");
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        throw new IOException("Test input stream");
    }

    @Override
    public long skip(long n) throws IOException {
        throw new IOException("Test input stream");
    }
}