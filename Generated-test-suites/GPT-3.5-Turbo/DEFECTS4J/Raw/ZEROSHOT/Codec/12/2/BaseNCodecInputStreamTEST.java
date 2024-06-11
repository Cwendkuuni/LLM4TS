import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {
  
  @Test
  public void readOneByteReturnsCorrectValue() throws Exception {
    byte[] data = {65};
    BaseNCodec baseNCodec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(data, baseNCodec, false);
    int result = inputStream.read();
    assertEquals(65, result);
  }
  
  @Test
  public void readMultipleBytesReturnsCorrectValues() throws Exception {
    byte[] data = {65, 66, 67};
    BaseNCodec baseNCodec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(data, baseNCodec, false);
    byte[] buffer = new byte[3];
    int bytesRead = inputStream.read(buffer, 0, buffer.length);
    assertEquals(3, bytesRead);
    assertArrayEquals(data, buffer);
  }
  
  @Test
  public void readEmptyArrayReturnsZero() throws Exception {
    byte[] data = {};
    BaseNCodec baseNCodec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(data, baseNCodec, false);
    byte[] buffer = new byte[3];
    int bytesRead = inputStream.read(buffer, 0, buffer.length);
    assertEquals(0, bytesRead);
  }
  
  @Test
  public void readReturnsNegativeOneWhenEOFReached() throws Exception {
    byte[] data = {65};
    BaseNCodec baseNCodec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(data, baseNCodec, false);
    inputStream.read();
    int result = inputStream.read();
    assertEquals(-1, result);
  }
  
  @Test
  public void markSupportedReturnsFalse() throws Exception {
    byte[] data = {65};
    BaseNCodec baseNCodec = new BaseNCodec();
    BaseNCodecInputStream inputStream = new BaseNCodecInputStream(data, baseNCodec, false);
    assertFalse(inputStream.markSupported());
  }
}