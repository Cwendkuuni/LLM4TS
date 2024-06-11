package org.apache.commons.compress.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BitInputStreamTest {

 private InputStream inputStreamMock;
 private BitInputStream bitInputStream;

 @Before
 public void setUp() {
 inputStreamMock = mock(InputStream.class);
 bitInputStream = new BitInputStream(inputStreamMock, ByteOrder.LITTLE_ENDIAN);
 }

 @After
 public void tearDown() throws Exception {
 bitInputStream.close();
 }

 @Test
 public void testReadBitsWithValidCountAndValidInput() throws IOException {
 final int count = 5;
 final long expectedValue = 0b110101; // binary: 110101

 when(inputStreamMock.read()).thenReturn((byte) 0x4d); // binary: 01110111

 long actualValue = bitInputStream.readBits(count);

 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithNegativeCount() throws IOException {
 final int count = -1;
 bitInputStream.readBits(count);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReadBitsWithCountGreaterThanMaximum() throws IOException {
 final int count = 64;
 bitInputStream.readBits(count);
 }

 @Test
 public void testReadBitsWhenEndOfStreamReached() throws IOException {
 final int count = 3;
 when(inputStreamMock.read()).thenReturn((byte) -1);

 long actualValue = bitInputStream.readBits(count);

 assertEquals(-1, actualValue);
 }

 @Test
 public void testClearBitCache() throws IOException {
 final int count = 5;
 when(inputStreamMock.read()).thenReturn((byte) 0x4d);

 bitInputStream.readBits(count);

 assertEquals(0, bitInputStream.bitsCachedSize);
 }
}