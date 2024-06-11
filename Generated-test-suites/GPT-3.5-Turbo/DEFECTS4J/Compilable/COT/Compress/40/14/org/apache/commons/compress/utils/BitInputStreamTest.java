package org.apache.commons.compress.utils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private final InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
    private final InputStream testStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
    
    private BitInputStream emptyBitInputStream;
    private BitInputStream testBitInputStream;
    
    @Before
    public void setUp() {
        emptyBitInputStream = new BitInputStream(emptyStream, ByteOrder.BIG_ENDIAN);
        testBitInputStream = new BitInputStream(testStream, ByteOrder.LITTLE_ENDIAN);
    }
    
    @Test
    public void testClearBitCache() throws IOException {
        testBitInputStream.readBits(16);
        testBitInputStream.clearBitCache();
        assertEquals(0, testBitInputStream.readBits(8));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsNegativeCount() throws IOException {
        emptyBitInputStream.readBits(-1);
        fail("Should throw IllegalArgumentException");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsCountGreaterThanMaximumCacheSize() throws IOException {
        emptyBitInputStream.readBits(64);
        fail("Should throw IllegalArgumentException");
    }
    
    @Test
    public void testReadBitsCountZero() throws IOException {
        assertEquals(0, emptyBitInputStream.readBits(0));
    }
    
    @Test
    public void testReadBitsCountLessThanEight() throws IOException {
        assertEquals(1, testBitInputStream.readBits(1));
    }
    
    @Test
    public void testReadBitsCountEqualsEight() throws IOException {
        assertEquals(1, testBitInputStream.readBits(8));
    }
    
    @Test
    public void testReadBitsCountGreaterThanEight() throws IOException {
        assertEquals(258, testBitInputStream.readBits(9));
    }
    
    @Test
    public void testReadBitsCountEqualToMaximumCacheSize() throws IOException {
        assertEquals(323, testBitInputStream.readBits(32));
    }
    
    @Test
    public void testReadBitsCountLessThanMaximumCacheSize() throws IOException {
        assertEquals(67, testBitInputStream.readBits(15));
    }
    
    @Test
    public void testReadBitsEndOfFile() throws IOException {
        assertEquals(-1, emptyBitInputStream.readBits(8));
    }
}