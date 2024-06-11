
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    private InputStream inputStream;

    @Before
    public void setUp() {
        byte[] data = new byte[] { (byte) 0b10101010, (byte) 0b01010101 };
        inputStream = new ByteArrayInputStream(data);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStream.close();
        inputStream.close();
    }

    @Test
    public void testReadBits() throws Exception {
        assertEquals(0b1010101001010101L, bitInputStream.readBits(16));
        assertEquals(0b10101010010101L, bitInputStream.readBits(14));
        assertEquals(0b1010101001L, bitInputStream.readBits(10));
        assertEquals(0b10101L, bitInputStream.readBits(5));
        assertEquals(0b0, bitInputStream.readBits(1));
        assertEquals(-1, bitInputStream.readBits(16));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws Exception {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithLargeCount() throws Exception {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws Exception {
        bitInputStream.readBits(8);
        bitInputStream.clearBitCache();

        assertEquals(0b1010101001010101L, bitInputStream.readBits(16));
    }
    
    @Test
    public void testClose() throws Exception {
        bitInputStream.close();
        assertNull(bitInputStream.readBits(16));
    }
}
