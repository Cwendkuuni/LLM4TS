package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import static org.junit.Assert.assertEquals;

public class BitInputStreamTest {
    private BitInputStream bitInputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        byteArrayInputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03});
        bitInputStream = new BitInputStream(byteArrayInputStream, ByteOrder.BIG_ENDIAN);
    }

    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsOutOfRange() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testReadBits() throws IOException {
        assertEquals(0x010203L, bitInputStream.readBits(24));
    }

}
