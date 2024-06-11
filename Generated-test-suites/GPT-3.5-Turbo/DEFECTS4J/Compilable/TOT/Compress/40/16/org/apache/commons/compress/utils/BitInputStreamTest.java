
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BitInputStreamTest {
	
	private BitInputStream bitInputStream;
	
	@Before
	public void setUp() {
		InputStream inputStream = new ByteArrayInputStream(new byte[] { (byte) 0xAA, (byte) 0xBB });
		bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
	}
	
	@After
	public void tearDown() throws IOException {
		bitInputStream.close();
	}
	
	@Test
	public void testReadBits() throws IOException {
		long result = bitInputStream.readBits(8);
		assertEquals(0xAA, result);
		result = bitInputStream.readBits(8);
		assertEquals(0xBB, result);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReadBitsWithNegativeCount() throws IOException {
		bitInputStream.readBits(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testReadBitsWithLargeCount() throws IOException {
		bitInputStream.readBits(100);
	}
	
	@Test
	public void testReadBitsWithEndOfStream() throws IOException {
		ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0]);
		BitInputStream emptyBitInputStream = new BitInputStream(emptyStream, ByteOrder.BIG_ENDIAN);
		long result = emptyBitInputStream.readBits(8);
		assertEquals(-1, result);
	}
	
	@Test
	public void testClearBitCache() throws IOException {
		bitInputStream.readBits(4);
		bitInputStream.clearBitCache();
		long result = bitInputStream.readBits(8);
		assertEquals(0xAA, result);
	}
}
