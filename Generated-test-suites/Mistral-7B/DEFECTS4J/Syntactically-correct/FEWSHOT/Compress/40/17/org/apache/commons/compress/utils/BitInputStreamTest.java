package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {

    @Test
    public void testReadBitsWithLittleEndianBitOrder() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        when(inputStream.read()).thenReturn(bytes[0]).thenReturn(bytes[1]).thenReturn(bytes[2]).thenReturn(bytes[3])
                .thenReturn(bytes[4]).thenReturn(-1);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        // Test reading 1 bit
        assertEquals(0b00000001L, bitInputStream.readBits(1));
        assertEquals(0b00000000L, bitInputStream.readBits(1));

        // Test reading 2 bits
        assertEquals(0b00000110L, bitInputStream.readBits(2));

        // Test reading 3 bits
        assertEquals(0b00111011L, bitInputStream.readBits(3));

        // Test reading 4 bits
        assertEquals(0b01111011L, bitInputStream.readBits(4));

        // Test reading 5 bits
        assertEquals(0b11111101L, bitInputStream.readBits(5));

        // Test reading 6 bits
        assertEquals(0b11111110L, bitInputStream.readBits(6));

        // Test reading 7 bits
        assertEquals(0xFF & 0b11111111L, bitInputStream.readBits(7));

        // Test reading 8 bits
        assertEquals(0xFF & 0x01020304L, bitInputStream.readBits(8));

        // Test reading 9 bits (should throw an exception)
        try {
            bitInputStream.readBits(9);
            fail("Expected IllegalArgumentException when reading 9 bits");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test reading 63 bits
        long expectedValue = 0x2468A3F4F68D6666L;
        assertEquals(expectedValue, bitInputStream.readBits(63));

        // Test reaching the end of the stream
        assertEquals(-1L, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBitsWithBigEndianBitOrder() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final byte[] bytes = {0x01, 0x02, 0x03, 0x04, 0x05};
        when(inputStream.read()).thenReturn(bytes[0]).thenReturn(bytes[1]).thenReturn(bytes[2]).thenReturn(bytes[3])
                .thenReturn(bytes[4]).thenReturn(-1);

        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        // Test reading 1 bit
        assertEquals(0b10000001L, bitInputStream.readBits(1));
        assertEquals(0b10000000L, bitInputStream.readBits(1));

        // Test reading 2 bits
        assertEquals(0b11000011L, bitInputStream.readBits(2));

        // Test reading 3 bits
        assertEquals(0b11100111L, bitInputStream.readBits(3));

        // Test reading 4 bits
        assertEquals(0b11111011L, bitInputStream.readBits(4));

        // Test reading 5 bits
        assertEquals(0b11111101L, bitInputStream.readBits(5));

        // Test reading 6 bits
        assertEquals(0b11111110L, bitInputStream.readBits(6));

        // Test reading 7 bits
        assertEquals(0xFF & 0x807F807FL, bitInputStream.readBits(7));

        // Test reading 8 bits
        assertEquals(0xFF & 0x04030201L, bitInputStream.readBits(8));

        // Test reading 9 bits (should throw an exception)
        try {
            bitInputStream.readBits(9);
            fail("Expected IllegalArgumentException when reading 9 bits");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }

        // Test reading 63 bits
        long expectedValue = 0x2468A3F4F68D6666L;
        assertEquals(expectedValue, bitInputStream.readBits(63));

        // Test reaching the end of the stream
        assertEquals(-1L, bitInputStream.readBits(1));
    }
}