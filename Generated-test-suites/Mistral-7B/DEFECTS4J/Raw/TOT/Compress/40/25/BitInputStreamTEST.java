package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;

import org.junit.Test;

/**
 * Test class for BitInputStream.
 */
public class BitInputStreamTest {

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}
     * with a valid InputStream and ByteOrder.
     * @throws IOException
     */
    @Test
    public void testBitInputStreamWithValidInputStreamAndByteOrder() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110101});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        final long bits1 = bitInputStream.readBits(3);
        final long bits2 = bitInputStream.readBits(3);
        final long bits3 = bitInputStream.readBits(2);

        final long expectedBits1 = 0b111 << 3;
        final long expectedBits2 = 0b010;
        final long expectedBits3 = 0b11;

        final boolean test1 = (bits1 == expectedBits1);
        final boolean test2 = (bits2 == expectedBits2);
        final boolean test3 = (bits3 == expectedBits3);

        boolean result = test1 && test2 && test3;

        if (!result) {
            fail("Test failed.");
        }
    }

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}
     * with a null InputStream.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamWithNullInputStream() {
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        new BitInputStream(null, byteOrder);
    }

    /**
     * Test method for {@link BitInputStream#BitInputStream(InputStream, ByteOrder)}
     * with a null ByteOrder.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamWithNullByteOrder() {
        final InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110101});
        new BitInputStream(input, null);
    }

    /**
     * Test method for {@link BitInputStream#close()}
     */
    @Test
    public void testClose() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110101});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.close();

        try {
            final long bits = bitInputStream.readBits(1);
        } catch (IOException e) {
            // expected
        }
    }

    /**
     * Test method for {@link BitInputStream#clearBitCache()}
     */
    @Test
    public void testClearBitCache() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110101});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();

        final long bits = bitInputStream.readBits(1);

        final boolean result = (bits == 0);

        boolean expectedResult = true;

        if (result != expectedResult) {
            fail("Test failed.");
        }
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with a negative count.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110101});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.readBits(-1);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with a count greater than 63.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThan63() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{(byte) 0b11110101});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        bitInputStream.readBits(64);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with the end of the underlying stream reached.
     */
    @Test
    public void testReadBitsWithEndOfUnderlyingStreamReached() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        final long bits = bitInputStream.readBits(1);

        final boolean result = (bits < 0);

        boolean expectedResult = true;

        if (result != expectedResult) {
            fail("Test failed.");
        }
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)} with valid inputs.
     */
    @Test
    public void testReadBitsWithValidInputs() throws IOException {
        final InputStream input = new ByteArrayInputStream(new byte[]{0x42, 0x33, 0x22, 0x11});
        final ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        final BitInputStream bitInputStream = new BitInputStream(input, byteOrder);

        final long bits1 = bitInputStream.readBits(3);
        final long bits2 = bitInputStream.readBits(4);
        final long bits3 = bitInputStream.readBits(5);

        final long expectedBits1 = 0b111 << 3;
        final long expectedBits2 = 0b0101_0000;
        final long expectedBits3 = 0b11111;

        final boolean test1 = (bits1 == expectedBits1);
        final boolean test2 = (bits2 == expectedBits2);
        final boolean test3 = (bits3 == expectedBits3);

        boolean result = test1 && test2 && test3;

        if (!result) {
            fail("Test failed.");
        }
    }
}