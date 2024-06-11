package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.ChecksumUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.zip.Adler32;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private ChecksumCalculatingInputStream checksumStream;
    private byte[] inputData;
    private long checksumExpected;

    @Before
    public void setUp() throws Exception {
        inputData = new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05};
        inputStream = new ByteArrayInputStream(inputData);
        checksumStream = new ChecksumCalculatingInputStream(new Adler32(), inputStream);
        checksumExpected = ChecksumUtils.adler32(inputData);
    }

    @After
    public void tearDown() throws Exception {
        if (checksumStream != null) {
            checksumStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testReadByte() throws IOException {
        int readByte = checksumStream.read();
        assertEquals(inputData[0], (byte) readByte);
        assertEquals(checksumExpected, checksumStream.getValue());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] readBytes = new byte[inputData.length];
        int length = checksumStream.read(readBytes);
        assertArrayEquals(inputData, readBytes);
        assertEquals(checksumExpected, checksumStream.getValue());
    }

    @Test
    public void testReadBytesPartial() throws IOException {
        byte[] readBytes = new byte[3];
        int length = checksumStream.read(readBytes);
        assertArrayEquals(new byte[]{inputData[0], inputData[1], inputData[2]}, readBytes);
        assertEquals(checksumExpected, checksumStream.getValue());
    }

    @Test
    public void testReadBytesEmpty() throws IOException {
        byte[] readBytes = new byte[0];
        int length = checksumStream.read(readBytes);
        assertEquals(0, length);
        assertEquals(checksumExpected, checksumStream.getValue());
    }

    @Test(expected = IOException.class)
    public void testReadBytesNegativeLength() throws IOException {
        byte[] readBytes = new byte[inputData.length + 1];
        int length = checksumStream.read(readBytes);
    }

    @Test
    public void testSkip() throws IOException {
        int skipped = 0;
        while (checksumStream.read() >= 0) {
            skipped++;
        }
        assertEquals(inputData.length, skipped);
        assertEquals(checksumExpected, checksumStream.getValue());
    }

    @Test
    public void testSkipStreamExhausted() throws IOException {
        byte[] expectedChecksum = new byte[4];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.wrap(expectedChecksum);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] inputDataMD5 = new byte[inputData.length];
        messageDigest.update(inputData);
        messageDigest.getBytes(byteBuffer);

        InputStream dataInputStream = new ByteArrayInputStream(inputData);
        InputStream checksumInputStream = new ChecksumCalculatingInputStream(messageDigest, dataInputStream);
        int skipped = 0;
        while (checksumInputStream.read() >= 0) {
            skipped++;
        }
        assertEquals(inputData.length, skipped);
        assertArrayEquals(expectedChecksum, messageDigest.digest());

        checksumStream = new ChecksumCalculatingInputStream(messageDigest, inputStream);
        skipped = 0;
        while (checksumStream.read() >= 0) {
            skipped++;
        }
        assertEquals(0, skipped);
        assertArrayEquals(expectedChecksum, messageDigest.digest());
    }
}