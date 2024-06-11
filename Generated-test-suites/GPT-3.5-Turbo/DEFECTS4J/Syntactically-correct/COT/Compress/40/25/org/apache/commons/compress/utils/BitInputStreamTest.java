import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.compress.utils.BitInputStream;

public class BitInputStreamTest {
  
  @Test
  public void testConstructor() {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA, (byte)0xBB});
    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    
    // when
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // then
    assertNotNull(bitInputStream);
  }
  
  @Test
  public void testClose() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA});
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    bitInputStream.close();
    
    // then
    // no exception should be thrown
  }
  
  @Test
  public void testClearBitCache() {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA});
    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    bitInputStream.readBits(4);
    
    // when
    bitInputStream.clearBitCache();
    
    // then
    // no exception should be thrown
  }
  
  @Test
  public void testReadBits() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA, (byte)0xBB});
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    long result = bitInputStream.readBits(16);
    
    // then
    assertEquals(0xBBAA, result);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReadBitsNegativeCount() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA});
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    bitInputStream.readBits(-1);
    
    // then
    // expect IllegalArgumentException
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testReadBitsCountGreaterThanMaximumCacheSize() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA});
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    bitInputStream.readBits(64);
    
    // then
    // expect IllegalArgumentException
  }
  
  @Test
  public void testReadBitsEndOfStream() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{});
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    long result = bitInputStream.readBits(16);
    
    // then
    assertEquals(-1, result);
  }
  
  @Test
  public void testReadBitsLittleEndian() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA, (byte)0xBB});
    ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    long result = bitInputStream.readBits(4);
    
    // then
    assertEquals(0xA, result);
  }
  
  @Test
  public void testReadBitsBigEndian() throws IOException {
    // given
    InputStream in = new ByteArrayInputStream(new byte[]{(byte)0xAA, (byte)0xBB});
    ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    BitInputStream bitInputStream = new BitInputStream(in, byteOrder);
    
    // when
    long result = bitInputStream.readBits(4);
    
    // then
    assertEquals(0xB, result);
  }
  
}