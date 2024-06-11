import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private static final byte[] TEST_DATA = new byte[]{(byte) 0x01, (byte) 0x42, (byte) 0x7F};
    private BitInputStream bitInputStream;

    @Before
    public void setup() throws Exception {
        bitInputStream = new BitInputStream(new java.io.ByteArrayInputStream(TEST_DATA), ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits_NormalCase() throws Exception {
        // Test reading 1 bit
        Assert.assertEquals(1L, bitInputStream.readBits(1));

        // Test reading 6 bits
        final long expectedValue = 42L | (1L << 6);
        Assert.assertEquals(expectedValue, bitInputStream.readBits(6));

        // Test reading 7 bits from the last byte
        final long expectedValue7bitsLastByte = 128L | 0x7F;
        Assert.assertEquals(expectedValue7bitsLastByte, bitInputStream.readBits(7));

        // Test reading 8 bits, should read next byte
        final long expectedValue8bits = 0x01020304L;
        Assert.assertEquals(expectedValue8bits, bitInputStream.readBits(8));
    }

    @Test(expected = IOException.class)
    public void testReadBits_IOError() throws Exception {
        final java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(TEST_DATA);
        final BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.close();
        bitInputStream.readBits(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_NegativeCount() throws Exception {
        final BitInputStream bitInputStream = new BitInputStream(new java.io.ByteArrayInputStream(TEST_DATA.clone()), ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_CountGreaterThanMaximum() throws Exception {
        final BitInputStream bitInputStream = new BitInputStream(new java.io.ByteArrayInputStream(TEST_DATA.clone()), ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(BitInputStream.MAXIMUM_CACHE_SIZE + 1);
    }
}