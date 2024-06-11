package org.apache.commons.compress.utils;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {
  
  @Test
  public void testReadSingleByte() throws IOException {
    byte[] data = {0x41, 0x42, 0x43};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    CRC32 checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    assertEquals(0x41, stream.read());
    assertEquals(0x42, stream.read());
    assertEquals(0x43, stream.read());
    assertEquals(-1, stream.read());
    assertEquals(0x83170D50, checksum.getValue());
  }
  
  @Test
  public void testReadByteArray() throws IOException {
    byte[] data = {0x41, 0x42, 0x43};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    CRC32 checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    byte[] buffer = new byte[3];
    assertEquals(3, stream.read(buffer));
    assertArrayEquals(data, buffer);
    assertEquals(-1, stream.read(buffer));
    assertEquals(0x83170D50, checksum.getValue());
  }
  
  @Test
  public void testSkip() throws IOException {
    byte[] data = {0x41, 0x42, 0x43};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    CRC32 checksum = new CRC32();
    ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(checksum, input);
    
    assertEquals(1, stream.skip(1));
    assertEquals(0, stream.skip(1));
    assertEquals(0x826E0482, checksum.getValue());
  }

}