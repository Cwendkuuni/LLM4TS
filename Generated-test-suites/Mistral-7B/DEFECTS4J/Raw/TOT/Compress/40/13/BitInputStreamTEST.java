import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for {@link BitInputStream}.
 */
public class BitInputStreamTest {

    private static final byte[] TEST_INPUT = new byte[]{(byte) 0x1a, (byte) 0x2b, (byte) 0x3c, (byte) 0x4d};
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(TEST_INPUT);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     * <p>
     * Test reading the correct number of bits.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadBitsCorrectNumber() throws IOException {
        final long bits = bitInputStream.readBits(8);
        final long expected = 0b11111111;
        Assert.assertEquals("Unexpected bits read", expected, bits);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     * <p>
     * Test reading less bits than available.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadBitsLessThanAvailable() throws IOException {
        final long bits = bitInputStream.readBits(5);
        final long expected = 0b11111;
        Assert.assertEquals("Unexpected bits read", expected, bits);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     * <p>
     * Test reading more bits than available.
     * @throws IOException if an I/O error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsMoreThanAvailable() throws IOException {
        bitInputStream.readBits(81);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     * <p>
     * Test reading bits from an empty stream.
     * @throws IOException if an I/O error occurs
     */
    @Test(expected = IOException.class)
    public void testReadBitsFromEmptyStream() throws IOException {
        final BitInputStream bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[0]), ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(1);
    }

    /**
     * Test method for {@link BitInputStream#clearBitCache()}.
     * <p>
     * Test clearing the cache after reading bits.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testClearBitCache() throws IOException {
        final long bits = bitInputStream.readBits(8);
        bitInputStream.clearBitCache();
        final long emptyBits = bitInputStream.readBits(8);
        Assert.assertEquals("Unexpected bits read after clearing cache", 0, emptyBits);
    }

    /**
     * Test method for {@link BitInputStream#readBits(int)}.
     * <p>
     * Test reading bits with different byte orders.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadBitsDifferentByteOrders() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45, 0x67});
        BitInputStream bitInputStreamBigEndian = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);

        final long bigEndianBits = bitInputStreamBigEndian.readBits(16);
        final long littleEndianBits = bitInputStreamLittleEndian.readBits(16);

        final long expectedBigEndian = 0b1111_0101_1111_0101;
        final long expectedLittleEndian = 0b0101_0111_1010_1011;

        Assert.assertEquals("Unexpected bits read with big endian byte order", expectedBigEndian, bigEndianBits);
        Assert.assertEquals("Unexpected bits read with little endian byte order", expectedLittleEndian, littleEndianBits);
    }
}