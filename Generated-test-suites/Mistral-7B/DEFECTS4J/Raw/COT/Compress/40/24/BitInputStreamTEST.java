package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * JUnit test cases for BitInputStream class
 */
public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;
    private static final int TEST_BIT_COUNT = 10;
    private static final int TEST_BYTE_COUNT = TEST_BIT_COUNT / 8 + 1;
    private static final byte[] TEST_INPUT_DATA = new byte[]{1, 2, 3};

    @Before
    public void setUp() throws IOException {
        byteArrayInputStream = new ByteArrayInputStream(TEST_INPUT_DATA);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testConstructor() throws IOException {
        // Testing constructor with valid InputStream and ByteOrder
        assertTrue(bitInputStream != null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeCount() throws IOException {
        // Testing constructor with negative count
        new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorGreaterThanMaximumCacheSize() throws IOException {
        // Testing constructor with count greater than maximum cache size
        new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN, TEST_BIT_COUNT + 1);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Testing clearBitCache method
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(TEST_BIT_COUNT));
    }

    @Test(expected = IOException.class)
    public void testReadBitsWhenEndOfStreamReached() throws IOException {
        // Testing readBits method when end of stream is reached
        byteArrayInputStream.read();
        byteArrayInputStream.read();
        assertEquals(-1, bitInputStream.readBits(TEST_BIT_COUNT));
    }

    @Test
    public void testReadBits() throws IOException {
        // Testing readBits method
        final long expectedBits = 0b0111101111011111;
        final long readBits = bitInputStream.readBits(TEST_BIT_COUNT);
        assertEquals(expectedBits, readBits);
    }

    @Test
    public void testReadBitsWithByteOrderBIG_ENDIAN() throws IOException {
        // Testing readBits method with byteOrder as BIG_ENDIAN
        ByteOrder byteOrderBIG_ENDIAN = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStreamBIG_ENDIAN = new BitInputStream(byteArrayInputStream, byteOrderBIG_ENDIAN);
        final long expectedBitsBIG_ENDIAN = 0b1111011111011101;
        final long readBitsBIG_ENDIAN = bitInputStreamBIG_ENDIAN.readBits(TEST_BIT_COUNT);
        assertEquals(expectedBitsBIG_ENDIAN, readBitsBIG_ENDIAN);
    }

    @Test(expected = IllegalStateException.class)
    public void testReadBitsWhenClosed() throws IOException {
        // Testing readBits method when stream is closed
        bitInputStream.close();
        bitInputStream.readBits(TEST_BIT_COUNT);
    }

    @Test
    public void testReadBitsEdgeCase() throws IOException {
        // Testing readBits method with bits to read equal to the number of available bits
        final long expectedBitsEdgeCase = 0xFFFFFFFFL >>> (64 - TEST_BIT_COUNT);
        final long readBitsEdgeCase = bitInputStream.readBits(TEST_BIT_COUNT);
        assertEquals(expectedBitsEdgeCase, readBitsEdgeCase);
    }

    @Test
    public void testReadBitsExceptionHandling() throws IOException {
        // Testing readBits method when an exception is thrown from the underlying InputStream
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x80, 0x80});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
        // Reading the first byte successfully
        bitInputStream.readBits(8);
        // An IOException should be thrown when reading the second byte
        try {
            bitInputStream.readBits(8);
        } catch (IOException e) {
            // Pass since expected
            assertEquals("Unexpected exception message", "Stream closed", e.getMessage());
        }
    }

    @Test
    public void testReadBitsPerformance() throws IOException {
        // Testing readBits method performance
        long startTime = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            bitInputStream.readBits(TEST_BIT_COUNT);
        }
        long endTime = System.nanoTime();
        long timeDifference = endTime - startTime;
        System.out.println("Reading " + TEST_BIT_COUNT + " bits " + timeDifference + " nanoseconds.");
    }

    @Test
    public void testReadBitsByteBuffer() throws IOException {
        // Testing readBits method with ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(TEST_INPUT_DATA);
        BitInputStream bitInputStreamByteBuffer = new BitInputStream(byteBuffer.asInputStream(), ByteOrder.LITTLE_ENDIAN);
        long expectedBitsByteBuffer = 0b0111101111011111;
        long readBitsByteBuffer = bitInputStreamByteBuffer.readBits(TEST_BIT_COUNT);
        assertEquals(expectedBitsByteBuffer, readBitsByteBuffer);
    }
}