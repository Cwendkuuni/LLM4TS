
package org.apache.commons.compress.utils;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.io.IOException;

public class BitInputStreamTest {

    @Test
    public void testReadBits() {
        InputStream in = null;
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
        
        // Test when count is 0
        try {
            assertEquals(-1, bitInputStream.readBits(0));
        } catch (IOException e) {
            fail("Exception thrown");
        }

        // Test when count is less than 0
        try {
            bitInputStream.readBits(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test when count is greater than 63
        try {
            bitInputStream.readBits(64);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }

        // Test when count is between 1 and 63
        try {
            // Test when byteOrder is BIG_ENDIAN
            byteOrder = ByteOrder.BIG_ENDIAN;
            bitInputStream = new BitInputStream(in, byteOrder);
            
            // Test when bitsCached is less than count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 5;
            bitInputStream.bitsCachedSize = 3;
            try {
                assertEquals(5, bitInputStream.readBits(4));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(3, bitInputStream.bitsCachedSize);

            // Test when bitsCached is equal to count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 14;
            bitInputStream.bitsCachedSize = 4;
            try {
                assertEquals(14, bitInputStream.readBits(4));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(0, bitInputStream.bitsCachedSize);

            // Test when bitsCached is greater than count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 27;
            bitInputStream.bitsCachedSize = 6;
            try {
                assertEquals(27, bitInputStream.readBits(4));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(2, bitInputStream.bitsCachedSize);

            // Test when bitsCachedSize is greater than count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 125;
            bitInputStream.bitsCachedSize = 8;
            try {
                assertEquals(5, bitInputStream.readBits(3));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(5, bitInputStream.bitsCachedSize);

            // Test when bitsCachedSize is equal to count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 77;
            bitInputStream.bitsCachedSize = 7;
            try {
                assertEquals(77, bitInputStream.readBits(7));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(0, bitInputStream.bitsCachedSize);

            // Test when bitsCachedSize is less than count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 255;
            bitInputStream.bitsCachedSize = 8;
            try {
                assertEquals(15, bitInputStream.readBits(10));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(6, bitInputStream.bitsCachedSize);

            // Test when count is zero
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 18;
            bitInputStream.bitsCachedSize = 8;
            try {
                assertEquals(0, bitInputStream.readBits(0));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(8, bitInputStream.bitsCachedSize);

            // Test when bitsCachedSize is greater than count
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 54;
            bitInputStream.bitsCachedSize = 8;
            try {
                assertEquals(54, bitInputStream.readBits(7));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(1, bitInputStream.bitsCachedSize);
            
            // Test when byteOrder is LITTLE_ENDIAN
            byteOrder = ByteOrder.LITTLE_ENDIAN;
            bitInputStream = new BitInputStream(in, byteOrder);
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 255;
            bitInputStream.bitsCachedSize = 8;
            try {
                assertEquals(7, bitInputStream.readBits(3));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(5, bitInputStream.bitsCachedSize);
            
            // Test when byteOrder is null
            byteOrder = null;
            bitInputStream = new BitInputStream(in, byteOrder);
            bitInputStream.clearBitCache();
            bitInputStream.bitsCached = 67;
            bitInputStream.bitsCachedSize = 6;
            try {
                assertEquals(67, bitInputStream.readBits(6));
            } catch (IOException e) {
                fail("Exception thrown");
            }
            assertEquals(0, bitInputStream.bitsCachedSize);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
}
