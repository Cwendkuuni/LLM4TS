package org.apache.commons.compress.utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import static org.junit.Assert.*;

public class BitInputStreamTest {

    private static final byte[] BYTE_ARRAY = new byte[]{0x75, 0x61, 0x66, 0x61};
    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        bitInputStream = new BitInputStream(new ByteArrayInputStream(BYTE_ARRAY), ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    // Test case for clearBitCache()
    @Test
    public void testClearBitCache() throws IOException {
        // Reading some bits from bitInputStream to fill the bit cache
        bitInputStream.readBits(5);
        // Clear the bit cache
        bitInputStream.clearBitCache();
        // Make sure that the bit cache is cleared
        assertEquals(0, bitInputStream.readBits(4));
    }

    // Test case for readBits() - typical use case
    @Test
    public void testReadBits_typical() throws IOException {
        // Request reading 16 bits
        long result = bitInputStream.readBits(16);
        // The expected result should be 0x7561
        assertEquals(0x7561, result);
    }

    // Test case for readBits() - edge case
    @Test
    public void testReadBits_edge() throws IOException {
        // Request reading 8 bits as the maximum cache size is 63 bits
        long result = bitInputStream.readBits(8);
        // The expected result should be 0x75
        assertEquals(0x75, result);
    }

    // Test case for readBits() - error scenario
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_invalidInput() throws IOException {
        // Request reading -1 bits
        bitInputStream.readBits(-1);
    }

    // Test case for readBits() - error scenario
    @Test
    public void testReadBits_endOfStream() throws IOException {
        // Request reading 32 bits, which is larger than the available bits in BYTE_ARRAY
        long result = bitInputStream.readBits(32);
        // The expected result should be -1 indicating end of stream
        assertEquals(-1, result);
    }

}