package org.apache.commons.compress.utils;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {

    private BitInputStream bitInputStreamBigEndian;
    private BitInputStream bitInputStreamLittleEndian;

    @Before
    public void setUp() throws Exception {
        byte[] data = {0x78, 0x56, 0x34, 0x12}; // 0x78563412
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        bitInputStreamBigEndian = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
        bitInputStreamLittleEndian = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }

    @After
    public void tearDown() throws Exception {
        bitInputStreamBigEndian.close();
        bitInputStreamLittleEndian.close();
    }

    @Test
    public void testReadBits_LittleEndian() throws IOException {
        long result = bitInputStreamLittleEndian.readBits(32);
        assertEquals(0x12345678, result);
    }

    @Test
    public void testReadBits_BigEndian() throws IOException {
        long result = bitInputStreamBigEndian.readBits(32);
        assertEquals(0x78563412, result);
    }

    @Test
    public void testReadBits_NegativeCount() throws IOException {
        try {
            bitInputStreamLittleEndian.readBits(-1);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_CountGreaterThanMaximum() throws IOException {
        try {
            bitInputStreamBigEndian.readBits(64);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("count must not be negative or greater than 63", e.getMessage());
        }
    }

    @Test
    public void testReadBits_EndOfStream() throws IOException {
        long result = bitInputStreamBigEndian.readBits(64);
        assertEquals(-1, result);
    }

    @Test
    public void testClearBitCache() throws IOException {
        bitInputStreamBigEndian.clearBitCache();
        long result = bitInputStreamBigEndian.readBits(32);
        assertEquals(0, result);
    }
}