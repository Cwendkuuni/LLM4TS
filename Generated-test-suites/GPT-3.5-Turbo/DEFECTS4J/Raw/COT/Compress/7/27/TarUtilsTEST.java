import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {

  // Test parseOctal method

  @Test
  public void testParseOctal() {
    byte[] buffer = {49, 50, 51, 0};
    long result = TarUtils.parseOctal(buffer, 0, 4);
    assertEquals(123, result);
  }

  // Test parseName method

  @Test
  public void testParseName() {
    byte[] buffer = {65, 66, 67, 0};
    String result = TarUtils.parseName(buffer, 0, 4);
    assertEquals("ABC", result);
  }

  // Test formatNameBytes method

  @Test
  public void testFormatNameBytes() {
    String name = "Hello";
    byte[] buf = new byte[6];
    int result = TarUtils.formatNameBytes(name, buf, 0, 6);
    assertArrayEquals(new byte[]{72, 101, 108, 108, 111, 0}, buf);
    assertEquals(6, result);
  }

  // Test formatUnsignedOctalString method

  @Test
  public void testFormatUnsignedOctalString() {
    byte[] buffer = new byte[5];
    TarUtils.formatUnsignedOctalString(123, buffer, 0, 5);
    assertArrayEquals(new byte[]{48, 48, 49, 51, 51}, buffer);
  }

  // Test formatOctalBytes method

  @Test
  public void testFormatOctalBytes() {
    byte[] buf = new byte[4];
    int result = TarUtils.formatOctalBytes(123, buf, 0, 4);
    assertArrayEquals(new byte[]{48, 48, 49, 32}, buf);
    assertEquals(4, result);
  }

  // Test formatLongOctalBytes method

  @Test
  public void testFormatLongOctalBytes() {
    byte[] buf = new byte[4];
    int result = TarUtils.formatLongOctalBytes(123, buf, 0, 4);
    assertArrayEquals(new byte[]{49, 50, 51, 32}, buf);
    assertEquals(4, result);
  }

  // Test formatCheckSumOctalBytes method

  @Test
  public void testFormatCheckSumOctalBytes() {
    byte[] buf = new byte[4];
    int result = TarUtils.formatCheckSumOctalBytes(123, buf, 0, 4);
    assertArrayEquals(new byte[]{49, 50, 51, 32}, buf);
    assertEquals(4, result);
  }

  // Test computeCheckSum method

  @Test
  public void testComputeCheckSum() {
    byte[] buf = {49, 50, 51};
    long result = TarUtils.computeCheckSum(buf);
    assertEquals(186, result);
  }

}