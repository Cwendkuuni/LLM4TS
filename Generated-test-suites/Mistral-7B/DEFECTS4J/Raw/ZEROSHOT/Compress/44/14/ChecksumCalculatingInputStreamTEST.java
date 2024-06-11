package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumGenerator;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testCalculateChecksumWithByteArray() throws IOException, NoSuchAlgorithmException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', 'I', 'n', 'p', 'u', 't'};
        byte[] expectedChecksum = new byte[]{(byte) 0x89, (byte) 0x2A};

        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(
                new MessageDigest("MD5"), inputStream);

        byte[] resultChecksum = new byte[16];
        IOUtils.closeQuietly(checksumCalculatingInputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(checksumCalculatingInputStream, outputStream);

        InputStream resultInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ChecksumCalculatingInputStream resultChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(
                new MessageDigest("MD5"), resultInputStream);

        byte[] result = new byte[2];
        resultChecksumCalculatingInputStream.read(result, 0, result.length);
        assertArrayEquals(expectedChecksum, result);
    }

    @Test(expected = IOException.class)
    public void testExceptionWhenReadPastEndOfStream() throws IOException {
        byte[] inputData = new byte[]{'T', 'e', 's', 't', 'I', 'n', 'p', 'u', 't'};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(
                new MessageDigest("MD5"), inputStream);
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        // should throw an IOException
        checksumCalculatingInputStream.read();
    }

    @Test
    public void testCalculateChecksumWithInputFile() throws IOException, NoSuchAlgorithmException {
        String inputFile = "input.txt";
        String expectedChecksumHex = "0d8a5728181d3f840e7428a7";

        InputStream inputStream = IOUtils.toInputStream(getClass().getResource(inputFile));
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(
                new MessageDigest("MD5"), inputStream);

        byte[] resultChecksum = new byte[16];
        IOUtils.closeQuietly(checksumCalculatingInputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(checksumCalculatingInputStream, outputStream);

        InputStream resultInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ChecksumCalculatingInputStream resultChecksumCalculatingInputStream = new ChecksumCalculatingInputStream(
                new MessageDigest("MD5"), resultInputStream);

        byte[] result = new byte[32];
        resultChecksumCalculatingInputStream.read(result, 0, result.length);

        String actualChecksumHex = byteArrayToHexString(result);
        assertEquals(expectedChecksumHex, actualChecksumHex);
    }

    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) {
            result.append(Integer.toString((0xFF & byt) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}