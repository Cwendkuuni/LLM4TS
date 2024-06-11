package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        BaseNCodec baseNCodec = new BaseNCodec();
        boolean doEncode = false;
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, doEncode);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('t', baseNCodecInputStream.read());
        assertEquals('e', baseNCodecInputStream.read());
        assertEquals('s', baseNCodecInputStream.read());
        assertEquals('t', baseNCodecInputStream.read());
    }

    @Test
    public void testReadEmptyStream() throws IOException {
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec emptyBaseNCodec = new BaseNCodec();
        boolean doEncode = false;
        baseNCodecInputStream = new BaseNCodecInputStream(emptyInputStream, emptyBaseNCodec, doEncode);

        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadWithOffsetAndLen() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(4, baseNCodecInputStream.read(buffer, 0, 4));
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, -1, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, 5, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLen() throws IOException {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, 0, 5);
    }

    @Test
    public void testReadWithZeroLen() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(0, baseNCodecInputStream.read(buffer, 0, 0));
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() {
        assertEquals(1, baseNCodecInputStream.available());
    }
}