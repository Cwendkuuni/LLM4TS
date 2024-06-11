package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x12, 0x34, 0x56, 0x78});
        bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
    }
    
    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }
    
    @Test
    public void testReadBits() throws IOException {
        assertEquals(0x123, bitInputStream.readBits(12));
        assertEquals(0x4, bitInputStream.readBits(4));
        assertEquals(0x56, bitInputStream.readBits(8));
        assertEquals(0x7, bitInputStream.readBits(3));
        assertEquals(-1, bitInputStream.readBits(1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_negativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBits_countGreaterThanMaximumCacheSize() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(1));
    }
}