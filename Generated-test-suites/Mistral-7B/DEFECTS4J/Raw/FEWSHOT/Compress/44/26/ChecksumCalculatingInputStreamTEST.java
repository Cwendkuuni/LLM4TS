package org.apache.commons.compress.utils;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
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
    public void testChecksumCalculatingInputStream() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // This shouldn't happen
        }

        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(md, input);

        byte[] expected = md.digest();
        byte[] buffer = new byte[input.available()];
        input.read(buffer);

        assertNotNull(checksumInput.checksum);
        assertSame(md, checksumInput.checksum);
        assertSame(input, checksumInput.in);

        assertEquals(expected.length, checksumInput.getValue(), 0);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01});
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(null, input);

        byte byteRead = (byte) checksumInput.read();
        byte expected = (byte) 0x01;

        assertEquals(expected, byteRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(null, input);

        byte[] buffer = new byte[3];
        int length = checksumInput.read(buffer, 0, buffer.length);

        assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(null, input);

        long skipped = checksumInput.skip(2);

        assertEquals(2, skipped);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnRead() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        InputStream input = new ByteArrayInputStream(new byte[]{});
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(null, input);

        checksumInput.read();
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnSkip() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[]{});
        ChecksumCalculatingInputStream checksumInput = new ChecksumCalculatingInputStream(null, input);

        checksumInput.skip(1);
    }
}