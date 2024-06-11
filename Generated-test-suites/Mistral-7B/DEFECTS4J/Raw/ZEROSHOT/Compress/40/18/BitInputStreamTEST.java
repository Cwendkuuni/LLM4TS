package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test class for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45, 0x67});
        byteOrder = ByteOrder.LITTLE_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(bitInputStream);
        assertEquals(inputStream, bitInputStream.in);
        assertEquals(ByteOrder.LITTLE_ENDIAN, bitInputStream.byteOrder);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.bitsCached);
        assertEquals(0, bitInputStream.bitsCachedSize);
    }

    @Test
    public void testReadBits_lessThanOrEqualMaximumSize() throws IOException {
        long expected = 0;

        for (int count = 0; count <= MAXIMUM_CACHE_SIZE; count++) {
            long actual = bitInputStream.readBits(count);

            if (actual == -1) {
                break;
            }

            expected |= readExpectedBits(count);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testReadBits_greaterThanMaximumSize() {
        assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(MAXIMUM_CACHE_SIZE + 1));
    }

    @Test
    public void testReadBits_endOfStream() throws IOException {
        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits_noBitsLeft() throws IOException {
        bitInputStream.clearBitCache();
        inputStream.read(); // consume first byte
        bitInputStream.readBits(8); // consume all bits from first byte
        assertEquals(-1, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits_bigEndian() throws IOException {
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        bitInputStream = new BitInputStream(inputStream, byteOrder);

        long expected = 0;

        for (int count = 0; count <= MAXIMUM_CACHE_SIZE; count++) {
            long actual = bitInputStream.readBits(count);

            if (actual == -1) {
                break;
            }

            expected |= readExpectedBitsBigEndian(count);
            assertEquals(expected, actual);
        }
    }

    private long readExpectedBits(int count) {
        long result = 0;
        int index = 0;
        while (index < 4) {
            long bitsFromByte = (inputStream.read() & 0xFF) << (8 * (3 - index));
            result |= (bitsFromByte >>> (8 * index)) & MASKS[count];
            index++;
        }
        return result;
    }

    private long readExpectedBitsBigEndian(int count) {
        long result = 0;
        int index = 0;
        while (index < 4) {
            long bitsFromByte = ((inputStream.read() & 0xFF) << (8 * index));
            result |= bitsFromByte >>> (8 * (3 - index)) & MASKS[count];
            index++;
        }
        return result;
    }
}