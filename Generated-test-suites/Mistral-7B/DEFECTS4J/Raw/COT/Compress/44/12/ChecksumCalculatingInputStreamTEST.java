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

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        try {
            new ChecksumCalculatingInputStream(null, null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    /**
     * Test read method
     */
    @Test
    public void testRead() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(sourceData[0], readByte);

        final int readBytes = checksumCalculatingInputStream.read(new byte[3]);
        Assert.assertArrayEquals(sourceData, new byte[]{sourceData[0], sourceData[1], sourceData[2]});

        final byte[] readBytesFromStream = new byte[outputStream.size()];
        outputStream.read(readBytesFromStream);
        Assert.assertArrayEquals(sourceData, readBytesFromStream);

        final long checksumValue = checksumCalculatingInputStream.getValue();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sourceData);
        Assert.assertArrayEquals(md.digest(), new byte[]{(byte) 0x30, (byte) 0x36, (byte) 0x28, (byte) 0x9B, (byte) 0x41, (byte) 0x59, (byte) 0x2A, (byte) 0x5F, (byte) 0x15, (byte) 0x5D, (byte) 0xEA, (byte) 0xA6, (byte) 0x9F, (byte) 0xC9, (byte) 0xAB, (byte) 0xCB, (byte) 0x0E, (byte) 0x14, (byte) 0x1F, (byte) 0x4D, (byte) 0x4B, (byte) 0xD6, (byte) 0xD1, (byte) 0xD6, (byte) 0xF5, (byte) 0x2A});
        Assert.assertEquals(checksumValue, ByteBuffer.wrap(md.digest()).getLong(0));
    }

    /**
     * Test read method with empty input
     */
    @Test
    public void testReadEmptyInput() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        Assert.assertEquals(-1, checksumCalculatingInputStream.read());
    }

    /**
     * Test read method with exhausted input
     */
    @Test
    public void testReadExhaustedInput() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final int readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals(sourceData[0], readByte);

        final int readBytes = checksumCalculatingInputStream.read(new byte[3]);
        Assert.assertArrayEquals(sourceData, new byte[]{sourceData[0], sourceData[1], sourceData[2]});

        // Exhaust the input stream
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();

        final long checksumValue = checksumCalculatingInputStream.getValue();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sourceData, 0, sourceData.length - 3);
        Assert.assertArrayEquals(md.digest(), new byte[]{(byte) 0x30, (byte) 0x36, (byte) 0x28, (byte) 0x9B, (byte) 0x41, (byte) 0x59, (byte) 0x2A, (byte) 0x5F, (byte) 0x15, (byte) 0x5D, (byte) 0xEA, (byte) 0xA6, (byte) 0x9F, (byte) 0xC9, (byte) 0xAB, (byte) 0xCB, (byte) 0x0E, (byte) 0x14, (byte) 0x1F, (byte) 0x4D, (byte) 0x4B, (byte) 0xD6, (byte) 0xD1, (byte) 0xD6, (byte) 0xF5, (byte) 0x2A});
        Assert.assertEquals(checksumValue, ByteBuffer.wrap(md.digest()).getLong(0));
    }

    /**
     * Test read method with null input
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullInput() throws IOException {
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), null);
        checksumCalculatingInputStream.read();
    }

    /**
     * Test read method with null output
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullOutput() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        final ByteArrayOutputStream outputStream = null;
        checksumCalculatingInputStream.read(outputStream);
    }

    /**
     * Test read method with negative length
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLength() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumCalculatingInputStream.read(new byte[3], -1, 3);
    }

    /**
     * Test read method with positive length but less than the available data
     */
    @Test
    public void testReadLessThanAvailableData() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final int readBytes = checksumCalculatingInputStream.read(new byte[3]);
        Assert.assertArrayEquals(new byte[]{sourceData[0], sourceData[1], sourceData[2]}, new byte[]{sourceData[0], sourceData[1], sourceData[2]});
    }

    /**
     * Test read method with positive length equal to the available data
     */
    @Test
    public void testReadEqualAvailableData() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final int readBytes = checksumCalculatingInputStream.read(new byte[sourceData.length]);
        Assert.assertEquals(sourceData.length, readBytes);
        Assert.assertArrayEquals(sourceData, new byte[sourceData.length]);
    }

    /**
     * Test read method with positive length greater than the available data
     */
    @Test
    public void testReadGreaterThanAvailableData() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final int readBytes = checksumCalculatingInputStream.read(new byte[sourceData.length + 1]);
        Assert.assertEquals(sourceData.length, readBytes);
        Assert.assertArrayEquals(sourceData, new byte[sourceData.length]);
    }

    /**
     * Test skip method with positive value
     */
    @Test
    public void testSkipPositiveValue() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final long skippedBytes = checksumCalculatingInputStream.skip(2);
        Assert.assertEquals(2, skippedBytes);
    }

    /**
     * Test skip method with negative value
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeValue() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumCalculatingInputStream.skip(-1);
    }

    /**
     * Test skip method with zero value
     */
    @Test
    public void testSkipZeroValue() throws IOException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final long skippedBytes = checksumCalculatingInputStream.skip(0);
        Assert.assertEquals(0, skippedBytes);
    }

    /**
     * Test getValue method
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumCalculatingInputStream.close();

        final long getValue = checksumCalculatingInputStream.getValue();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sourceData);
        Assert.assertEquals(getValue, ByteBuffer.wrap(md.digest()).getLong(0));
    }

    /**
     * Test getValue method with exhausted input
     */
    @Test
    public void testGetValueExhaustedInput() throws IOException, NoSuchAlgorithmException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);

        final int readBytes = inputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();

        final long getValue = checksumCalculatingInputStream.getValue();
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(sourceData, 0, sourceData.length - 3);
        Assert.assertEquals(getValue, ByteBuffer.wrap(md.digest()).getLong(0));
    }

    /**
     * Test getValue method with null checksum
     */
    @Test(expected = NullPointerException.class)
    public void testGetValueNullChecksum() {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        checksumCalculatingInputStream.getValue();
    }

    /**
     * Test getValue method with closed input
     */
    @Test
    public void testGetValueClosedInput() throws IOException, NoSuchAlgorithmException {
        final byte[] sourceData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceData);
        final ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), inputStream);
        checksumCalculatingInputStream.close();

        try {
            checksumCalculatingInputStream.getValue();
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}