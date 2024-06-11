import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = new byte[]{(byte) 0b1111_1111_1111_1111, (byte) 0b0110_1110_1110_1101};
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testBitInputStreamCreation() {
        assertNotNull(bitInputStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBitInputStreamCreationWithInvalidByteOrder() {
        new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximum() {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        long expected = 0b1111_1111_1111_1111_0110_1110_1110_1101;
        long actual = bitInputStream.readBits(14);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBitsWithValidCountAtEndOfStream() throws IOException {
        long actual = bitInputStream.readBits(14);
        assertNull(actual);
    }

    @Test
    public void testClearBitCache() throws IOException {
        long expected = 0b1111_1111_1111_1111_0110_1110_1110_1101;
        long actual = bitInputStream.readBits(14);
        assertEquals(expected, actual);
        bitInputStream.clearBitCache();
        long actual2 = bitInputStream.readBits(1);
        assertFalse(actual2 >= 0);
    }

    @Test
    public void testReadBitsWithValidCountAndDifferentByteOrders() throws IOException {
        ByteOrder bigEndian = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStreamBigEndian = new BitInputStream(inputStream, bigEndian);
        long expectedBigEndian = 0b0110_1110_1110_1101_1111_1111_1111_1111;
        long actualBigEndian = bitInputStreamBigEndian.readBits(14);
        assertEquals(expectedBigEndian, actualBigEndian);

        ByteOrder littleEndian = ByteOrder.LITTLE_ENDIAN;
        BitInputStream bitInputStreamLittleEndian = new BitInputStream(inputStream, littleEndian);
        long expectedLittleEndian = 0b1111_1111_1111_1111_0110_1110_1110_1101;
        long actualLittleEndian = bitInputStreamLittleEndian.readBits(14);
        assertEquals(expectedLittleEndian, actualLittleEndian);

        assertEquals(expectedLittleEndian, expectedBigEndian ^ 0xFFFFFFFFL);
    }
}