import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        byte[] byteArray = {0x6F, (byte) 0x84, (byte) 0xCD, (byte) 0x9A, (byte) 0xB8};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testReadBits_typicalCase() throws IOException {
        long result = bitInputStream.readBits(16);
        assertEquals(0x6F84, result);
    }

    @Test
    public void testReadBits_edgeCase_countIsZero() throws IOException {
        long result = bitInputStream.readBits(0);
        assertEquals(0, result);
    }

    @Test
    public void testReadBits_edgeCase_countEqualsMaximumCacheSize() throws IOException {
        long result = bitInputStream.readBits(BitInputStream.MAXIMUM_CACHE_SIZE);
        assertEquals(0x6F84CD9AB8L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_errorScenario_countIsNegative() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_errorScenario_countExceedsMaximumCacheSize() throws IOException {
        bitInputStream.readBits(BitInputStream.MAXIMUM_CACHE_SIZE + 1);
    }

    @Test
    public void testClearBitCache_typicalCase() throws IOException {
        bitInputStream.readBits(16);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(0x84, result);
    }

    @Test
    public void testClearBitCache_edgeCase_BitCacheFullyUsed() throws IOException {
        bitInputStream.readBits(40);
        bitInputStream.clearBitCache();
        long result = bitInputStream.readBits(8);
        assertEquals(0x9A, result);
    }

}