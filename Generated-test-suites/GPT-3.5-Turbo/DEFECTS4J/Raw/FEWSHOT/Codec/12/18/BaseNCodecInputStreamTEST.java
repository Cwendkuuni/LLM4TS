package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private BaseNCodec codec;

    @Before
    public void setUp() {
        codec = new BaseNCodec();
    }

    @Test
    public void testRead() {
        assertEquals(102, codec.read(singleByte, 0, 1));
        assertEquals(EOF, codec.read(singleByte, 0, 1));
    }

    @Test
    public void testReadWithOffsetAndLength() {
        byte[] buffer = new byte[10];
        assertEquals(5, codec.read(buffer, 0, 5));
        assertEquals(EOF, codec.read(buffer, 0, 10));
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws NullPointerException {
        codec.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IndexOutOfBoundsException {
        byte[] buffer = new byte[5];
        codec.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IndexOutOfBoundsException {
        byte[] buffer = new byte[5];
        codec.read(buffer, 0, 6);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IndexOutOfBoundsException {
        byte[] buffer = new byte[5];
        codec.read(buffer, 6, 6);
    }

    @Test
    public void testMarkSupported() {
        assertEquals(false, codec.markSupported());
    }

    @Test
    public void testAvailable() {
        assertEquals(1, codec.available());
    }
}