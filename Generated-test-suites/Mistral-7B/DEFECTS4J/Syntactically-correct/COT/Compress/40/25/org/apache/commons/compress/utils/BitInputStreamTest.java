package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    @org.junit.Before
    public void setup() {
        InputStream inputStreamMock = Mockito.mock(InputStream.class);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c, (byte) 0x4d});
        Mockito.when(inputStreamMock.read()).thenReturn(byteArrayInputStream.read);
        BitInputStream bitInputStream = new BitInputStream(inputStreamMock, ByteOrder.LITTLE_ENDIAN);
        this.bitInputStream = bitInputStream;
    }

    private BitInputStream bitInputStream;

    @org.junit.Test
    public void testBitInputStreamConstructor() throws IOException {
        assertNotNull(bitInputStream);
    }

    @org.junit.Test
    public void testClose() throws IOException {
        bitInputStream.close();
        Mockito.verify(bitInputStream.in).close();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }

    @org.junit.Test
    public void testReadBitsSmallCount() throws IOException {
        long result = bitInputStream.readBits(2);
        assertEquals(0b01, result);

        result = bitInputStream.readBits(3);
        assertEquals(0b111, result);

        result = bitInputStream.readBits(5);
        assertEquals(0b101111, result);
    }

    @org.junit.Test
    public void testReadBitsEndOfStream() throws IOException {
        byteArrayInputStream.readAllBytes(); // read all bytes from the input stream
        long result = bitInputStream.readBits(1);
        assertEquals(-1, result);
    }

    @org.junit.Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(3);
        assertEquals(0, result);
    }
}