package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws IOException {
        InputStream in = new InputStream() {
            int[] testData = {1, 2, 3, 4, 5, 6};
            int currentIndex = 0;

            public int read() throws IOException {
                if (currentIndex >= testData.length) {
                    return -1;
                }
                return testData[currentIndex++];
            }
        };

        Checksum checksum = new Checksum() {
            private long value = 0;

            public void update(int b) {
                value += b;
            }

            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    value += b[i];
                }
            }

            public long getValue() {
                return value;
            }

            public void reset() {
                value = 0;
            }
        };

        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(1, ccis.read());
        assertEquals(2, ccis.read());
        assertEquals(3, ccis.read());
        assertEquals(4, ccis.read());
        assertEquals(5, ccis.read());
        assertEquals(6, ccis.read());
        assertEquals(-1, ccis.read());

        assertEquals(21, ccis.getValue());
    }

    @Test
    public void testReadByteArray() throws IOException {
        InputStream in = new InputStream() {
            byte[] testData = {1, 2, 3, 4, 5, 6};
            int currentIndex = 0;

            public int read() throws IOException {
                if (currentIndex >= testData.length) {
                    return -1;
                }
                return testData[currentIndex++];
            }
        };

        byte[] b = new byte[3];
        Checksum checksum = new Checksum() {
            private long value = 0;

            public void update(int b) {
                value += b;
            }

            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    value += b[i];
                }
            }

            public long getValue() {
                return value;
            }

            public void reset() {
                value = 0;
            }
        };

        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(3, ccis.read(b));

        assertEquals(1, b[0]);
        assertEquals(2, b[1]);
        assertEquals(3, b[2]);

        assertEquals(6, ccis.getValue());
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        InputStream in = new InputStream() {
            byte[] testData = {1, 2, 3, 4, 5, 6};
            int currentIndex = 0;

            public int read() throws IOException {
                if (currentIndex >= testData.length) {
                    return -1;
                }
                return testData[currentIndex++];
            }
        };

        byte[] b = new byte[6];
        Checksum checksum = new Checksum() {
            private long value = 0;

            public void update(int b) {
                value += b;
            }

            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    value += b[i];
                }
            }

            public long getValue() {
                return value;
            }

            public void reset() {
                value = 0;
            }
        };

        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(6, ccis.read(b, 0, 6));

        assertEquals(1, b[0]);
        assertEquals(2, b[1]);
        assertEquals(3, b[2]);
        assertEquals(4, b[3]);
        assertEquals(5, b[4]);
        assertEquals(6, b[5]);

        assertEquals(21, ccis.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        InputStream in = new InputStream() {
            int[] testData = {1, 2, 3, 4, 5, 6};
            int currentIndex = 0;

            public int read() throws IOException {
                if (currentIndex >= testData.length) {
                    return -1;
                }
                return testData[currentIndex++];
            }
        };

        Checksum checksum = new Checksum() {
            private long value = 0;

            public void update(int b) {
                value += b;
            }

            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    value += b[i];
                }
            }

            public long getValue() {
                return value;
            }

            public void reset() {
                value = 0;
            }
        };

        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(1, ccis.skip(1));
        assertEquals(2, ccis.read());
        assertEquals(3, ccis.read());
        assertEquals(4, ccis.read());
        assertEquals(5, ccis.read());
        assertEquals(6, ccis.read());
        assertEquals(-1, ccis.read());

        assertEquals(20, ccis.getValue());
    }

    // Test for edge case where the stream is empty
    @Test
    public void testEmptyInputStream() throws IOException {
        InputStream in = new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        };

        Checksum checksum = new Checksum() {
            private long value = 0;

            public void update(int b) {
                value += b;
            }

            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    value += b[i];
                }
            }

            public long getValue() {
                return value;
            }

            public void reset() {
                value = 0;
            }
        };

        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, in);

        assertEquals(-1, ccis.read());

        assertEquals(0, ccis.getValue());
    }

    // Test for exception throwing when the checksum doesn't match the expected value
    @Test(expected = IOException.class)
    public void testChecksumMismatch() throws IOException {
        InputStream in = new InputStream() {
            int[] testData = {1, 2, 3, 4, 5, 6};
            int currentIndex = 0;

            public int read() throws IOException {
                if (currentIndex >= testData.length) {
                    return -1;
                }
                return testData[currentIndex++];
            }
        };

        Checksum checksum = new Checksum() {
            private long value = 0;

            public void update(int b) {
                value += b;
            }

            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    value += b[i];
                }
            }

            public long getValue() {
                return value;
            }

            public void reset() {
                value = 0;
            }
        };

        ChecksumCalculatingInputStream ccis = new ChecksumCalculatingInputStream(checksum, in);

        ccis.read();

        assertEquals(20, ccis.getValue());
    }
}