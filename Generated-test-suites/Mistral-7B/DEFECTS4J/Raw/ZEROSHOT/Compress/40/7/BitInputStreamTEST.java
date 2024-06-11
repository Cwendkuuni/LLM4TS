package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BitInputStreamTest {

 private BitInputStream bitInputStream;
 private InputStream inputStreamMock;

 @Before
 public void setUp() {
 inputStreamMock = mock(InputStream.class);
 bitInputStream = new BitInputStream(inputStreamMock, ByteOrder.LITTLE_ENDIAN);
 }

 @Test
 public void testReadBitsWhenByteIsReadThenReturnCorrectBits() throws IOException {
 when(inputStreamMock.read()).thenReturn((byte) 0x12).thenReturn((byte) 0x34).thenReturn((byte) -1);

 long expectedBits = 0b11111111_11111111_00101100;
 long actualBits = 0;

 actualBits |= bitInputStream.readBits(5);
 assertEquals(expectedBits >>> 3, actualBits);

 actualBits |= bitInputStream.readBits(6);
 assertEquals(expectedBits, actualBits);
 }

 @Test
 public void testReadBitsWhenEndOfStreamIsReachedThenReturnMinusOne() throws IOException {
 when(inputStreamMock.read()).thenReturn((byte) 0x12).thenReturn((byte) 0x34).thenReturn((byte) -1);

 long actualBits = 0;
 actualBits |= bitInputStream.readBits(7);
 assertEquals(-1, actualBits);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWhenCountIsNegative() throws IOException {
 bitInputStream.readBits(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWhenCountIsGreaterThanMaximumCacheSize() throws IOException {
 bitInputStream.readBits(64);
 }

 @Test
 public void testClearBitCache() throws IOException {
 long bitsBeforeClearCache = bitInputStream.readBits(11);
 bitInputStream.clearBitCache();
 long bitsAfterClearCache = bitInputStream.readBits(11);
 assertEquals(0, bitsAfterClearCache);
 }
}