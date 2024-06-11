
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
    
    private BitInputStream bitInputStream;

    @Before
    public void setUp() {
        // Prepare test data
        byte[] testData = { 0b01010101, 0b11001100, 0b10101010 };
        InputStream inputStream = new ByteArrayInputStream(testData);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }
    
    @After
    public void tearDown() {
        // Close the input stream
        try {
            bitInputStream.close();
        } catch (IOException e) {
            // Handle IOException
        }
    }

    @Test
    public void testReadBits() {
        try {
            assertEquals(0b0101_0101_1100_1100_1010_1010L, bitInputStream.readBits(48));
            assertEquals(0b0101_01L, bitInputStream.readBits(6));
            assertEquals(0b01L, bitInputStream.readBits(2));
            assertEquals(-1L, bitInputStream.readBits(16)); // End of stream reached
        } catch (IOException e) {
            // Handle IOException
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithNegativeCount() throws IOException {
        bitInputStream.readBits(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsWithExceedingCount() throws IOException {
        bitInputStream.readBits(64);
    }
    
    @Test
    public void testClearBitCache() {
        try {
            bitInputStream.readBits(6); // Fill the bit cache
            bitInputStream.clearBitCache();
            assertEquals(0, bitInputStream.readBits(1));
        } catch (IOException e) {
            // Handle IOException
        }
    }
}
