package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream.
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStreamConstructor() {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        assertEquals(checksum, checksumCalculatingInputStream.getChecksum());
        assertEquals(inputStream, checksumCalculatingInputStream.getInputStream());
    }

    @Test
    public void testReadByte() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        assertEquals(inputStream.read(), checksumCalculatingInputStream.read());
        checksumCalculatingInputStream.read(); // consume the byte, checksum should be updated
        assertEquals(checksum.getValue(), 1L); // checksum should have been updated
    }

    @Test
    public void testReadByteArray_full() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B'});
        byte[] expectedResult = new byte[]{'A', 'B'};
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] result = new byte[2];
        int readBytes = checksumCalculatingInputStream.read(result);
        assertEquals(readBytes, 2);
        assertEquals(expectedResult, result);
        assertEquals(checksum.getValue(), 2L); // checksum should have been updated
    }

    @Test
    public void testReadByteArray_partial() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A', 'B'});
        byte[] expectedResult = new byte[]{'A'};
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        byte[] result = new byte[1];
        int readBytes = checksumCalculatingInputStream.read(result);
        assertEquals(readBytes, 1);
        assertEquals(expectedResult[0], result[0]);
        assertEquals(checksum.getValue(), 1L); // checksum should have been updated
    }

    @Test
    public void testReadByteArray_empty() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        byte[] result = new byte[1];
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        int readBytes = checksumCalculatingInputStream.read(result);
        assertEquals(readBytes, -1);
        assertEquals(checksum.getValue(), 0L); // checksum should not have been updated
    }

    @Test
    public void testReadByteArray_exception() throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(messageDigest, inputStream);
        byte[] result = new byte[1];
        assertThrows(UnsupportedOperationException.class, () -> checksumCalculatingInputStream.read(result));
    }

    @Test
    public void testSkip() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        assertEquals(1, checksumCalculatingInputStream.skip(1));
        assertEquals(checksum.getValue(), 1L); // checksum should have been updated
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkip_exception() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{'A'});
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.skip(Long.MAX_VALUE);
    }

    @Test
    public void testGetValue() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.read(); // read a byte to start the checksum calculation
        long expectedResult = checksum.getValue();
        long result = checksumCalculatingInputStream.getValue();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetValue_eof() throws IOException {
        Checksum checksum = new ChecksumUtils().newAdler32();
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
        checksumCalculatingInputStream.close(); // close the stream to trigger EOF
        long result = checksumCalculatingInputStream.getValue();
        assertEquals(0, result);
    }

    @Test
    public void testGetValue_exception() throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(messageDigest, inputStream);
        assertThrows(UnsupportedOperationException.class, () -> checksumCalculatingInputStream.getValue());
    }
}