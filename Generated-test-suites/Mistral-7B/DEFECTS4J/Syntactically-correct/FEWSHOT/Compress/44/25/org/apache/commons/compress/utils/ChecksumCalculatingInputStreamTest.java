package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test cases for ChecksumCalculatingInputStream class.
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        Checksum adler32 = new Adler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(adler32, inputStream);
        assertEquals("Expected checksum instance: " + adler32.getClass().getName(), adler32.getClass(), checksumCalculatingInputStream.checksum.getClass());
        assertEquals("Checksum instances should be the same: ", adler32, checksumCalculatingInputStream.checksum);
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Checksum md5 = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        byte[] bytes = new byte[3];

        int read = checksumCalculatingInputStream.read(bytes, 0, 3);
        assertEquals("Expected number of bytes read: ", 3, read);
        assertArrayEquals("Expected bytes to be read: ", new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3}, bytes);
        assertTrue("Checksum should be updated with the read data: ", md5.isComplete());

        byte[] expectedMD5Checksum = md5.digest();
        byte[] inputMD5Checksum = outputStream.toByteArray();
        assertArrayEquals("Expected MD5 checksum to match: ", expectedMD5Checksum, inputMD5Checksum);
    }

    @Test
    public void testReadFailedWithIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        assertNull("Expected checksum to be null: ", checksumCalculatingInputStream.checksum);

        try {
            checksumCalculatingInputStream.read();
            fail("Expected IOException to be thrown: ");
        } catch (IOException e) {
            // Expected
        }

        assertTrue("Expected checksum to be updated with the exception: ", checksumCalculatingInputStream.checksum.isError());
    }

    @Test
    public void testReadWithNullInputByteArray() {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = getChecksumCalculatingInputStreamWithMD5Checksum();
        assertNull("Expected bytes array to be null: ", checksumCalculatingInputStream.read(null, 0, 0));
    }

    @Test
    public void testReadWithOffsetAndLengthGreaterThanInputByteArraySize() {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = getChecksumCalculatingInputStreamWithMD5Checksum();
        byte[] inputByteArray = new byte[]{0x1};
        int offset = 0;
        int length = inputByteArray.length + 1;

        assertSame("Expected input stream to be the same: ", checksumCalculatingInputStream.in, inputByteArray);
        assertArrayEquals("Expected bytes read to be empty: ", new byte[0], checksumCalculatingInputStream.read(new byte[length], offset, length));
        assertTrue("Expected checksum to be updated with the read data: ", checksumCalculatingInputStream.checksum.isComplete());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Checksum md5 = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        long skipped = checksumCalculatingInputStream.skip(2);
        assertEquals("Expected number of bytes skipped: ", 2, skipped);
        assertTrue("Checksum should be updated with the skipped data: ", md5.isComplete());

        byte[] expectedMD5Checksum = md5.digest();
        byte[] inputMD5Checksum = outputStream.toByteArray();
        assertArrayEquals("Expected MD5 checksum to match: ", expectedMD5Checksum, inputMD5Checksum);
    }

    @Test
    public void testSkipWithNegativeValue() throws IOException {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = getChecksumCalculatingInputStreamWithMD5Checksum();
        try {
            checksumCalculatingInputStream.skip(-1);
            fail("Expected IOException to be thrown: ");
        } catch (IOException e) {
            // Expected
        }

        assertTrue("Expected checksum to be updated with the exception: ", checksumCalculatingInputStream.checksum.isError());
    }

    @Test
    public void testGetValue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{(byte) 0x1, (byte) 0x2, (byte) 0x3});
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Checksum md5 = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(md5, inputStream);

        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();

        byte[] expectedMD5Checksum = md5.digest();
        long checksumValue = checksumCalculatingInputStream.getValue();

        assertArrayEquals("Expected MD5 checksum to match: ", expectedMD5Checksum, getMD5FromByteArray((byte[]) checksumValue));
    }

    @Test
    public void testGetValueWithErrorChecksum() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        checksumCalculatingInputStream.read();

        try {
            checksumCalculatingInputStream.getValue();
            fail("Expected IOException to be thrown: ");
        } catch (IOException e) {
            // Expected
        }

        assertTrue("Expected checksum to be updated with the exception: ", checksumCalculatingInputStream.checksum.isError());
    }

    private ChecksumCalculatingInputStream getChecksumCalculatingInputStreamWithMD5Checksum() throws NoSuchAlgorithmException {
        Checksum md5 = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x1});

        return new ChecksumCalculatingInputStream(md5, inputStream);
    }

    private byte[] getMD5FromByteArray(byte[] byteArray) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] result = new byte[16];
        md.update(byteArray, 0, byteArray.length);

        md.digest(result, 0, result.length);

        return result;
    }

}