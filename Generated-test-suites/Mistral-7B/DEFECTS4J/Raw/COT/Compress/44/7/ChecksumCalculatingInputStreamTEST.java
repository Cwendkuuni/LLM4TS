package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ChecksumCalculatingInputStream
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream inputStreamMock;
    private Checksum checksumMock;
    private ChecksumCalculatingInputStream checksumCalculatingInputStream;

    @Before
    public void setUp() {
        inputStreamMock = mock(InputStream.class);
        checksumMock = mock(Checksum.class);
        checksumCalculatingInputStream = new ChecksumCalculatingInputStream(checksumMock, inputStreamMock);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        byte testByte = 5;
        doReturn(testByte).when(inputStreamMock).read();

        int actual = checksumCalculatingInputStream.read();
        int expected = testByte;

        assertEquals(expected, actual);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        doReturn(testByteArray).when(inputStreamMock).read(testByteArray, 0, testByteArray.length);

        int actual = checksumCalculatingInputStream.read(testByteArray, 0, testByteArray.length);
        int expected = testByteArray.length;

        assertEquals(expected, actual);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        int offset = 1;
        doReturn(testByteArray, offset).when(inputStreamMock)
                .read(testByteArray, offset, testByteArray.length - offset);

        int actual = checksumCalculatingInputStream.read(testByteArray, offset, testByteArray.length - offset);
        int expected = testByteArray.length - offset;

        assertEquals(expected, actual);
    }

    @Test
    public void testReadByteArrayWithNegativeOffset() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        int offset = -1;
        doThrow(IllegalArgumentException.class).when(checksumCalculatingInputStream)
                .read(testByteArray, offset, testByteArray.length);

        assertThrows(IllegalArgumentException.class, () -> checksumCalculatingInputStream.read(testByteArray, offset, testByteArray.length));
    }

    @Test
    public void testReadByteArrayWithNegativeLength() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        int length = -1;
        doThrow(IllegalArgumentException.class).when(checksumCalculatingInputStream)
                .read(testByteArray, 0, length);

        assertThrows(IllegalArgumentException.class, () -> checksumCalculatingInputStream.read(testByteArray, 0, length));
    }

    @Test
    public void testSkip() throws IOException {
        MessageDigest md = mock(MessageDigest.class);
        doReturn(md).when(checksumMock).getMessageDigest();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doReturn(outputStream.toByteArray()).when(md).digest();

        checksumCalculatingInputStream.skip(10);
        byte[] expected = Arrays.copyOfRange(outputStream.toByteArray(), 0, 10);
        byte[] actual = new byte[10];
        checksumCalculatingInputStream.read(actual, 0, actual.length);

        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionInRead() throws IOException {
        doThrow(IOException.class).when(inputStreamMock).read();

        checksumCalculatingInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionInReadByteArray() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        doThrow(IOException.class).when(inputStreamMock).read(testByteArray, 0, testByteArray.length);

        checksumCalculatingInputStream.read(testByteArray, 0, testByteArray.length);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionInReadByteArrayWithOffset() throws IOException {
        byte[] testByteArray = {1, 2, 3};
        int offset = 1;
        doThrow(IOException.class).when(inputStreamMock)
                .read(testByteArray, offset, testByteArray.length - offset);

        checksumCalculatingInputStream.read(testByteArray, offset, testByteArray.length - offset);
    }

    @Test
    public void testGetValue() {
        MessageDigest md = mock(MessageDigest.class);
        doReturn(md).when(checksumMock).getMessageDigest();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        doReturn(outputStream.toByteArray()).when(md).digest();
        checksumCalculatingInputStream.read(new byte[1024], 0, 1024);

        long expected = Arrays.hashCode(outputStream.toByteArray());
        long actual = checksumCalculatingInputStream.getValue();

        assertEquals(expected, actual);
    }
}