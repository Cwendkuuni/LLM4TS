
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;

    @Before
    public void setup() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] {0x12, 0x34, 0x56});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStream.readBits(16); // Read 16 bits to populate the cache
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(8));
    }

    @Test
    public void testReadBitsLittleEndian() throws IOException {
        bitInputStream = new BitInputStream(new ByteArrayInputStream(new byte[] {0x12, 0x34, 0x56}), ByteOrder.LITTLE_ENDIAN);
        
        assertEquals(1929, bitInputStream.readBits(12)); // 0x123
        assertEquals(56389, bitInputStream.readBits(16)); // 0x3456
        assertEquals(-1, bitInputStream.readBits(10)); // End of stream
        
        assertNull(bitInputStream.readBits(8));
    }

    @Test
    public void testReadBitsBigEndian() throws IOException {
        assertEquals(18, bitInputStream.readBits(5)); // 0x12
        assertEquals(4, bitInputStream.readBits(9)); // 0x34
        assertEquals(0, bitInputStream.readBits(2)); // 0x56 (truncated)
        
        assertNull(bitInputStream.readBits(16));
    }
}
