package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    private InputStream inputStream;

    @Before
    public void setup() {
        inputStream = mock(InputStream.class);
    }

    @After
    public void teardown() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Swallow exception as we mocked the InputStream
            }
        }
    }

    @Test
    public void testReadBitsWithPositiveCountAndLessThanMaximum() throws IOException {
        givenInputStreamReturnsByte(0x0F);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long bits = bitInputStream.readBits(6);

        thenBitsRead(bits, 0b111111);
    }

    @Test
    public void testReadBitsWithPositiveCountAndEqualToMaximum() throws IOException {
        givenInputStreamReturnsByte(0x0F);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        final long bits = bitInputStream.readBits(MAXIMUM_CACHE_SIZE);

        thenBitsRead(bits, 0b111111111111);
    }

    @Test
    public void testReadBitsWithNegativeCount() {
        givenInputStreamReturnsByte(0x0F);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
    }

    @Test
    public void testReadBitsWithCountGreaterThanMaximum() {
        givenInputStreamReturnsByte(0x0F);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(MAXIMUM_CACHE_SIZE + 1));
    }

    @Test
    public void testReadBitsWithEndOfStream() throws IOException {
        givenInputStreamReturnsByte(-1);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test
    public void testClearBitCache() throws IOException {
        givenInputStreamReturnsByte(0x0F);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1); // Read 1 bit
        bitInputStream.clearBitCache();

        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBitsWithDifferentByteOrder() throws IOException {
        givenInputStreamReturnsByte(0x0F);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        final long bits = bitInputStream.readBits(6);

        thenBitsRead(bits, 0b111111000000);
    }

    private void givenInputStreamReturnsByte(int byteValue) throws IOException {
        when(inputStream.read()).thenReturn((byte) byteValue);
    }

    private void thenBitsRead(long expectedBits, long bitsMask) {
        assertEquals(expectedBits, bitsMask & bitsCachedLong(expectedBits));
        verify(inputStream).read();
    }

    private long bitsCachedLong(long bits) {
        long cachedBits = bitsCached;
        long cachedSize = bitsCachedSize;

        while (cachedSize > 0 && cachedBits < bits) {
            cachedBits <<= 8;
            cachedSize -= 8;
        }

        return cachedBits;
    }
}