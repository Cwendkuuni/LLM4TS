package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
    private ChecksumCalculatingInputStream inputStream;

    @Before
    public void setUp() throws Exception {
        Checksum checksum = new Checksum() {
            private long value;

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

        byte[] data = {1, 2, 3, 4, 5};
        inputStream = new ChecksumCalculatingInputStream(checksum, new ByteArrayInputStream(data));
    }

    @Test
    public void testRead() throws IOException {
        int b1 = inputStream.read();
        int b2 = inputStream.read();
        int b3 = inputStream.read();
        int b4 = inputStream.read();
        int b5 = inputStream.read();
        int b6 = inputStream.read();
        
        assertEquals(1, b1);
        assertEquals(2, b2);
        assertEquals(3, b3);
        assertEquals(4, b4);
        assertEquals(5, b5);
        assertEquals(-1, b6);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];

        int bytesRead = inputStream.read(buffer);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3}, buffer);

        bytesRead = inputStream.read(buffer);

        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{4, 5, 3}, buffer);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        byte[] buffer = new byte[5];

        int bytesRead = inputStream.read(buffer, 1, 3);

        assertEquals(3, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 0}, buffer);

        bytesRead = inputStream.read(buffer, 2, 4);

        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[]{0, 1, 4, 5, 0}, buffer);
    }

    @Test
    public void testSkip() throws IOException {
        long skippedBytes = inputStream.skip(3);

        assertEquals(3, skippedBytes);

        skippedBytes = inputStream.skip(5);

        assertEquals(0, skippedBytes);
    }

    @Test
    public void testGetValue() {
        long value = inputStream.getValue();

        assertEquals(15, value);
    }

}