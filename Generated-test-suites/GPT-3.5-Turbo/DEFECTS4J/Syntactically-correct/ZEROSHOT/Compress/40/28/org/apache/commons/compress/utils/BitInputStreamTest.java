package org.apache.commons.compress.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitInputStreamTest {
    
    // Test readBits() method with count less than 63
    @Test
    public void testReadBitsLessThan63() throws IOException {
        byte[] bytes = {0b10101010, 0b01100110, 0b00110011};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bis = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        long result = bis.readBits(10);
        assertEquals(0b1010101001, result);
        bis.close();
    }
    
    // Test readBits() method with count equal to 63
    @Test
    public void testReadBitsEqualTo63() throws IOException {
        byte[] bytes = {0b10101010, 0b01100110, 0b00110011};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bis = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        long result = bis.readBits(63);
        assertEquals(0b1010101001100110001100110001100110011001100110011001100110011, result);
        bis.close();
    }
    
    // Test readBits() method with count greater than 63
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsGreaterThan63() throws IOException {
        byte[] bytes = {0b10101010, 0b01100110, 0b00110011};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bis = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bis.readBits(64);
        bis.close();
    }
    
    // Test readBits() method with count less than 0
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsLessThan0() throws IOException {
        byte[] bytes = {0b10101010, 0b01100110, 0b00110011};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bis = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bis.readBits(-10);
        bis.close();
    }
    
    // Test readBits() method with end of stream before reading the requested number of bits
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        byte[] bytes = {0b10101010, 0b01100110, 0b00110011};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bis = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        long result = bis.readBits(30);
        assertEquals(-1, result);
        bis.close();
    }
    
    // Test clearBitCache() method
    @Test
    public void testClearBitCache() throws IOException {
        byte[] bytes = {0b10101010, 0b01100110, 0b00110011};
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        BitInputStream bis = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
        bis.readBits(10);
        bis.clearBitCache();
        long result = bis.readBits(8);
        assertEquals(0b10101010, result);
        bis.close();
    }
}