import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x5A, (byte) 0xA5, (byte) 0xAA });
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testReadBits_PositiveCount_ReturnsBitsConcatenatedAsLong() throws IOException {
        long result = bitInputStream.readBits(18);
        assertEquals(0x5AA, result);
    }

    @Test
    public void testReadBits_NegativeCount_ThrowsIllegalArgumentException() {
        try {
            bitInputStream.readBits(-5);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("count must not be negative"));
        } catch (IOException e) {
            assertFalse("IOException not expected for this test case", true);
        }
    }

    @Test
    public void testReadBits_CountBiggerThanMaximumCacheSize_ThrowsIllegalArgumentException() {
        try {
            bitInputStream.readBits(70);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("count must not be greater than 63"));
        } catch (IOException e) {
            assertFalse("IOException not expected for this test case", true);
        }
    }

    @Test
    public void testReadBits_EndOfStreamBeforeReadingRequestedBits_ReturnsMinusOne() throws IOException {
        long result = bitInputStream.readBits(65);
        assertEquals(-1, result);
    }

    @Test
    public void testReadBits_LittleEndian_ReturnsBitsConcatenatedAsLong() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x5A, (byte) 0xA5, (byte) 0xAA });
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(18);
        assertEquals(0xAA5, result);
    }

    @Test
    public void testClearBitCache_ResetsBitCache() throws IOException {
        bitInputStream.readBits(10);
        bitInputStream.clearBitCache();

        int bitsCachedSize = bitInputStream.getBitsCachedSize();
        assertEquals(0, bitsCachedSize);
    }

    @Test
    public void testReadBits_EdgeCase_CountEqualToMaximumCacheSize_ReturnsBitsConcatenatedAsLong() throws IOException {
        long result = bitInputStream.readBits(63);
        assertEquals(0x5AA5A, result);
    }

    @Test
    public void testReadBits_EdgeCase_CountEqualToMaximumCacheSize_BigEndian_ReturnsBitsConcatenatedAsLong() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x5A, (byte) 0xA5, (byte) 0xAA });
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);

        long result = bitInputStream.readBits(63);
        assertEquals(0x5AA5A0000000000L, result);
    }
    
    @Test
    public void testReadBits_EdgeCase_CountEqualToMaximumCacheSize_LittleEndian_ReturnsBitsConcatenatedAsLong() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x5A, (byte) 0xA5, (byte) 0xAA });
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);

        long result = bitInputStream.readBits(63);
        assertEquals(0xAA5A50000000000L, result);
    }

}