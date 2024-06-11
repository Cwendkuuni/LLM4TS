import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

  @Test
  public void testParseOctal() {
    byte[] buffer = "12345".getBytes();
    int offset = 0;
    int length = buffer.length;
    
    long result = TarUtils.parseOctal(buffer, offset, length);
    
    assertEquals(12345, result);
  }

  @Test
  public void testParseName() {
    byte[] buffer = "TestFile".getBytes();
    int offset = 0;
    int length = buffer.length;
    
    String result = TarUtils.parseName(buffer, offset, length);
    
    assertEquals("TestFile", result);
  }

  @Test
  public void testFormatNameBytes() {
    String name = "NewFile.txt";
    byte[] buffer = new byte[10];
    int offset = 0;
    int length = buffer.length;
    
    int newOffset = TarUtils.formatNameBytes(name, buffer, offset, length);
    
    assertEquals(10, newOffset);
  }

  @Test
  public void testFormatUnsignedOctalString() {
    long value = 12345;
    byte[] buffer = new byte[5];
    int offset = 0;
    int length = buffer.length;
    
    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    
    assertEquals("12345", new String(buffer).trim());
  }

  @Test
  public void testComputeCheckSum() {
    byte[] buffer = "TestChecksum".getBytes();
    
    long sum = TarUtils.computeCheckSum(buffer);
    
    assertEquals(1276, sum);
  }
}