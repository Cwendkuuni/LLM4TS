
package org.apache.commons.codec.binary;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[]{1, 2, 3, 4, 5};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        int result = baseNCodecInputStream.read();
        assertEquals(1, result);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int result = baseNCodecInputStream.read(buffer, 0, 5);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, buffer);
        assertEquals(5, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeLength() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 10, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidLength() throws IOException {
        byte[] buffer = new byte[5];
        baseNCodecInputStream.read(buffer, 4, 2);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }
}
