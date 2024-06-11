package org.apache.commons.compress.utils;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.apache.commons.compress.utils.ByteOrder.BIG_ENDIAN;
import static org.apache.commons.compress.utils.ByteOrder.LITTLE_ENDIAN;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

 private BitInputStream bitInputStream;
 private ByteArrayInputStream inputStream;

 @Before
 public void setup() {
 inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x23, 0x45});
 bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
 }

 @Test
 public void testConstructor() {
 // Test constructor with valid arguments
 Assert.assertNotNull(bitInputStream);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorInvalidByteOrder() {
 // Test constructor with invalid byte order
 BitInputStream bitInputStream = new BitInputStream(inputStream, ByteOrder.CUSTOM);
 }

 @Test
 public void testClose() throws IOException {
 // Test close method
 bitInputStream.close();
 Assert.assertFalse(inputStream.isOpen());
 }

 @Test
 public void testClearBitCache() throws IOException {
 // Test clearBitCache method
 bitInputStream.clearBitCache();
 Assert.assertEquals(0, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBits() throws IOException {
 // Test readBits method with valid arguments
 Assert.assertEquals(0x23, bitInputStream.readBits(5));
 Assert.assertEquals(0x45, bitInputStream.readBits(8));
 Assert.assertEquals(-1, bitInputStream.readBits(9));
 // Test readBits method with empty cache
 bitInputStream.clearBitCache();
 Assert.assertEquals(0x01, bitInputStream.readBits(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsInvalidCount() throws IOException {
 // Test readBits method with invalid count
 bitInputStream.readBits(-1);
 }

 @Test(expected = IOException.class)
 public void testReadBitsEndOfStream() throws IOException {
 // Test readBits method when reaching end of stream
 inputStream.readAllBytes();
 inputStream.close();
 bitInputStream.readBits(1);
 }

 @Test
 public void testReadBitsByteOrderLittleEndian() throws IOException {
 // Test readBits method with little endian byte order
 bitInputStream = new BitInputStream(inputStream, LITTLE_ENDIAN);
 Assert.assertEquals(0x23, bitInputStream.readBits(5));
 Assert.assertEquals(0x45, bitInputStream.readBits(8));
 Assert.assertEquals(-1, bitInputStream.readBits(9));
 // Test readBits method with empty cache
 bitInputStream.clearBitCache();
 Assert.assertEquals(0x01, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBitsByteOrderBigEndian() throws IOException {
 // Test readBits method with big endian byte order
 bitInputStream = new BitInputStream(inputStream, BIG_ENDIAN);
 Assert.assertEquals(0x01, bitInputStream.readBits(1));
 Assert.assertEquals(0x23, bitInputStream.readBits(5));
 Assert.assertEquals(0x45, bitInputStream.readBits(8));
 // Test readBits method with empty cache
 bitInputStream.clearBitCache();
 Assert.assertEquals(0x45, bitInputStream.readBits(1));
 }
}