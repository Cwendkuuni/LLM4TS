package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksumifier;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Checksum checksum = new Adler32(); // Use a checksum algorithm
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int byteRead = checksumCalculatingInputStream.read();
        assertEquals(-1, byteRead);

        byte[] buffer = new byte[10];
        int bytesRead = checksumCalculatingInputStream.read(buffer);
        assertEquals(10, bytesRead);

        checksumCalculatingInputStream.close();
        assertEquals(checksum.getValue(), outputStream.getChecksum());
    }

    @Test
    public void testNegativeRead() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);

        assertThrows(IOException.class, () -> checksumCalculatingInputStream.read());
    }

    @Test
    public void testNegativeReadWithChecksumMismatch() throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        MessageDigest md = MessageDigest.getInstance("MD5"); // Use a different checksum algorithm
        Checksum checksum = new MessageDigestChecksum(md);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int byteRead = checksumCalculatingInputStream.read();
        assertEquals(-1, byteRead);

        byte[] buffer = new byte[10];
        int bytesRead = checksumCalculatingInputStream.read(buffer);
        assertEquals(10, bytesRead);

        checksumCalculatingInputStream.close();

        // Change the checksum of the output stream
        ByteBuffer byteBuffer = ByteBuffer.wrap(outputStream.toByteArray());
        byteBuffer.position(0);
        byteBuffer.put(new byte[]{0x01});
        outputStream.position(0);
        outputStream.write(byteBuffer.array(), 0, byteBuffer.array().length);

        try {
            checksumCalculatingInputStream.getValue();
            assert false;
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);

        long skipped = checksumCalculatingInputStream.skip(Long.MAX_VALUE);
        assertEquals(0, skipped);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSkipUnsupportedOperation() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);

        checksumCalculatingInputStream.skip(1);
    }
}