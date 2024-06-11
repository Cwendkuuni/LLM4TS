package org.apache.commons.compress.utils.test;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream_construct() {
        // Test construction
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testRead_singleByte() throws IOException {
        // Test reading a single byte
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        int result = inputStreamWithChecksum.read();
        long checksumValue = checksum.getValue();
        Assert.assertEquals(1, result);
        Assert.assertEquals(checksumValue, 1L);
    }

    @Test
    public void testRead_byteArray() throws IOException {
        // Test reading a byte array
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] expected = new byte[]{1, 2, 3, 4};
        byte[] result = new byte[4];
        int length = inputStreamWithChecksum.read(result);
        Assert.assertEquals(length, 4);
        Assert.assertArrayEquals(expected, result);
        long checksumValue = checksum.getValue();
        Assert.assertEquals(checksumValue, 1L + 2L + 3L + 4L);
    }

    @Test
    public void testRead_byteArray_offset() throws IOException {
        // Test reading a byte array with offset
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] expected = new byte[]{2, 3, 4};
        byte[] result = new byte[3];
        int offset = 1;
        int length = inputStreamWithChecksum.read(result, offset);
        Assert.assertEquals(length, 3);
        Assert.assertArrayEquals(expected, result);
        long checksumValue = checksum.getValue();
        Assert.assertEquals(checksumValue, 1L + 2L + 3L + 4L);
    }

    @Test(expected = IOException.class)
    public void testRead_whenStreamExhausted() throws IOException {
        // Test reading when stream is exhausted
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] result = new byte[1];
        inputStreamWithChecksum.read(result);
        inputStreamWithChecksum.read(result);
        inputStreamWithChecksum.read(); // should throw IOException
    }

    @Test
    public void testSkip() throws IOException {
        // Test skip method
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        long skipped = inputStreamWithChecksum.skip(3);
        Assert.assertEquals(skipped, 3);
        Assert.assertEquals(inputStreamWithChecksum.read(), 1);
    }

    @Test(expected = IOException.class)
    public void testSkip_whenStreamExhausted() throws IOException {
        // Test skip method when stream is exhausted
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        inputStreamWithChecksum.skip(4); // should throw IOException
    }

    @Test
    public void testGetValue() throws IOException {
        // Test getting the value of the checksum
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        long expectedChecksum = 1L + 2L + 3L + 4L;
        long checksumValue = inputStreamWithChecksum.getValue();
        Assert.assertEquals(expectedChecksum, checksumValue);
    }

    @Test
    public void testGetValue_whenInputStreamIsEmpty() throws IOException {
        // Test getting the value of the checksum when input stream is empty
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        long checksumValue = inputStreamWithChecksum.getValue();
        Assert.assertEquals(0L, checksumValue);
    }

    @Test
    public void testGetValue_whenInputStreamThrowsIOException() throws IOException, NoSuchAlgorithmException {
        // Test getting the value of the checksum when input stream throws IOException
        Checksum checksum = new MessageDigest("MD5").getInstance();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        inputStreamWithChecksum.read();
        inputStreamWithChecksum.read();
        inputStreamWithChecksum.read();
        try {
            inputStreamWithChecksum.read();
        } catch (IOException e) {
            inputStreamWithChecksum.close();
            inputStream.close();
            // Continue the test
        }
        long checksumValue = inputStreamWithChecksum.getValue();
        Assert.assertNotNull(checksumValue);
    }

    @Test
    public void testGetValue_whenInputStreamHasDifferentData() throws IOException {
        // Test getting the value of the checksum when input stream has different data
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 5});
        long checksumValue = inputStreamWithChecksum.getValue();
        Assert.assertNotEquals(inputStreamWithChecksum.getValue(), checksumValue);
    }

    @Test
    public void testGetValue_whenChecksumIsMD5() throws IOException, NoSuchAlgorithmException {
        // Test getting the value of the checksum when checksum is MD5
        Checksum checksum = new MessageDigest("MD5").getInstance();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] expectedMD5 = new byte[]{0x30, 0x36, 0x2d, 0x9b, 0x64, 0x1d, 0x8c, 0xd7, 0xad, 0x9b, 0xef, 0xcb, 0xc8, 0xfc, 0x1f, 0x83, 0x62, 0x5b, 0x31, 0x61, 0x9d, 0xc2, 0x1e, 0x4f, 0x8a, 0x2b, 0x4e, 0x56, 0x5d};
        byte[] checksumValue = new byte[16];
        inputStreamWithChecksum.read(checksumValue, 0, checksumValue.length);
        Assert.assertArrayEquals(expectedMD5, checksumValue);
    }

    @Test
    public void testGetValue_whenChecksumIsAdler32() throws IOException {
        // Test getting the value of the checksum when checksum is Adler32
        Checksum checksum = new Adler32Checksum();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        long checksumValue = inputStreamWithChecksum.getValue();
        Assert.assertEquals(1L + 2L + 3L + 4L, checksumValue);
    }

    @Test
    public void testGetValue_whenChecksumIsCRC32() throws IOException {
        // Test getting the value of the checksum when checksum is CRC32
        Checksum checksum = new CRC32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3, 4});
        ChecksumCalculatingInputStream inputStreamWithChecksum = new ChecksumCalculatingInputStream(checksum, inputStream);
        long checksumValue = inputStreamWithChecksum.getValue();
        Assert.assertEquals(0x17ED4AA9L, checksumValue);
    }
}