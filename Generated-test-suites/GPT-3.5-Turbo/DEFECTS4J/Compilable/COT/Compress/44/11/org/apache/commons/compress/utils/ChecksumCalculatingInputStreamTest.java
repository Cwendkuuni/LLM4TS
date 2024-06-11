package org.apache.commons.compress.utils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead_singleByte() throws Exception {
        // given
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(null, null);

        // when
        int result = stream.read();

        // then
        assertEquals(-1, result);
    }

    @Test
    public void testRead_byteArray() throws Exception {
        // given
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(null, null);
        byte[] buffer = new byte[10];

        // when
        int result = stream.read(buffer);

        // then
        assertEquals(-1, result);
    }

    @Test
    public void testRead_byteArray_withOffset() throws Exception {
        // given
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(null, null);
        byte[] buffer = new byte[10];
        int offset = 5;

        // when
        int result = stream.read(buffer, offset, buffer.length - offset);

        // then
        assertEquals(-1, result);
    }

    @Test
    public void testSkip() throws Exception {
        // given
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(null, null);

        // when
        long result = stream.skip(10);

        // then
        assertEquals(0, result);
    }

    @Test
    public void testGetValue() throws Exception {
        // given
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(null, null);

        // when
        long result = stream.getValue();

        // then
        assertEquals(0, result);
    }

}