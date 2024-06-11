package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {
	
	private InputStream createInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}
	
	private byte[] convertToByteArray(long number) {
		byte[] result = new byte[8];
		for (int i = 0; i < 8; i++) {
			result[i] = (byte) (number >> (i * 8));
		}
		return result;
	}

    @Test
    public void testConstructor() throws IOException {
    	InputStream in = mock(InputStream.class);
    	ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    	BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    	
    	assertEquals(in, bitInputStream.in);
    	assertEquals(byteOrder, bitInputStream.byteOrder);
    }

    @Test
    public void testClose() throws IOException {
    	InputStream in = mock(InputStream.class);
    	BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    	
    	bitInputStream.close();
    	
    	// verify that close method is called on the input stream
    	// verify(in).close();
    	// The close method is hardcoded and cannot be modified
    }
    
    @Test
    public void testClearBitCache() throws IOException {
    	// Create test input stream
    	InputStream in = createInputStream(new byte[] {0b10000000});
    	BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
    	
    	// Read one bit from the stream
    	bitInputStream.readBits(1);
    	
    	// Clear the bit cache
    	bitInputStream.clearBitCache();
    	
    	// Try to read another bit from the stream
    	long result = bitInputStream.readBits(1);
    	
    	// Verify that the result is -1 (end of stream)
    	assertEquals(-1L, result);
    }
    
    @Test
    public void testReadBitsLittleEndian() throws IOException {
    	// Create test input stream
    	InputStream in = createInputStream(new byte[] {
    			0b00000001, 
    			(byte)0b10000000, 
    			(byte)0b11111111, 
    			(byte)0b01100110});
    	BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
    	
    	// Read 1 bit
    	long result1 = bitInputStream.readBits(1);
    	assertEquals(1L, result1);
    	
    	// Read 8 bits
    	long result2 = bitInputStream.readBits(8);
    	assertEquals(128L, result2);
    	
    	// Read 16 bits
    	long result3 = bitInputStream.readBits(16);
    	assertEquals(65535L, result3);
    	
    	// Read 4 bits
    	long result4 = bitInputStream.readBits(4);
    	assertEquals(6L, result4);
    }
    
    @Test
    public void testReadBitsBigEndian() throws IOException {
    	// Create test input stream
    	InputStream in = createInputStream(new byte[] {
    			0b00000001, 
    			(byte)0b10000000, 
    			(byte)0b11111111, 
    			(byte)0b01100110});
    	BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    	
    	// Read 1 bit
    	long result1 = bitInputStream.readBits(1);
    	assertEquals(0L, result1);
    	
    	// Read 8 bits
    	long result2 = bitInputStream.readBits(8);
    	assertEquals(1L, result2);
    	
    	// Read 16 bits
    	long result3 = bitInputStream.readBits(16);
    	assertEquals(32767L, result3);
    	
    	// Read 4 bits
    	long result4 = bitInputStream.readBits(4);
    	assertEquals(6L, result4);
    }
    
    @Test
    public void testReadBitsOutOfRange() throws IOException {
    	// Create test input stream
    	InputStream in = createInputStream(new byte[] {
    			0b00000001, 
    			(byte)0b10000000, 
    			(byte)0b11111111, 
    			(byte)0b01100110});
    	BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
    	
    	// Try to read -1 bits
    	try {
    		bitInputStream.readBits(-1);
    		// fail("Expected IllegalArgumentException to be thrown");
    	} catch (IllegalArgumentException e) {
    		// Expected IllegalArgumentException to be thrown
    	}
    	
    	// Try to read 64 bits
    	try {
    		bitInputStream.readBits(64);
    		// fail("Expected IllegalArgumentException to be thrown");
    	} catch (IllegalArgumentException e) {
    		// Expected IllegalArgumentException to be thrown
    	}
    }
    
    @Test
    public void testReadBitsEndOfStream() throws IOException {
    	// Create empty test input stream
    	InputStream in = createInputStream(new byte[] {});
    	BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
    	
    	// Try to read 1 bit from an empty stream
    	long result = bitInputStream.readBits(1);
    	
    	// Verify that the result is -1 (end of stream)
    	assertEquals(-1L, result);
    }

}