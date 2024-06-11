package org.apache.commons.compress.utils;

import static org.junit.Assert.fail;

import org.apache.commons.compress.utils.BitInputStream;
import org.junit.Test;

/**
 * Test class for BitInputStream
 */
public class BitInputStreamTest {

 private InputStream inputStream;
 private ByteOrder byteOrder;
 private BitInputStream bitInputStream;

 @SuppressWarnings("ResultOfMethodCallIgnored") // for InputStream.close()
 @Before
 public void setup() throws Exception {
 inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04 });
 byteOrder = ByteOrder.LITTLE_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 }

 @After
 public void tearDown() throws Exception {
 bitInputStream.close();
 inputStream.close();
 }

 /**
 * Test constructor
 */
 @Test
 public void testConstructor() {
 // no specific verification needed
 }

 /**
 * Test close method
 */
 @Test
 public void testClose() throws Exception {
 inputStream.close();
 try {
 bitInputStream.close();
 } catch (IOException e) {
 // expected
 }
 }

 /**
 * Test clearBitCache method
 */
 @Test
 public void testClearBitCache() throws Exception {
 bitInputStream.clearBitCache();
 assertEquals(0, bitInputStream.readBits(64));
 }

 /**
 * Test readBits method with valid parameters
 */
 @Test
 public void testReadBits() throws Exception {
 // test reading 1 bit
 assertEquals(1, bitInputStream.readBits(1));
 // test reading multiple bits
 assertEquals(0b11111111, bitInputStream.readBits(8));
 // test reading bits beyond the available data
 assertEquals(-1, bitInputStream.readBits(9));
 }

 /**
 * Test readBits method with invalid parameters
 */
 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithInvalidParameters() throws Exception {
 // test reading a negative number of bits
 bitInputStream.readBits(-1);
 // test reading a number of bits greater than the maximum allowed
 bitInputStream.readBits(64);
 }

 /**
 * Test readBits method with EOF
 */
 @Test
 public void testReadBitsWithEOF() throws Exception {
 // read all available bits
 bitInputStream.readBits(32);
 // read past EOF
 assertEquals(-1, bitInputStream.readBits(1));
 }

 /**
 * Test readBits with a byte-swapped byte order
 */
 @Test
 public void testReadBitsWithByteSwappedByteOrder() throws Exception {
 byteOrder = ByteOrder.BIG_ENDIAN;
 bitInputStream = new BitInputStream(inputStream, byteOrder);
 // test reading 1 bit
 assertEquals(1, bitInputStream.readBits(1));
 // test reading multiple bits
 assertEquals(0b00111111, bitInputStream.readBits(8));
 // test reading bits beyond the available data
 assertEquals(-1, bitInputStream.readBits(9));
 }
}