package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private final byte[] testData = { 1, 2, 3, 4 };
    private final long expectedChecksum = 118;

    @Before
    public void setUp() {
        inputStream = new ChecksumCalculatingInputStream(new ChecksumImpl(), new TestInputStream(testData));
    }

    @Test
    public void testReadSingleByte() throws Exception {
        assertEquals(1, inputStream.read());
        assertEquals(expectedChecksum, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] buffer = new byte[2];
        assertEquals(2, inputStream.read(buffer));
        assertEquals(expectedChecksum, inputStream.getValue());
    }

    @Test
    public void testReadOffsetAndLength() throws Exception {
        byte[] buffer = new byte[4];
        assertEquals(4, inputStream.read(buffer, 0, 4));
        assertEquals(expectedChecksum, inputStream.getValue());
    }

    @Test
    public void testSkip() throws Exception {
        assertEquals(1, inputStream.skip(1));
        assertEquals(expectedChecksum, inputStream.getValue());
    }

    private class TestInputStream extends InputStream {

        private final byte[] data;
        private int position;

        public TestInputStream(byte[] data) {
            this.data = data;
            this.position = 0;
        }

        @Override
        public int read() throws IOException {
            if (position < data.length) {
                int value = data[position];
                position++;
                return value;
            } else {
                return -1;
            }
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (position < data.length) {
                int count = Math.min(len, data.length - position);
                for (int i = 0; i < count; i++) {
                    b[off + i] = data[position + i];
                }
                position += count;
                return count;
            } else {
                return -1;
            }
        }
    }

    private class ChecksumImpl implements Checksum {

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