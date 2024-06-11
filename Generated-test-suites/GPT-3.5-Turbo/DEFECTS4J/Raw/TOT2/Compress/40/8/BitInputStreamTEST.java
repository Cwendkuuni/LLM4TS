import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

public class BitInputStreamTest {
  
  @Test
  public void testClearBitCache() {
    // Create a sample BitInputStream
    byte[] data = {0x01, 0x02, 0x03, 0x04};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    BitInputStream bitInput = new BitInputStream(input, ByteOrder.BIG_ENDIAN);

    // Read some bits to cache
    try {
      bitInput.readBits(16);
    } catch (IOException e) {
      fail("IOException thrown when reading bits.");
    }

    // Clear the cache
    bitInput.clearBitCache();

    // Check if cache is empty
    assertEquals(0, bitInput.bitsCachedSize);
    assertEquals(0, bitInput.bitsCached);
  }

  @Test
  public void testReadBits() {
    // Create a sample BitInputStream
    byte[] data = {0x01, 0x02, 0x03, 0x04};
    ByteArrayInputStream input = new ByteArrayInputStream(data);
    BitInputStream bitInput = new BitInputStream(input, ByteOrder.LITTLE_ENDIAN);

    // Read 8 bits
    try {
      long result1 = bitInput.readBits(8);
      assertEquals(0x01, result1);
    } catch (IOException e) {
      fail("IOException thrown when reading bits.");
    }

    // Read 16 bits
    try {
      long result2 = bitInput.readBits(16);
      assertEquals(0x0201, result2);
    } catch (IOException e) {
      fail("IOException thrown when reading bits.");
    }

    // Attempt to read more than maximum cache size
    try {
      bitInput.readBits(64);
      fail("Expected IllegalArgumentException for reading more than 63 bits.");
    } catch (IllegalArgumentException e) {
      // Expected exception
    } catch (IOException e) {
      fail("Unexpected IOException thrown.");
    }

    // End of stream check
    try {
      long result3 = bitInput.readBits(8);
      assertEquals(-1, result3);
    } catch (IOException e) {
      fail("IOException thrown when reading bits.");
    }
  }

}