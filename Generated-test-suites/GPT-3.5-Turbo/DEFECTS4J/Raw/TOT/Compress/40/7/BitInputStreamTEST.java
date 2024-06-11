import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{0b00000000, 0b11111111});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
        byteArrayInputStream.close();
    }

    @Test
    public void testConstructor() {
        assertNotNull(bitInputStream);
    }

    @Test
    public void testClearBitCache() throws IOException {
        // Fill the cache
        bitInputStream.readBits(10);
        bitInputStream.clearBitCache();
        assertEquals(0L, bitInputStream.readBits(10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countGreaterThanMaxCacheSize() throws IOException {
        bitInputStream.readBits(bitInputStream.MAXIMUM_CACHE_SIZE + 1);
    }

    @Test
    public void testReadBits_positiveCount() throws IOException {
        assertEquals(0L, bitInputStream.readBits(0));
        assertEquals(0L, bitInputStream.readBits(0));

        assertEquals(0b00000000L, bitInputStream.readBits(8));
        assertEquals(0b11111111L, bitInputStream.readBits(8));
        assertEquals(-1L, bitInputStream.readBits(8));

        assertEquals(0b00001111L, bitInputStream.readBits(4));
        assertEquals(0b00001111L, bitInputStream.readBits(15));
        assertEquals(0b00001111L, bitInputStream.readBits(16));

        assertEquals(0b0000000000001111L, bitInputStream.readBits(16));
        assertEquals(0b00000000000011111111L, bitInputStream.readBits(24));
        assertEquals(0b00000000000011111111L, bitInputStream.readBits(25));

        assertEquals(-1L, bitInputStream.readBits(1));
    }

    @Test
    public void testReadBits_LittleEndian() throws IOException {
        BitInputStream littleEndianBitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(0b0000000011111111L, littleEndianBitInputStream.readBits(16));
        littleEndianBitInputStream.close();
    }

}