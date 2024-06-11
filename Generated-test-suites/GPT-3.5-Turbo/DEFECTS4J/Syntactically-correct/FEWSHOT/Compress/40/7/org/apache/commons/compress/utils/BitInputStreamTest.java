
package org.apache.commons.compress.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.junit.Test;

public class BitInputStreamTest {
  
  @Test
  public void testReadBits() throws IOException {
    // Test reading 0 bits
    BitInputStream bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0;
      }
    }, ByteOrder.BIG_ENDIAN);
    assertEquals(0, bis.readBits(0));
    bis.close();
    
    // Test reading positive number of bits
    bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0xFF;
      }
    }, ByteOrder.LITTLE_ENDIAN);
    assertEquals(255, bis.readBits(8));
    bis.close();
    
    // Test reading more bits than available
    bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0xFF;
      }
    }, ByteOrder.BIG_ENDIAN);
    assertEquals(-1, bis.readBits(64));
    bis.close();
    
    // Test reading bits with different byte orders
    bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0xAB;
      }
    }, ByteOrder.BIG_ENDIAN);
    assertEquals(175, bis.readBits(8));
    bis.close();
    
    bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0xAB;
      }
    }, ByteOrder.LITTLE_ENDIAN);
    assertEquals(171, bis.readBits(8));
    bis.close();
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReadBitsWithInvalidCount() throws IOException {
    BitInputStream bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0;
      }
    }, ByteOrder.BIG_ENDIAN);
    bis.readBits(-1);
    bis.close();
  }
  
  @Test
  public void testClearBitCache() throws IOException {
    BitInputStream bis = new BitInputStream(new InputStream() {
      public int read() throws IOException {
        return 0xFF;
      }
    }, ByteOrder.BIG_ENDIAN);
    
    // Read some bits to populate the cache
    bis.readBits(16);
    
    // Clear the cache
    bis.clearBitCache();
    
    // Verify that cache is cleared
    assertEquals(0, bis.readBits(8));
    bis.close();
  }
  
}

