package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {

    private BitInputStream bitInputStream;
    private InputStream inputStream;

    @Before
    public void setUp() {
        byte[] testData = new byte[]{0x55, 0x45, 0x37};
        inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        bitInputStream.close();
    }

    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
        // verify that the bit cache is cleared by trying to read a bit
        try {
            long bit = bitInputStream.readBits(1);
            assertEquals(-1, bit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadBits() {
        try {
            long bits = bitInputStream.readBits(8);
            assertEquals(0x55, bits);

            bits = bitInputStream.readBits(4);
            assertEquals(0x5, bits);

            bits = bitInputStream.readBits(6);
            assertEquals(0x45, bits);

            bits = bitInputStream.readBits(2);
            assertEquals(0x1, bits);

            bits = bitInputStream.readBits(8);
            assertEquals(0x37, bits);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
