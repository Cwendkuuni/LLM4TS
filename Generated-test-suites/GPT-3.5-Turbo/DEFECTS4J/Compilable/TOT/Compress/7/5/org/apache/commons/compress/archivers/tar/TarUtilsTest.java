package org.apache.commons.compress.archivers.tar;
import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {

  @Test
  public void testParseOctal() {
    // Typical use case
    byte[] buffer1 = { 49, 50, 51, 52, 53 }; // ascii representation of 12345
    assertEquals(12345, TarUtils.parseOctal(buffer1, 0, 5));

    // Edge case
    byte[] buffer2 = { 48, 48 }; // ascii representation of 00
    assertEquals(0, TarUtils.parseOctal(buffer2, 0, 2));

    // Error scenario
    byte[] buffer3 = { 49, 50, 71, 52, 53 }; // invalid octal digit G
    try {
      TarUtils.parseOctal(buffer3, 0, 5);
      fail("Expected IllegalArgumentException to be thrown");
    } catch (IllegalArgumentException e) {
      // Expected IllegalArgumentException to be thrown
    }
  }

  @Test
  public void testParseName() {
    // Typical use case
    byte[] buffer1 = { 65, 66, 67, 0 }; // ascii representation of ABC
    assertEquals("ABC", TarUtils.parseName(buffer1, 0, 4));

    // Edge case
    byte[] buffer2 = { 0, 0, 0 }; // empty string
    assertEquals("", TarUtils.parseName(buffer2, 0, 3));
  }

  @Test
  public void testFormatNameBytes() {
    // Typical use case
    String name1 = "ABC";
    byte[] buffer1 = new byte[5];
    TarUtils.formatNameBytes(name1, buffer1, 0, 5);
    assertEquals(65, buffer1[0]);
    assertEquals(66, buffer1[1]);
    assertEquals(67, buffer1[2]);
    assertEquals(0, buffer1[3]);
    assertEquals(0, buffer1[4]);

    // Edge case
    String name2 = "";
    byte[] buffer2 = new byte[1];
    TarUtils.formatNameBytes(name2, buffer2, 0, 1);
    assertEquals(0, buffer2[0]);
  }

  @Test
  public void testFormatUnsignedOctalString() {
    // Typical use case
    long value1 = 12345;
    byte[] buffer1 = new byte[8];
    TarUtils.formatUnsignedOctalString(value1, buffer1, 0, 8);
    assertEquals(49, buffer1[0]);
    assertEquals(51, buffer1[1]);
    assertEquals(50, buffer1[2]);
    assertEquals(51, buffer1[3]);
    assertEquals(53, buffer1[4]);
    assertEquals(0, buffer1[5]);
    assertEquals(0, buffer1[6]);
    assertEquals(0, buffer1[7]);
    
    // Edge case
    long value2 = 0;
    byte[] buffer2 = new byte[3];
    TarUtils.formatUnsignedOctalString(value2, buffer2, 0, 3);
    assertEquals(48, buffer2[0]);
    assertEquals(0, buffer2[1]);
    assertEquals(0, buffer2[2]);
  }

  @Test
  public void testFormatOctalBytes() {
    // Typical use case
    long value1 = 12345;
    byte[] buffer1 = new byte[7];
    TarUtils.formatOctalBytes(value1, buffer1, 0, 7);
    assertEquals(49, buffer1[0]);
    assertEquals(51, buffer1[1]);
    assertEquals(50, buffer1[2]);
    assertEquals(51, buffer1[3]);
    assertEquals(53, buffer1[4]);
    assertEquals(32, buffer1[5]);
    assertEquals(0, buffer1[6]);

    // Edge case
    long value2 = 0;
    byte[] buffer2 = new byte[3];
    TarUtils.formatOctalBytes(value2, buffer2, 0, 3);
    assertEquals(48, buffer2[0]);
    assertEquals(32, buffer2[1]);
    assertEquals(0, buffer2[2]);
  }

  @Test
  public void testFormatLongOctalBytes() {
    // Typical use case
    long value1 = 12345;
    byte[] buffer1 = new byte[6];
    TarUtils.formatLongOctalBytes(value1, buffer1, 0, 6);
    assertEquals(49, buffer1[0]);
    assertEquals(51, buffer1[1]);
    assertEquals(50, buffer1[2]);
    assertEquals(51, buffer1[3]);
    assertEquals(53, buffer1[4]);
    assertEquals(32, buffer1[5]);

    // Edge case
    long value2 = 0;
    byte[] buffer2 = new byte[2];
    TarUtils.formatLongOctalBytes(value2, buffer2, 0, 2);
    assertEquals(0, buffer2[0]);
    assertEquals(32, buffer2[1]);
  }

  @Test
  public void testFormatCheckSumOctalBytes() {
    // Typical use case
    long value1 = 12345;
    byte[] buffer1 = new byte[6];
    TarUtils.formatCheckSumOctalBytes(value1, buffer1, 0, 6);
    assertEquals(49, buffer1[0]);
    assertEquals(51, buffer1[1]);
    assertEquals(50, buffer1[2]);
    assertEquals(51, buffer1[3]);
    assertEquals(53, buffer1[4]);
    assertEquals(0, buffer1[5]);
    assertEquals(32, buffer1[6]);

    // Edge case
    long value2 = 0;
    byte[] buffer2 = new byte[2];
    TarUtils.formatCheckSumOctalBytes(value2, buffer2, 0, 2);
    assertEquals(0, buffer2[0]);
    assertEquals(32, buffer2[1]);
  }

  @Test
  public void testComputeCheckSum() {
    byte[] buffer1 = { 65, 66, 67, 0 }; // ascii representation of ABC
    assertEquals(198, TarUtils.computeCheckSum(buffer1));

    byte[] buffer2 = { 65, 66, 67 }; // ascii representation of ABC
    assertEquals(198, TarUtils.computeCheckSum(buffer2));
  }

}