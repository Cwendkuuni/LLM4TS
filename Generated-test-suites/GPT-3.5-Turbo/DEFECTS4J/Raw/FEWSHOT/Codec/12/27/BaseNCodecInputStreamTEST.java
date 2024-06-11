package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {
    private byte[] testData;
    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        testData = new byte[] { 0x61, 0x62, 0x63, 0x64 };
        baseNCodec = new BaseNCodec();
        baseNCodecInputStream = new BaseNCodecInputStream(testData, baseNCodec, false);
    }

    @Test
    public void testRead() throws Exception {
        assertEquals(97, baseNCodecInputStream.read());
        assertEquals(98, baseNCodecInputStream.read());
        assertEquals(99, baseNCodecInputStream.read());
        assertEquals(100, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] buffer = new byte[2];

        int bytesRead = baseNCodecInputStream.read(buffer);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] { 97, 98 }, buffer);

        bytesRead = baseNCodecInputStream.read(buffer);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] { 99, 100 }, buffer);

        bytesRead = baseNCodecInputStream.read(buffer);
        assertEquals(-1, bytesRead);
        assertArrayEquals(new byte[] { 99, 100 }, buffer);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws Exception {
        byte[] buffer = new byte[4];

        int bytesRead = baseNCodecInputStream.read(buffer, 1, 2);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] { 0, 97, 98, 0 }, buffer);

        bytesRead = baseNCodecInputStream.read(buffer, 0, 2);
        assertEquals(2, bytesRead);
        assertArrayEquals(new byte[] { 99, 100, 98, 0 }, buffer);

        bytesRead = baseNCodecInputStream.read(buffer, 0, 2);
        assertEquals(-1, bytesRead);
        assertArrayEquals(new byte[] { 99, 100, 98, 0 }, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws Exception {
        baseNCodecInputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidOffset() throws Exception {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, -1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidLength() throws Exception {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithInvalidBufferSize() throws Exception {
        byte[] buffer = new byte[4];
        baseNCodecInputStream.read(buffer, 0, 5);
    }

    @Test
    public void testMarkSupported() {
        assertEquals(false, baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() throws Exception {
        assertEquals(1, baseNCodecInputStream.available());
        baseNCodecInputStream.read();
        assertEquals(1, baseNCodecInputStream.available());
        baseNCodecInputStream.read();
        assertEquals(1, baseNCodecInputStream.available());
        baseNCodecInputStream.read();
        assertEquals(1, baseNCodecInputStream.available());
        baseNCodecInputStream.read();
        assertEquals(0, baseNCodecInputStream.available());
    }
}