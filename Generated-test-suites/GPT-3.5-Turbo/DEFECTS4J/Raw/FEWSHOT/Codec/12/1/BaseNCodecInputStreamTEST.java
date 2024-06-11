package org.apache.commons.codec.binary;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.Test;

public class BaseNCodecInputStreamTest {
  
  private static final byte[] TEST_DATA = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47 };
  
  private static final byte[] TEST_ENCODED_DATA = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47 };
  
  private static final byte[] TEST_DECODED_DATA = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47 };
  
  private static final byte[] ENCODED_PADDING_DATA = new byte[] { -114, -68, -98, -86, -68, -102, -96, -70, -100, -44, -41, -29 };
  
  private static final byte[] DECODED_PADDING_DATA = new byte[] { 0x01 };
  
  private static final byte[] ENCODED_CHUNKED_DATA = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67 };
  
  private static final byte[] DECODED_CHUNKED_DATA = new byte[] { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67 }; 

  private BaseNCodecInputStreamTest test;
  
  @Test(expected = NullPointerException.class)
  public void testConstructorNull() {
    new BaseNCodecInputStream(null, new Base64(), true);
  }
  
  @Test(expected = NullPointerException.class)
  public void testConstructorWithInputStreamNull() {
    new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), null, true);
  }
  
   @Test
   public void testConstructorWithParameters() throws Exception {
      InputStream is = new ByteArrayInputStream(TEST_DATA);
      try (BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(is, new Base64(), true)) {
          assertEquals(is, baseNCodecInputStream.in);
          assertTrue(Arrays.equals(TEST_DATA, baseNCodecInputStream.singleByte));
      }
   }
  
  @Test
  public void testMandatoryMethods() throws Exception {
    BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
    baseNCodecInputStream.read();
    baseNCodecInputStream.read(new byte[10], 0, 1);
    assertTrue(baseNCodecInputStream.markSupported());
    try {
      baseNCodecInputStream.skip(10);
      fail("Exception should have been thrown.");
    } catch (IOException e) {
      assertEquals("Skipping is not supported.", e.getMessage());
    }
    baseNCodecInputStream.close();
  }
  
  @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() throws Exception {
      BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
      baseNCodecInputStream.skip(-1);
    }
  
  @Test
  public void testReadWithZeroLength() throws Exception {
      BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
      assertEquals(0, baseNCodecInputStream.read(new byte[0], 0, 0));
      baseNCodecInputStream.close();
  }
  
  @Test
    public void testReadWithNullBytes() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        assertNull(baseNCodecInputStream.read(null, 0, 10));
        baseNCodecInputStream.close();
    }
  
  @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        baseNCodecInputStream.read(new byte[10], -1, 10);
        baseNCodecInputStream.close();
    }
  
  @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLen() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        baseNCodecInputStream.read(new byte[10], 0, -1);
        baseNCodecInputStream.close();
    }
  
  @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBuffer() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        baseNCodecInputStream.read(new byte[10], 5, 10);
        baseNCodecInputStream.close();
    }
  
 @Test
    public void testReadWithEncode() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        byte[] buffer = new byte[10];
        assertEquals(4, baseNCodecInputStream.read(buffer, 0, 4));
        assertTrue(Arrays.equals(Arrays.copyOfRange(ENCODED_PADDING_DATA, 0, buffer.length), buffer));
        baseNCodecInputStream.close();
    }
  
   @Test
    public void testReadWithDecode() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(ENCODED_PADDING_DATA), new Base64(), false);
        byte[] buffer = new byte[10];
        assertEquals(1, baseNCodecInputStream.read(buffer, 0, 2));
        assertTrue(Arrays.equals(Arrays.copyOfRange(DECODED_PADDING_DATA, 0, buffer.length), buffer));
        baseNCodecInputStream.close();
    }
   
   @Test
    public void testReadWithEncodeAndChunkedData() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        byte[] buffer = new byte[10];
        assertEquals(9, baseNCodecInputStream.read(buffer, 0, 10));
        assertTrue(Arrays.equals(Arrays.copyOfRange(ENCODED_CHUNKED_DATA, 0, buffer.length), buffer));
        baseNCodecInputStream.close();
    }
   
  @Test
    public void testReadWithDecodeAndChunkedData() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(ENCODED_CHUNKED_DATA), new Base64(), false);
        byte[] buffer = new byte[10];
        assertEquals(14, baseNCodecInputStream.read(buffer, 0, 15));
        assertTrue(Arrays.equals(Arrays.copyOfRange(DECODED_CHUNKED_DATA, 0, buffer.length), buffer));
        baseNCodecInputStream.close();
    }
  
  @Test
    public void testReadWithEOF() throws Exception {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(TEST_DATA), new Base64(), true);
        byte[] buffer = new byte[10];
        assertEquals(-1, baseNCodecInputStream.read(buffer, 10, 10));
        baseNCodecInputStream.close();
    }
  
}