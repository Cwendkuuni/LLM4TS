package org.apache.commons.compress.utils;

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
import org.junit.Test;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        assertNotNull(new ChecksumCalculatingInputStream(null, null));
    }

    /**
     * Test read method with single byte
     * @throws IOException
     */
    @Test
    public void testReadSingleByte() throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'a'});
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int readByte = checksumInputStream.read();

        assertEquals('a', readByte);
        assertEquals(0, checksumInputStream.getValue()); // checksum should be empty since read single byte
        assertEquals(1, byteStream.size());
        assertEquals('a', byteStream.toByteArray()[0]);
    }

    /**
     * Test read method with byte array
     * @throws IOException
     */
    @Test
    public void testReadByteArray() throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'a', 'b'});
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int bytesRead = checksumInputStream.read(new byte[2]);

        assertEquals(2, bytesRead);
        assertEquals(0, checksumInputStream.getValue()); // checksum should be empty since read byte array
        assertEquals(3, byteStream.size());
        assertEquals('a', byteStream.toByteArray()[0]);
        assertEquals('b', byteStream.toByteArray()[1]);
    }

    /**
     * Test read method with large byte array
     * @throws IOException
     */
    @Test
    public void testReadLargeByteArray() throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int bytesRead = checksumInputStream.read(new byte[1024]);

        assertEquals(1024, bytesRead);
        assertEquals(1024L, checksumInputStream.getValue()); // checksum should have the value of 1KB read
        assertEquals(2048, byteStream.size()); // 1KB read from inputStream is written to byteStream
    }

    /**
     * Test skip method
     * @throws IOException
     */
    @Test
    public void testSkip() throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final long skippedBytes = checksumInputStream.skip(512L);

        assertEquals(512L, skippedBytes);
        assertEquals(512L, checksumInputStream.getValue()); // checksum should have the value of 512B skipped
        assertEquals(1536, byteStream.size()); // 1KB read from inputStream is written to byteStream
    }

    /**
     * Test getValue method
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @Test
    public void testGetValue() throws IOException, NoSuchAlgorithmException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int bytesRead = checksumInputStream.read(new byte[1024]);
        checksumInputStream.getValue();

        assertEquals(1024L, checksumInputStream.getValue()); // checksum should have the value of 1KB read
        assertEquals(2048, byteStream.size()); // 1KB read from inputStream is written to byteStream
    }

    /**
     * Test IOException when the underlying stream is exhausted
     * @throws IOException
     */
    @Test
    public void testIOExceptionWhenStreamIsExhausted() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(null, inputStream);
        assertThrows(IOException.class, () -> checksumInputStream.read());
    }

    /**
     * Test IOException when the Checksum doesn't match the expected value
     * @throws IOException
     */
    @Test
    public void testIOExceptionWhenChecksumDoesntMatchExpectedValue() throws IOException {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Checksum checksum = MessageDigest.getInstance("MD5");
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{'a'});
        final ChecksumCalculatingInputStream checksumInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        final int bytesRead = checksumInputStream.read();
        checksum.update(new byte[]{'b'});
        assertThrows(IOException.class, () -> checksumInputStream.getValue());
    }

    /**
     * Test null pointer exception
     */
    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        new ChecksumCalculatingInputStream(null, null);
    }

}