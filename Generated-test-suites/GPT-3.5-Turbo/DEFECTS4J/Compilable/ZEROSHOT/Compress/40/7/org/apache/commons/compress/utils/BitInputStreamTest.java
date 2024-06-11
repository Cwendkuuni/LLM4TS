package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;

public class BitInputStreamTest {
  
  private InputStream inputStream;
  private BitInputStream bitInputStream;

  @Before
  public void setUp() throws Exception {
    byte[] data = {0x01, 0x02, 0x03};
    inputStream = new ByteArrayInputStream(data);
    bitInputStream = new BitInputStream(inputStream, ByteOrder.BIG_ENDIAN);
  }

  @After
  public void tearDown() throws IOException {
    bitInputStream.close();
  }

  @Test
  public void testReadBitsPositive() throws IOException {
    assertEquals(1, bitInputStream.readBits(1));
    assertEquals(2, bitInputStream.readBits(2));
    assertEquals(1, bitInputStream.readBits(3));
  }

  @Test
  public void testReadBitsZero() throws IOException {
    assertEquals(0, bitInputStream.readBits(0));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReadBitsNegativeCount() throws IOException {
    bitInputStream.readBits(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReadBitsCountTooLarge() throws IOException {
    bitInputStream.readBits(64);
  }

  @Test
  public void testReadBitsEndOfStream() throws IOException {
    bitInputStream.readBits(24); // Consume all bits
    assertEquals(-1, bitInputStream.readBits(1)); // Expecting end of stream
  }

  @Test
  public void testClearBitCache() throws IOException {
    bitInputStream.clearBitCache();
    assertEquals(0, bitInputStream.readBits(3)); // Expecting 0 after cache cleared
  }

}