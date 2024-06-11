import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {
    private static final int TEST_STREAM_SIZE = 5;
    private ByteArrayInputStream testStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        testStream = new ByteArrayInputStream(new byte[]{(byte) 0b1111_1111, (byte) 0b0111_1111, (byte) 0b1011_1111, (byte) 0b0101_1111, (byte) 0b1010_1111});
        bitInputStream = new BitInputStream(testStream, ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void testReadBits_ValidCount_LittleEndian() throws IOException {
        long expected = 0b1111_1111_0111_1111_1011_1111_0101_1111_1010_1111L;
        long actual = bitInputStream.readBits(63);
        assertEquals(expected, actual);
    }

    @Test
    public void testReadBits_ValidCount_BigEndian() throws IOException {
        byteOrder = ByteOrder.BIG_ENDIAN;
        long expected = 0b1010_1111_0101_1111_0111_1111_1111_1111_1111_1111L;
        long actual = bitInputStream.readBits(63);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_NegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_CountBiggerThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        long expected = 0;
        long actual = bitInputStream.readBits(63);
        assertEquals(expected, actual);
        bitInputStream.clearBitCache();
        actual = bitInputStream.readBits(63);
        assertEquals(expected, actual);
    }
}