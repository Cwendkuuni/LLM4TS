package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

  @Test
  public void testParseOctal() {
    byte[] buffer = new byte[] {'1', '2', '3', '4'};
    int offset = 0;
    int length = 4;
    long result = TarUtils.parseOctal(buffer, offset, length);
    assertEquals(1234L, result);
  }

  @Test
  public void testParseName() {
    byte[] buffer = new byte[] {'t', 'e', 's', 't', 0};
    int offset = 0;
    int length = 5;
    String result = TarUtils.parseName(buffer, offset, length);
    assertEquals("test", result);
  }

  @Test
  public void testFormatNameBytes() {
    String name = "test";
    byte[] buf = new byte[10];
    int offset = 0;
    int length = 5;
    int newOffset = TarUtils.formatNameBytes(name, buf, offset, length);
    assertEquals(5, newOffset);
    assertEquals("test\0\0\0\0", new String(buf));
  }

  @Test
  public void testComputeCheckSum() {
    byte[] buf = new byte[] {'a', 'b', 'c'};
    long result = TarUtils.computeCheckSum(buf);
    assertEquals(294L, result);
  }

  // Include more test cases for other methods...

}