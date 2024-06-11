package org.apache.commons.compress.utils;

import static org.junit.Assert.*;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

 private BitInputStream bitInputStream;
 private ByteArrayInputStream inputStream;
 private ByteArrayOutputStream outputStream;

 @Before
 public void setUp() {
 inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04});
 outputStream = new ByteArrayOutputStream();
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 assertNotNull(bitInputStream);
 }

 /**
 * Test close() method
 */
 @Test
 public void testClose() throws IOException {
 bitInputStream.close();
 assertTrue(inputStream.available() == 0);
 }

 /**
 * Test clearBitCache() method
 */
 @Test
 public void testClearBitCache() {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.bitsCached);
 assertEquals(0, bitInputStream.bitsCachedSize);
 }

 /**
 * Test readBits method with positive count
 */
 @Test
 public void testReadBitsPositiveCount() throws IOException {
 // Test reading 1 bit
 assertEquals(1, bitInputStream.readBits(1));
 // Test reading 63 bits
 long expected = 0b11111111111111111111111111111111111111111111111111111111;
 long actual = bitInputStream.readBits(63);
 assertEquals(expected, actual);
 // Test reading less than 63 bits
 long expected2 = 0b11111111;
 long actual2 = bitInputStream.readBits(7);
 assertEquals(expected2, actual2);
 }

 /**
 * Test readBits method with negative count
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsNegativeCount() throws IOException {
 bitInputStream.readBits(-1);
 }

 /**
 * Test readBits method with count greater than MAXIMUM_CACHE_SIZE
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsGreaterThanMaximumCacheSize() throws IOException {
 bitInputStream.readBits(64);
 }

 /**
 * Test readBits method with end of stream
 */
 @Test
 public void testReadBitsEndOfStream() throws IOException {
 // Test reading before end of stream
 long expected = 0b11111111;
 long actual = bitInputStream.readBits(8);
 assertEquals(expected, actual);
 // Test reading after end of stream
 bitInputStream.readBits(1);
 bitInputStream.readBits(1);
 assertEquals(-1, bitInputStream.readBits(1));
 }

 /**
 * Test readBits method with different byte orders
 */
 @Before
 public void setUp2() {
 inputStream = new ByteArrayInputStream(new byte[]{0x04, 0x03, 0x02, 0x01});
 byteOrder = ByteOrder.BIG_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 }

 @Test
 public void testReadBitsDifferentByteOrders() throws IOException {
 // Test reading 1 bit
 assertEquals(1, bitInputStream.readBits(1));
 // Test reading 63 bits
 long expected = 0b0000000111111111111111111111111111111111111111111111111;
 long actual = bitInputStream.readBits(63);
 assertEquals(expected, actual);
 // Test reading less than 63 bits
 long expected2 = 0b00001111;
 long actual2 = bitInputStream.readBits(3);
 assertEquals(expected2, actual2);
 }
}