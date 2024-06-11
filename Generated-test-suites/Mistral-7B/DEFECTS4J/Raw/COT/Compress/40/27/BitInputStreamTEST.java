package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for BitInputStream class
 */
public class BitInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setup() {
        inputStream = mock(InputStream.class);
    }

    // Test constructor
    @Test
    public void testBitInputStreamConstructor() {
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    // Test close method
    @Test
    public void testCloseMethod() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        assertFalse(inputStream.isOpen());
    }

    // Test clearBitCache method
    @Test
    public void testClearBitCacheMethod() {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    // Test readBits method with valid count
    @Test
    public void testReadBitsMethodValidCount() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x1a, (byte) 0x2b, (byte) 0x0c);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long expected = 0b111101101011_01101100 & MASKS[7]; // first 7 bits from first byte
        final long result = bitInputStream.readBits(7);
        assertEquals(expected, result);
    }

    // Test readBits method with zero count
    @Test
    public void testReadBitsMethodZeroCount() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long result = bitInputStream.readBits(0);
        assertEquals(0, result);
    }

    // Test readBits method with negative count
    @Test
    public void testReadBitsMethodNegativeCount() {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    // Test readBits method with count greater than MAXIMUM_CACHE_SIZE
    @Test
    public void testReadBitsMethodCountGreaterThanMaximumCacheSize() {
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
    }

    // Test readBits method with end of stream reached
    @Test
    public void testReadBitsMethodEndOfStreamReached() throws IOException {
        when(inputStream.read()).thenReturn((byte) 0x1a);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        final long result = bitInputStream.readBits(1);
        assertEquals(-1, result);
    }
}