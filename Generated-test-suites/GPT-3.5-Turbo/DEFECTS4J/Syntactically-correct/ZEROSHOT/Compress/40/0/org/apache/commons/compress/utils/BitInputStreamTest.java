import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
  
  @Test
  public void testConstructor() {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return -1;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    assertEquals(bitInputStream.in, in);
    assertEquals(bitInputStream.byteOrder, ByteOrder.BIG_ENDIAN);
  }
  
  @Test
  public void testClose() throws IOException {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return -1;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    bitInputStream.close();
    assertThrows(IOException.class, () -> bitInputStream.readBits(10));
  }
  
  @Test
  public void testClearBitCache() {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return -1;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    bitInputStream.clearBitCache();
    assertEquals(bitInputStream.bitsCached, 0);
    assertEquals(bitInputStream.bitsCachedSize, 0);
  }
  
  @Test
  public void testReadBits() throws IOException {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return 0;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    long bits = bitInputStream.readBits(8);
    assertEquals(bits, 0);  
  }
  
  @Test
  public void testReadBitsWithNegativeCount() {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return 0;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(-1));
  }
  
  @Test
  public void testReadBitsWithCountGreaterThanMaximumCacheSize() {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return 0;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    assertThrows(IllegalArgumentException.class, () -> bitInputStream.readBits(64));
  }
  
  @Test
  public void testReadBitsWithEndOfStream() throws IOException {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return -1;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    long bits = bitInputStream.readBits(8);
    assertEquals(bits, -1);  
  }
  
  @Test
  public void testReadBitsWithLittleEndianByteOrder() throws IOException {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return 0;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.LITTLE_ENDIAN);
    long bits = bitInputStream.readBits(8);
    assertEquals(bits, 0);  
  }
  
  @Test
  public void testReadBitsWithMask() throws IOException {
    InputStream in = new InputStream() {
      public int read() throws IOException {
        return -1;
      }
    };
    BitInputStream bitInputStream = new BitInputStream(in, ByteOrder.BIG_ENDIAN);
    long bits = bitInputStream.readBits(8);
    assertEquals(bits, -1);
    assertEquals(bitInputStream.bitsCachedSize, 0);
  }
  
}