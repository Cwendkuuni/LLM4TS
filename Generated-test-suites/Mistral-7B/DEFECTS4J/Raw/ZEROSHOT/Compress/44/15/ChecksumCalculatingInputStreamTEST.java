package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;

/**
 * Test class for {@link ChecksumCalculatingInputStream}
 */
public class ChecksumCalculatingInputStreamTest {

    /**
     * Test for {@link ChecksumCalculatingInputStream#ChecksumCalculatingInputStream(Checksum, InputStream)}
     */
    @Test
    public void testChecksumCalculatingInputStreamConstruction() {
        final Checksum checksum = mock(Checksum.class);
        final InputStream inputStream = mock(InputStream.class);
        new ChecksumCalculatingInputStream(checksum, inputStream);
        verify(checksum).reset();
        verify(inputStream).mark(0);
    }

    /**
     * Test for {@link ChecksumCalculatingInputStream#read()}
     */
    @Test
    public void testRead() throws IOException {
        final byte[] data = new byte[] { 0x01, 0x02 };
        final InputStream inputStream = new ByteArrayInputStream(data);
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream testSubject = new ChecksumCalculatingInputStream(checksum, inputStream);
        final int actual = testSubject.read();
        assertEquals(data[0], actual);
        verify(checksum).update(data[0]);

        final int actual2 = testSubject.read();
        assertEquals(data[1], actual2);
        verify(checksum).update(data[1]);

        final int actual3 = testSubject.read();
        assertEquals(-1, actual3);
    }

    /**
     * Test for {@link ChecksumCalculatingInputStream#read(byte[], int, int)}
     */
    @Test
    public void testReadArray() throws IOException {
        final byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        final byte[] expected = new byte[] { 0x01, 0x02 };
        final InputStream inputStream = new ByteArrayInputStream(data);
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream testSubject = new ChecksumCalculatingInputStream(checksum, inputStream);
        final int actual = testSubject.read(expected, 0, expected.length);
        assertArrayEquals(expected, new byte[] { 0x01, 0x02 });
        verify(checksum).update(expected, 0, expected.length);

        final int actual2 = testSubject.read(expected, 0, 3);
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, expected);
        verify(checksum).update(expected, 0, 3);

        final int actual3 = testSubject.read(expected, 2, 2);
        assertArrayEquals(new byte[] { 0x03, 0x04 }, expected);
        verify(checksum).update(expected, 2, 2);

        final int actual4 = testSubject.read(expected, 4, 2);
        assertArrayEquals(new byte[] {}, expected);
    }

    /**
     * Test for {@link ChecksumCalculatingInputStream#skip(long)}
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSkip() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        final Checksum checksum = mock(Checksum.class);
        final ChecksumCalculatingInputStream testSubject = new ChecksumCalculatingInputStream(checksum, inputStream);
        testSubject.skip(1);
    }

    /**
     * Test for {@link ChecksumCalculatingInputStream#getValue()}
     */
    @Test
    public void testValue() throws NoSuchAlgorithmException, IOException {
        final byte[] data = new byte[] { 0x01, 0x02, 0x03, 0x04 };
        final InputStream inputStream = new ByteArrayInputStream(data);
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final Checksum checksum = new Checksum() {
            @Override
            public void reset() {
            }

            @Override
            public void update(byte b) {
                messageDigest.update(new byte[] { b });
            }

            @Override
            public void update(byte[] b, int off, int len) {
                messageDigest.update(b, off, len);
            }

            @Override
            public long getValue() {
                return messageDigest.digest();
            }
        };
        final ChecksumCalculatingInputStream testSubject = new ChecksumCalculatingInputStream(checksum, inputStream);
        final byte[] expected = messageDigest.digest(data);
        assertEquals(expected, testSubject.getValue());
    }
}