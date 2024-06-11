package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.utils.BitInputStream;
import org.apache.commons.compress.utils.ByteOrder;
import org.junit.Test;

/**
 * Test cases for BitInputStream class
 */
public class BitInputStreamTest {

 private InputStream testInputStream;
 private BitInputStream bitInputStream;

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Before
 public void setUp() throws Exception {
 testInputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
 bitInputStream = new BitInputStream(testInputStream, ByteOrder.LITTLE_ENDIAN);
 }

 @Test
 public void testConstructor() {
 InputStream inputStream = new ByteArrayInputStream(new byte[] {});
 BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
 assertTrue(bitInputStream instanceof BitInputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeCount() {
 BitInputStream bitInputStream = new BitInputStream(testInputStream, ByteOrder.LITTLE_ENDIAN);
 bitInputStream.readBits(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidCount() {
 BitInputStream bitInputStream = new BitInputStream(testInputStream, ByteOrder.LITTLE_ENDIAN);
 bitInputStream.readBits(64);
 }

 @Test
 public void testReadBitsWithSmallCount() throws IOException {
 long expected = 0b01111111;
 long actual = bitInputStream.readBits(7);
 assertEquals(expected, actual);
 }

 @Test
 public void testReadBitsWithLargeCount() throws IOException {
 long expected = 0b01111111_00110011_11111000;
 long actual = bitInputStream.readBits(15);
 assertEquals(expected, actual);
 }

 @Test
 public void testReadBitsFromEndOfStream() throws IOException {
 long expected = -1;
 long actual = bitInputStream.readBits(5);
 assertEquals(expected, actual);
 }

 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 long actual = bitInputStream.readBits(1);
 assertEquals(0, actual);
 }

 @Test
 public void testReadBitsWithEndianSwap() throws IOException {
 BitInputStream bitInputStreamBigEndian = new BitInputStream(testInputStream, ByteOrder.BIG_ENDIAN);
 long expected = 0b11110000_00111100;
 long actual = bitInputStreamBigEndian.readBits(8);
 assertEquals(expected, actual);
 }
}