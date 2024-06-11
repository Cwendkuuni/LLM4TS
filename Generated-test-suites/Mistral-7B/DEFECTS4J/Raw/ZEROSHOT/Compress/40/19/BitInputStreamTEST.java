package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

/**
 * Test class for {@link BitInputStream}
 */
public class BitInputStreamTest {

    private InputStream inputStream;
    private BitInputStream bitInputStream;

    @Before
    public void setUp() throws IOException {
        String testData = "0110110101101101";
        inputStream = new ByteArrayInputStream(testData.getBytes());
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws IOException {
        if (bitInputStream != null) {
            bitInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Test
    public void testReadBitsWithValidCount() throws IOException {
        for (int i = 1; i <= 63; i++) {
            long expected = (long) Math.pow(2, i - 1);
            long actual = bitInputStream.readBits(i);
            assertEquals("Test for reading " + i + " bits", expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
        bitInputStream.readBits(64);
    }

    @Test
    public void testClearBitCache() throws IOException {
        long firstRead = bitInputStream.readBits(1);
        bitInputStream.clearBitCache();
        long secondRead = bitInputStream.readBits(1);
        assertFalse("Test for cleared cache being equal to previous read value", firstRead == secondRead);
    }

    @Test
    public void testReadBitsFromEndOfStream() throws IOException {
        bitInputStream.readBits(1);
        bitInputStream.readBits(1);
        long actual = bitInputStream.readBits(1);
        long expected = -1;
        assertEquals("Test for reading from end of stream", expected, actual);
    }
}