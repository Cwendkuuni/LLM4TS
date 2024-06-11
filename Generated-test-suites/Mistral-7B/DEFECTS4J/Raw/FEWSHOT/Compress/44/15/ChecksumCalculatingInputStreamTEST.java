package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStream;
    private Checksum checksum;

    @Before
    public void setUp() throws NoSuchAlgorithmException {
        checksum = mock(Checksum.class);
        inputStream = new ByteArrayInputStream(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04 });
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteRead() throws IOException {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte readByte = (byte) checksumCalculatingInputStream.read();

        assertEquals(readByte, (byte) 0x00);
        verify(checksum).update(readByte);

        readByte = (byte) checksumCalculatingInputStream.read();

        assertEquals(readByte, (byte) 0x01);
        verify(checksum).update(readByte);

        readByte = (byte) checksumCalculatingInputStream.read();

        assertEquals(readByte, (byte) 0x02);
        verify(checksum).update(readByte);

        readByte = (byte) checksumCalculatingInputStream.read();

        assertEquals(readByte, (byte) 0x03);
        verify(checksum).update(readByte);

        readByte = (byte) checksumCalculatingInputStream.read();

        assertEquals(readByte, (byte) 0x04);
        verify(checksum).update(readByte);

        readByte = (byte) checksumCalculatingInputStream.read();

        assertNull(readByte);
        verify(checksum).update((byte) -1);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithByteArrayRead() throws IOException {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] readBytes = new byte[5];

        int length = checksumCalculatingInputStream.read(readBytes);

        assertArrayEquals(readBytes, new byte[] { (byte) 0x00, (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04 });
        verify(checksum).update(readBytes, 0, 5);

        length = checksumCalculatingInputStream.read(readBytes, 2, 2);

        assertArrayEquals(readBytes, new byte[] { (byte) 0x02, (byte) 0x03, (byte) 0x00, (byte) 0x01, (byte) 0x04 });
        verify(checksum).update(readBytes, 2, 2);
    }

    @Test
    public void testChecksumCalculatingInputStreamWithLongSkip() throws IOException {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        long skippedBytes = checksumCalculatingInputStream.skip(5);

        assertEquals(skippedBytes, 5);
        verify(inputStream, mock.isNull()).skip(5);
        verify(checksum).update(new byte[5]);
    }

    @Test
    public void testGetValue() throws IOException {
        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(mock(MessageDigest.class), inputStream);

        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();
        checksumCalculatingInputStream.read();

        long value = checksumCalculatingInputStream.getValue();

        // Mock message digest to return a byte array with a specific hex value
        byte[] expectedValue = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F };
        verify(checksum).getValue();
        verify(checksum).digest();
        MessageDigest messageDigest = (MessageDigest) mock.resolveMock(MessageDigest.class);
        messageDigest.reset();
        messageDigest.update(new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04 });
        messageDigest.update(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        messageDigest.update(new byte[] { 0x02, 0x03, 0x04, 0x05, 0x06 });
        messageDigest.update(new byte[] { 0x03, 0x04, 0x05, 0x06, 0x07 });
        messageDigest.update(new byte[] { 0x04, 0x05, 0x06, 0x07, 0x08 });
        messageDigest.update(new byte[] { 0x05, 0x06, 0x07, 0x08, 0x09 });
        messageDigest.update(new byte[] { 0x06, 0x07, 0x08, 0x09, 0x0A });
        messageDigest.update(new byte[] { 0x07, 0x08, 0x09, 0x0A, 0x0B });
        messageDigest.update(new byte[] { 0x08, 0x09, 0x0A, 0x0B, 0x0C });
        messageDigest.update(new byte[] { 0x09, 0x0A, 0x0B, 0x0C, 0x0D });
        messageDigest.update(new byte[] { 0x0A, 0x0B, 0x0C, 0x0D, 0x0E });
        messageDigest.update(new byte[] { 0x0B, 0x0C, 0x0D, 0x0E, 0x0F });
        byte[] expectedDigest = messageDigest.digest();

        assertArrayEquals(expectedValue, expectedDigest);
        assertEquals(checksumCalculatingInputStream.getValue(), Long.parseLong(new String(new BaseEncodedString(expectedDigest).decode()), 16));
    }
}