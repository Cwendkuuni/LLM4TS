package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream cis;

    @Before
    public void setUp() {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[]{1, 2, 3, 4, 5});
        Checksum checksum = new Checksum() {
            @Override
            public void update(int b) {
                // do nothing
            }

            @Override
            public void update(byte[] b, int off, int len) {
                // do nothing
            }

            @Override
            public long getValue() {
                return 0;
            }

            @Override
            public void reset() {
                // do nothing
            }
        };
        cis = new ChecksumCalculatingInputStream(checksum, bais);
    }

    @Test
    public void testRead() throws IOException {
        int byteRead = cis.read();
        assertEquals(1, byteRead);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] byteArray = new byte[5];
        int bytesRead = cis.read(byteArray);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, byteArray);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] byteArray = new byte[7];
        int bytesRead = cis.read(byteArray, 1, 5);
        assertArrayEquals(new byte[]{0, 1, 2, 3, 4, 5, 0}, byteArray);
        assertEquals(5, bytesRead);
    }

    @Test
    public void testGetValue() {
        assertEquals(0, cis.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long bytesSkipped = cis.skip(3);
        assertEquals(1, bytesSkipped);
    }

    @Test
    public void testSkipLarge() throws IOException {
        long bytesSkipped = cis.skip(10);
        assertEquals(0, bytesSkipped);
    }
    
    @Test(expected = IOException.class)
    public void testReadException() throws IOException {
        cis.read();
        cis.read();
    }
    
    @Test(expected = IOException.class)
    public void testReadByteArrayException() throws IOException {
        byte[] byteArray = new byte[5];
        cis.read(byteArray);
        cis.read(byteArray);
    }
    
    @Test(expected = IOException.class)
    public void testReadByteArrayWithOffsetException() throws IOException {
        byte[] byteArray = new byte[7];
        cis.read(byteArray, 1, 5);
        cis.read(byteArray, 1, 5);
    }
}