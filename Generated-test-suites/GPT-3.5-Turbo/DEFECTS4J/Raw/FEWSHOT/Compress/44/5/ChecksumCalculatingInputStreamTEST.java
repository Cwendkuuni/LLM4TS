package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int result = inputStream.read();

        assertEquals(1, result);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testReadWithEmptyInputStream() throws IOException {
        byte[] data = {};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        int result = inputStream.read();

        assertEquals(-1, result);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testReadWithMultipleBytes() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[2];
        int result = inputStream.read(buffer);

        assertEquals(2, result);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer, 1, 2);

        assertEquals(2, result);
        assertEquals(1, checksum.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long result = inputStream.skip(2);

        assertEquals(2, result);
        assertEquals(3, checksum.getValue());
    }

    @Test
    public void testSkipWithEmptyInputStream() throws IOException {
        byte[] data = {};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long result = inputStream.skip(2);

        assertEquals(0, result);
        assertEquals(0, checksum.getValue());
    }

    @Test
    public void testGetValue() throws IOException {
        byte[] data = {1, 2, 3};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Checksum checksum = new DummyChecksum();
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        long result = inputStream.getValue();

        assertEquals(0, result);
        inputStream.read();
        result = inputStream.getValue();
        assertEquals(1, result);
        inputStream.read();
        result = inputStream.getValue();
        assertEquals(3, result);
    }

    private class DummyChecksum implements Checksum {
        private long value;

        @Override
        public void update(int b) {
            value += b;
        }

        @Override
        public void update(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) {
                value += b[i];
            }
        }

        @Override
        public long getValue() {
            return value;
        }

        @Override
        public void reset() {
            value = 0;
        }
    }
}