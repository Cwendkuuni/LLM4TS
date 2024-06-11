package org.apache.commons.compress.utils.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;
    private InputStream originalInputStream;
    private static final byte[] TEST_DATA = "Test data".getBytes(StandardCharsets.UTF_8);

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        originalInputStream = new ByteArrayInputStream(TEST_DATA);
        inputStream = new ChecksumCalculatingInputStream(ChecksumUtils.createChecksum("MD5"), originalInputStream);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     */
    @Test
    public void testConstructor() {
        // no specific verification needed for this method
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read()}
     */
    @Test
    public void testRead() throws IOException {
        final int readByte = inputStream.read();
        assertEquals(-1, readByte);

        final byte[] testByteArray = new byte[TEST_DATA.length];
        final int testBytesRead = inputStream.read(testByteArray);
        assertEquals(TEST_DATA.length, testBytesRead);

        assertArrayEquals(TEST_DATA, testByteArray);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read(byte[], int, int)}
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] testByteArray = new byte[TEST_DATA.length];
        final int testBytesRead = inputStream.read(testByteArray);
        assertEquals(TEST_DATA.length, testBytesRead);

        assertArrayEquals(TEST_DATA, testByteArray);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#read(byte[], int, int)} with less data available
     */
    @Test
    public void testReadByteArrayWithLessData() throws IOException {
        final byte[] testByteArray = new byte[TEST_DATA.length];
        final int testBytesRead = inputStream.read(testByteArray, 0, TEST_DATA.length - 1);
        assertEquals(TEST_DATA.length - 1, testBytesRead);

        assertArrayEquals(new byte[TEST_DATA.length - 1], testByteArray);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#skip(long)}
     */
    @Test
    public void testSkip() throws IOException {
        assertThrows(IOException.class, () -> inputStream.skip(TEST_DATA.length * 2L));

        final int bytesSkipped = inputStream.read();
        assertEquals(1, bytesSkipped);
    }

    /**
     * Test method for {@link ChecksumCalculatingInputStream#getValue()}
     */
    @Test
    public void testGetValue() {
        final byte[] expectedValue = "5eb6fda3b882d6a2964a3f0e4a9192d1".getBytes(StandardCharsets.HEX);
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(TEST_DATA);

        final byte[] calculatedValue = inputStream.getValue();
        assertArrayEquals(expectedValue, calculatedValue);
    }
}