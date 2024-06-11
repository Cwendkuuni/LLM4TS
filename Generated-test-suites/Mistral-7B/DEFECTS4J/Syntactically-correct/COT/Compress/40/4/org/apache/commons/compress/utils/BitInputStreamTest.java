package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitInputStreamTest {

 private InputStream inputStream;
 private ByteOrder byteOrder;
 private BitInputStream bitInputStream;

 @SuppressWarnings("ResultOfMethodCallIgnored")
 @Before
 public void setUp() throws IOException {
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 inputStream = new ByteArrayInputStream(new byte[]{(byte) 0b11111111, (byte) 0b01111111, (byte) 0b00111111, (byte) 0b00011111, (byte) 0b00001111, (byte) 0b00000111, (byte) 0b00000011, (byte) 0b00000001});
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 }

 @Test
 public void testBitInputStreamConstructor() {
 BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
 assertNotNull(bitInputStream);
 }

 @Test
 public void testClose() throws IOException {
 bitInputStream.close();
 assertFalse(inputStream.isOpen());
 }

 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.bitsCached);
 assertEquals(0, bitInputStream.bitsCachedSize);
 }

 @Test
 public void testReadBitsNegativeCount() throws IOException {
 try {
 bitInputStream.readBits(-1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testReadBitsZeroCount() throws IOException {
 long result = bitInputStream.readBits(0);
 assertEquals(0, result);
 }

 @Test
 public void testReadBitsOneBit() throws IOException {
 long result = bitInputStream.readBits(1);
 assertEquals(1, result);
 }

 @Test
 public void testReadBitsSixBit() throws IOException {
 long result = bitInputStream.readBits(6);
 assertEquals(4096L, result);
 }

 @Test
 public void testReadBitsMaximumSize() throws IOException {
 long result = bitInputStream.readBits(63);
 assertEquals(Long.parseLong("1111111111111111111111111111111111111111", 2), result);
 }

 @Test
 public void testReadBitsBeyondMaximumSize() throws IOException {
 try {
 bitInputStream.readBits(64);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testReadBitsEndOfStream() throws IOException {
 long result = bitInputStream.readBits(1);
 assertEquals(-1, result);
 }

 @Test
 public void testReadBitsByteSwappedEndian() throws IOException {
 byteOrder = ByteOrder.BIG_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 long result = bitInputStream.readBits(6);
 assertEquals(131072L, result);
 }

 @Test(expected = IOException.class)
 public void testReadBitsIOException() throws IOException, InterruptedException {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
 BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
 bitInputStream.readBits(1);
 inputStream.close();
 try {
 bitInputStream.readBits(1);
 } finally {
 bitInputStream.close();
 }
}

 @Test
 public void testReadBitsEofDuringRead() throws IOException {
 InputStream inputStream = new ByteArrayInputStream(new byte[]{0});
 BitInputStream bitInputStream = new BitInputStream(inputStream, byteOrder);
 long result = bitInputStream.readBits(2);
 assertEquals(-1, result);
 }
}