package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testChecksumCalculatingInputStream() {
        // Test constructor with valid inputs
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        Checksum checksum = MessageDigest.getInstance("MD5");
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Test method functionality: read()
        int read = checksumCalculatingInputStream.read();
        Assert.assertEquals(1, read);
        Assert.assertNotNull(checksumCalculatingInputStream.checksum);
        Assert.assertEquals(1, checksum.getBlockSize());
        byte[] readBytes = new byte[checksum.getBlockSize()];
        int readBytesLength = checksumCalculatingInputStream.read(readBytes);
        Assert.assertArrayEquals(new byte[]{1}, readBytes);

        // Test method functionality: read(byte[])
        int readBytesLength1 = checksumCalculatingInputStream.read(readBytes, 0, readBytes.length);
        Assert.assertEquals(readBytesLength1, readBytes.length);
        Assert.assertArrayEquals(new byte[]{1, 2, 3}, readBytes);

        // Test method functionality: read(byte[], int, int)
        int readBytesLength2 = checksumCalculatingInputStream.read(readBytes, 1, readBytes.length - 1);
        Assert.assertEquals(readBytesLength2, 2);
        Assert.assertArrayEquals(new byte[]{2, 3}, new byte[]{readBytes[1], readBytes[2]});

        // Test method functionality: skip()
        long skipped = checksumCalculatingInputStream.skip(10);
        Assert.assertEquals(1, skipped);

        // Test method functionality: getValue()
        long value = checksumCalculatingInputStream.getValue();
        byte[] expectedValue = new byte[16];
        byte[] readAllBytes = new byte[3];
        checksumCalculatingInputStream.read(readAllBytes, 0, readAllBytes.length);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(readAllBytes);
        md.digest(expectedValue, 0, expectedValue.length);
        Assert.assertArrayEquals(expectedValue, checksum.getValue());

        // Test exception handling: IOException when underlying stream is exhausted
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamExhausted = new ByteArrayInputStream(outputStream.toByteArray());
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStreamExhausted);
        try {
            checksumCalculatingInputStream.read();
        } catch (IOException e) {
            // expected
        }

        // Test edge case: checksum calculation with large input
        byte[] largeInput = new byte[1024 * 1024];
        ByteArrayInputStream inputStreamLarge = new ByteArrayInputStream(largeInput);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStreamLarge);
        int readAll = 0;
        while (readAll < largeInput.length) {
            int bytesRead = checksumCalculatingInputStream.read(readBytes, 0, readBytes.length);
            readAll += bytesRead;
        }

        // Test edge case: checksum calculation with multiple threads
        Thread thread1 = new Thread(() -> {
            try {
                checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    checksumCalculatingInputStream.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    checksumCalculatingInputStream.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}