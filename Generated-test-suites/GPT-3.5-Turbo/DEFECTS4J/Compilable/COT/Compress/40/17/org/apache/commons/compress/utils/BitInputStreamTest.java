package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws Exception {
        byte[] data = {0x01, 0x23, 0x45, 0x67, (byte) 0x89, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
        inputStream = new ByteArrayInputStream(data);
    }

    @After
    public void tearDown() throws Exception {
        inputStream.close();
    }

    @Test
    public void testClearBitCache() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        bitInputStream.readBits(20);
        bitInputStream.clearBitCache();
        assertEquals(0, bitInputStream.readBits(4));
    }

    @Test
    public void testReadBits() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals(1, bitInputStream.readBits(4));
        assertEquals(2, bitInputStream.readBits(4));
        assertEquals(3, bitInputStream.readBits(5));
        assertEquals(4, bitInputStream.readBits(6));
        assertEquals(5, bitInputStream.readBits(8));
    }

    @Test
    public void testReadBitsLittleEndian() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
        assertEquals(1, bitInputStream.readBits(4));
        assertEquals(2, bitInputStream.readBits(4));
        assertEquals(3, bitInputStream.readBits(5));
        assertEquals(4, bitInputStream.readBits(6));
        assertEquals(5, bitInputStream.readBits(8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaxCacheSize() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBitsEndOfFile() throws IOException {
        BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        assertEquals(-1, bitInputStream.readBits(64));
    }

}
