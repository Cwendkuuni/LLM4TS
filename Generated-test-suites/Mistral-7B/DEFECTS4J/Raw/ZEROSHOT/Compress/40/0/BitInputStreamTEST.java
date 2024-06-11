package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BitInputStreamTest {

 private InputStream inputStream;
 private ByteOrder byteOrder;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() throws Exception {
 inputStream = new MockInputStream();
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 }

 @After
 public void tearDown() throws Exception {
 bitInputStream.close();
 inputStream.close();
 }

 @Test
 public void testConstructor() {
 BitInputStream bitInputStream = new BitInputStream(null, ByteOrder.LITTLE_ENDIAN);
 assertNull(bitInputStream);
 bitInputStream = new BitInputStream(new MockInputStream(), null);
 assertNull(bitInputStream);
 bitInputStream = new BitInputStream(new MockInputStream(), ByteOrder.BIG_ENDIAN);
 assertNotNull(bitInputStream);
 }

 @Test
 public void testClose() throws IOException {
 bitInputStream.close();
 assertFalse(bitInputStream.isOpen());
 }

 @Test
 public void testClearBitCache() throws IOException {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.bitsCachedSize);
 assertEquals(0, bitInputStream.bitsCached);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithNegativeCount() throws IOException {
 bitInputStream.readBits(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
 bitInputStream.readBits(64);
 }

 @Test
 public void testReadBits() throws IOException {
 // test reading 1 bit
 assertEquals(0, bitInputStream.readBits(1));
 bitInputStream.writeBit(0);
 assertEquals(1, bitInputStream.readBits(1));

 // test reading 2 bits
 assertEquals(0, bitInputStream.readBits(2));
 bitInputStream.writeBits(11);
 assertEquals(11, bitInputStream.readBits(2));

 // test reading 3 bits
 assertEquals(0, bitInputStream.readBits(3));
 bitInputStream.writeBits(0b111);
 assertEquals(7, bitInputStream.readBits(3));

 // test reading 4 bits
 assertEquals(0, bitInputStream.readBits(4));
 bitInputStream.writeBits(0b1111);
 assertEquals(15, bitInputStream.readBits(4));

 // test reading 5 bits
 assertEquals(0, bitInputStream.readBits(5));
 bitInputStream.writeBits(0b11111);
 assertEquals(31, bitInputStream.readBits(5));

 // test reading 6 bits
 assertEquals(0, bitInputStream.readBits(6));
 bitInputStream.writeBits(0b111111);
 assertEquals(63, bitInputStream.readBits(6));

 // test reading all available bits
 assertEquals(-1, bitInputStream.readBits(8));
 bitInputStream.close();
 inputStream = new MockInputStream();
 byteOrder = ByteOrder.BIG_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 bitInputStream.writeBits(0b11111111);
 assertEquals(-1, bitInputStream.readBits(8));
 }

 @Test
 public void testReadBitsWithEndOfStream() throws IOException {
 assertEquals(-1, bitInputStream.readBits(1));
 bitInputStream.close();
 inputStream = new MockInputStream();
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 assertEquals(-1, bitInputStream.readBits(1));
 }

 @Test
 public void testReadBitsWithRandomBits() throws IOException {
 // write some random bits
 bitInputStream.writeBit(1);
 bitInputStream.writeBit(0);
 bitInputStream.writeBit(1);
 bitInputStream.writeBit(1);
 bitInputStream.writeBit(0);
 bitInputStream.writeBit(1);
 bitInputStream.writeBit(1);
 bitInputStream.writeBit(0);
 bitInputStream.writeBit(0);
 bitInputStream.writeBit(0);
 bitInputStream.writeBit(1);
 // read them back
 assertEquals(1, bitInputStream.readBits(1));
 assertEquals(0, bitInputStream.readBits(1));
 assertEquals(1, bitInputStream.readBits(1));
 assertEquals(1, bitInputStream.readBits(1));
 assertEquals(0, bitInputStream.readBits(1));
 assertEquals(1, bitInputStream.readBits(1));
 assertEquals(1, bitInputStream.readBits(1));
 assertEquals(0, bitInputStream.readBits(1));
 assertEquals(0, bitInputStream.readBits(1));
 assertEquals(0, bitInputStream.readBits(1));
 assertEquals(1, bitInputStream.readBits(1));
 }
}

class MockInputStream extends InputStream {
 @Override
 public int read() throws IOException {
 return 0xFF & (int) System.in.read();
 }

 @Override
 public void write(byte[] b, int off, int len) throws IOException {
 // no-op
 }

 @Override
 public void write(int b) throws IOException {
 // no-op
 }

 @Override
 public boolean markSupported() {
 return false;
 }

 @Override
 public void mark(int readlimit) {
 // no-op
 }

 @Override
 public void reset() throws IOException {
 // no-op
 }

 @Override
 public long skip(long n) throws IOException {
 return 0;
 }

 @Override
 public int available() throws IOException {
 return 0;
 }

 @Override
 public synchronized void close() throws IOException {
 // no-op
 }

 @Override
 public synchronized void flush() throws IOException {
 // no-op
 }
}