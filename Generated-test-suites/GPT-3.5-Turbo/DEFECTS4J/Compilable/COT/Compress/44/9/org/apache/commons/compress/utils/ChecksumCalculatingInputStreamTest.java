package org.apache.commons.compress.utils;

import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

public class ChecksumCalculatingInputStreamTest {
  
  @Test
  public void testRead() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    Checksum checksum = new Adler32();
    ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, inputStream);
    
    Assert.assertEquals(1, in.read());
    Assert.assertEquals(2, in.read());
    Assert.assertEquals(3, in.read());
    Assert.assertEquals(4, in.read());
    Assert.assertEquals(5, in.read());
    Assert.assertEquals(-1, in.read());
    
    in.close();
    Assert.assertEquals(0x62, in.getValue());
  }
  
  @Test
  public void testReadByteArray() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    Checksum checksum = new Adler32();
    ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, inputStream);
    
    byte[] buffer = new byte[3];
    
    Assert.assertEquals(3, in.read(buffer));
    Assert.assertArrayEquals(new byte[]{1, 2, 3}, buffer);
    
    Assert.assertEquals(2, in.read(buffer));
    Assert.assertArrayEquals(new byte[]{4, 5, 3}, buffer);
    
    Assert.assertEquals(-1, in.read(buffer));
    Assert.assertEquals(-1, in.read(buffer));
    
    in.close();
    Assert.assertEquals(0x62, in.getValue());
  }
  
  @Test
  public void testReadByteArrayWithOffset() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
    Checksum checksum = new Adler32();
    ChecksumCalculatingInputStream in = new ChecksumCalculatingInputStream(checksum, inputStream);
    
    byte[] buffer = new byte[5];
    
    Assert.assertEquals(3, in.read(buffer, 0, 3));
    Assert.assertArrayEquals(new byte[]{1, 2, 3, 0, 0}, buffer);
    
    Assert.assertEquals(2, in.read(buffer, 2, 2));
    Assert.assertArrayEquals(new byte[]{1, 2, 4, 5, 0}, buffer);
    
    Assert.assertEquals(-1, in.read(buffer, 0, 2));
    Assert.assertEquals(-1, in.read(buffer, 0, 2));
    
    in.close();
    Assert.assertEquals(0x62, in.getValue());
  }
}
