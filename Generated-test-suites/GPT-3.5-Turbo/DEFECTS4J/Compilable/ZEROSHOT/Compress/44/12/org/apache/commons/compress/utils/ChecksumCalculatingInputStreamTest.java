package org.apache.commons.compress.utils;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        try {
            assertEquals(1, calcInputStream.read());
            assertEquals(1, calcInputStream.getValue());
            assertEquals(2, calcInputStream.read());
            assertEquals(3, calcInputStream.getValue());
            assertEquals(3, calcInputStream.read());
            assertEquals(6, calcInputStream.getValue());
            assertEquals(-1, calcInputStream.read());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[2];
        try {
            assertEquals(2, calcInputStream.read(buffer));
            assertEquals(3, calcInputStream.getValue());
            assertArrayEquals(new byte[]{1, 2}, buffer);

            assertEquals(1, calcInputStream.read(buffer));
            assertEquals(6, calcInputStream.getValue());
            assertArrayEquals(new byte[]{3, 2}, buffer);

            assertEquals(-1, calcInputStream.read(buffer));
            assertEquals(6, calcInputStream.getValue());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayWithOffset() {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        byte[] buffer = new byte[3];
        try {
            assertEquals(3, calcInputStream.read(buffer, 0, 3));
            assertEquals(6, calcInputStream.getValue());
            assertArrayEquals(new byte[]{1, 2, 3}, buffer);

            assertEquals(-1, calcInputStream.read(buffer, 0, 3));
            assertEquals(6, calcInputStream.getValue());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        try {
            long bytesSkipped = calcInputStream.skip(2);
            assertEquals(2, bytesSkipped);
            assertEquals(3, calcInputStream.getValue());

            bytesSkipped = calcInputStream.skip(1);
            assertEquals(1, bytesSkipped);
            assertEquals(6, calcInputStream.getValue());

            bytesSkipped = calcInputStream.skip(1);
            assertEquals(0, bytesSkipped);
            assertEquals(6, calcInputStream.getValue());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Checksum checksum = new Adler32();
        ChecksumCalculatingInputStream calcInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        try {
            assertEquals(0, calcInputStream.getValue());

            calcInputStream.read();
            assertEquals(1, calcInputStream.getValue());

            calcInputStream.read();
            assertEquals(3, calcInputStream.getValue());

            calcInputStream.read();
            assertEquals(6, calcInputStream.getValue());

            calcInputStream.read();
            assertEquals(6, calcInputStream.getValue());
        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        }
    }
}