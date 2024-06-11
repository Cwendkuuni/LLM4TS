package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.io.input.BiggerFiles;
import org.apache.commons.io.input.ByteArrayInputStream;
import org.apache.commons.io.input.ByteArrayOutputStream;
import org.apache.commons.io.input.CountingInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream testStream;
    private InputStream originalStream;
    private byte[] testData;
    private byte[] testDataWithTrailingGarbage;
    private long checksumExpected;

    @Before
    public void setUp() throws IOException, NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        testData = "Test Data".getBytes(StandardCharsets.UTF_8);
        originalStream = new ByteArrayInputStream(testData);
        testStream = new ChecksumCalculatingInputStream(md, originalStream);
        checksumExpected = md.digest(testData).bytes[0] & 0xFF |
                ((md.digest(testData).bytes[1] & 0xFF) << 8) |
                ((md.digest(testData).bytes[2] & 0xFF) << 16) |
                ((md.digest(testData).bytes[3] & 0xFF) << 24);

        testDataWithTrailingGarbage = new byte[]{"Test Data".getBytes(StandardCharsets.UTF_8), (byte) 0xCA, (byte) 0xDE};
        originalStream = new ByteArrayInputStream(testDataWithTrailingGarbage);
    }

    @Test
    public void testReadByte() throws IOException {
        final int readByte = testStream.read();
        Assert.assertThat(readByte, is(match(testData[0])));
        Assert.assertEquals(testData[0], (byte) checksumCalculatingInputStreamRead(testStream));

        final int readGarbage = testStream.read();
        Assert.assertEquals(-1, readGarbage);
    }

    @Test
    public void testReadByteArray() throws IOException {
        final byte[] readByteArray = new byte[testData.length];
        testStream.read(readByteArray);
        Assert.assertArrayEquals(testData, readByteArray);
        Assert.assertEquals(checksumExpected, testStream.getValue());
    }

    @Test
    public void testReadByteArrayWithTrailingGarbage() throws IOException {
        final byte[] readByteArray = new byte[testDataWithTrailingGarbage.length];
        testStream.read(readByteArray);
        Assert.assertArrayEquals(testDataWithTrailingGarbage, readByteArray);
        Assert.assertEquals(checksumExpected, testStream.getValue());
    }

    @Test
    public void testReadCountingInputStream() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CountingInputStream countingInputStream = new CountingInputStream(testStream, out);
        testStream.read(testData, 0, testData.length);
        Assert.assertEquals(testData.length, countingInputStream.getCount());
        Assert.assertArrayEquals(testData, out.toByteArray());
        Assert.assertEquals(checksumExpected, testStream.getValue());
    }

    @Test
    public void testReadBiggerFile() throws IOException {
        final byte[] biggerFile = BiggerFiles.createRandomFile(1024 * 1024);
        final InputStream biggerFileInputStream = new ByteArrayInputStream(biggerFile);
        testStream = new ChecksumCalculatingInputStream(MessageDigest.getInstance("MD5"), biggerFileInputStream);
        final byte[] readByteArray = new byte[1024];
        int totalBytesRead = 0;
        while (totalBytesRead < biggerFile.length) {
            final int bytesRead = testStream.read(readByteArray, 0, readByteArray.length);
            totalBytesRead += bytesRead;
            testStream.read(readByteArray, totalBytesRead, readByteArray.length - totalBytesRead);
        }
        Assert.assertEquals(biggerFile.length, totalBytesRead);
        Assert.assertEquals(checksumExpected, testStream.getValue());
    }

    private static int match(final int expected) {
        return expected & 0xFF;
    }

    private static int checksumCalculatingInputStreamRead(final ChecksumCalculatingInputStream checksumCalculatingInputStream) throws IOException {
        final int readByte = checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.checksum.update((byte) readByte);
        return readByte;
    }
}