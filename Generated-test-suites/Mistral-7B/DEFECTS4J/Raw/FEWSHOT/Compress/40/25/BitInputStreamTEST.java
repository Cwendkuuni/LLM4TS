package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBits_LessThanOrEqualToMaximumCacheSize() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        when(inputStream.read()).thenReturn((byte) 0b1111_0111); // -3
        when(inputStream.read()).thenReturn((byte) 0b1111_1111); // -1

        // Test reading 1 bit
        assertEquals(0b0000_0001L, bitInputStream.readBits(1));
        assertEquals(0b0000_0001L, bitInputStream.readBits(1));
        assertEquals(-3, bitInputStream.readBits(8));

        // Test reading multiple bits
        assertEquals(0b0111_0111L, bitInputStream.readBits(3));
        assertEquals(0b1111_1100L, bitInputStream.readBits(4));
        assertEquals(0b1111_1111L, bitInputStream.readBits(8));

        // Test reading all bits from the byte
        assertEquals(0b1111_0111L, bitInputStream.readBits(7));
        assertEquals(0b1111_1111L, bitInputStream.readBits(8));

        // Test clearing cache
        bitInputStream.clearBitCache();
        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_CountNegative() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { -1 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_CountGreaterThanMaximum() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { -1 });
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(64);
    }
}