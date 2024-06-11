package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() throws IOException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', 'I', 'n', 'p', 'u', 't'};

        Checksum checksum = new Adler32(); // Use any checksum implementation
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        int readByte;
        byte[] readBytes = new byte[inputData.length];
        long expectedChecksum = checksum.getValue();

        // Test reading a single byte
        readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals("Expected read byte to be same as input data", inputData[0], readByte);
        checksum.update((byte) readByte);

        // Test reading a byte array
        int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, inputData.length);
        Assert.assertEquals("Expected to read the same number of bytes as input data", inputData.length, bytesRead);
        checksum.update(readBytes, 0, inputData.length);

        // Test getting the calculated checksum
        long actualChecksum = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Expected checksum to match the one calculated manually", expectedChecksum, actualChecksum);

        // Test reading a large byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] largeInputData = new byte[1024 * 1024]; // 1MB
        outputStream.write(largeInputData, 0, largeInputData.length);
        ByteBuffer largeInputBuffer = ByteBuffer.wrap(outputStream.toByteArray());
        checksum.reset();
        long readByteCount = 0;

        while (largeInputBuffer.hasRemaining()) {
            readByte = checksumCalculatingInputStream.read();
            Assert.assertTrue("Expected read to be > 0", readByte >= 0);
            readByteCount++;
            checksum.update((byte) readByte);
        }

        Assert.assertEquals("Expected to read the same number of bytes as input data", largeInputData.length, readByteCount);
        long actualChecksumOnLargeData = checksumCalculatingInputStream.getValue();
        Assert.assertEquals("Expected checksum to match the one calculated manually", checksum.getValue(), actualChecksumOnLargeData);

        // Test reading from a different checksum implementation
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        checksum = new DigestChecksum(messageDigest);
        inputStream = new ByteArrayInputStream(inputData);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        readByte = checksumCalculatingInputStream.read();
        Assert.assertEquals("Expected read byte to be same as input data", inputData[0], readByte);
        checksum.update((byte) readByte);

        bytesRead = checksumCalculatingInputStream.read(readBytes, 0, inputData.length);
        Assert.assertEquals("Expected to read the same number of bytes as input data", inputData.length, bytesRead);
        checksum.update(readBytes, 0, inputData.length);

        expectedChecksum = messageDigest.digest(inputData);
        actualChecksum = checksum.getValue();
        Assert.assertArrayEquals("Expected checksums to match", expectedChecksum, actualChecksum);
    }
}