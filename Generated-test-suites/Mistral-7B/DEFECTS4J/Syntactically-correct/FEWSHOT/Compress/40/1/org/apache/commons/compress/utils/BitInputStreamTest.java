package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for BitInputStream class.
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
    }

    @Test
    public void testBitInputStreamConstructor() {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x23, 0x45, 0x67 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
        assertEquals(inputStream, bitInputStream.in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamConstructorWithInvalidByteOrder() {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = createBitInputStream();
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithZeroCount() throws IOException {
        BitInputStream bitInputStream = createBitInputStream();
        long bits = bitInputStream.readBits(0);
        assertNull(bits);
    }

    @Test
    public void testReadBitsWithOneBit() throws IOException {
        BitInputStream bitInputStream = createBitInputStream();
        long bits = bitInputStream.readBits(1);
        assertEquals(1, bits);
    }

    @Test
    public void testReadBitsWithSeveralBits() throws IOException {
        BitInputStream bitInputStream = createBitInputStream();
        long bits = bitInputStream.readBits(5);
        long expectedBits = 0b110111;
        assertEquals(expectedBits, bits);
        long readByte = 0x25;
        when(inputStream.read()).thenReturn(readByte);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bits = bitInputStream.readBits(5);
        expectedBits = 0b111111 & (readByte >>> 3);
        assertEquals(expectedBits, bits);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        BitInputStream bitInputStream = createBitInputStream();
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountBiggerThanMaximum() throws IOException {
        BitInputStream bitInputStream = createBitInputStream();
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsUntilEndOfStreamIsReached() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x23, 0x45, 0x67 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = 0;
        while (bits < 24) {
            bits += bitInputStream.readBits(8);
        }
        assertEquals(24, bits);
        assertEquals(-1, bitInputStream.readBits(8));
    }

    @Test
    public void testReadBitsWithByteOrderLittleEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x23, 0x45, 0x67 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        long bits = bitInputStream.readBits(5);
        long expectedBits = 0b11011;
        assertEquals(expectedBits, bits);

        long bits2 = bitInputStream.readBits(3);
        long expectedBits2 = 0b111 & (0x23 >>> 5);
        assertEquals(expectedBits2, bits2);
    }

    @Test
    public void testReadBitsWithByteOrderBigEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x23, 0x45, 0x67 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        long bits = bitInputStream.readBits(5);
        long expectedBits = 0b111111 & (0x67 << 3);
        assertEquals(expectedBits, bits);

        long bits2 = bitInputStream.readBits(3);
        long expectedBits2 = 0b111 & (0x67 >>> 5);
        assertEquals(expectedBits2, bits2);
    }

    @Test
    public void testReadBitsWithByteOrderMiddleEndian() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x23, 0x45, 0x67 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.nativeOrder());
        long bits = bitInputStream.readBits(5);
        long expectedBits = 0b111111 & ((0x67 >>> 1) << 3);
        assertEquals(expectedBits, bits);

        long bits2 = bitInputStream.readBits(3);
        long expectedBits2 = 0b111 & ((0x67 >>> 4) << 3);
        assertEquals(expectedBits2, bits2);
    }

    private BitInputStream createBitInputStream() throws IOException {
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        InputStream inputStream = mock(InputStream.class);
        return new BitInputStream(inputStream, byteOrder);
    }
}