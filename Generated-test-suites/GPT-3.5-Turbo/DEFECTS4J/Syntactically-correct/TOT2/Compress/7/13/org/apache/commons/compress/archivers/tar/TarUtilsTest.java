import static org.junit.Assert.*;
import org.junit.Test;

public class TarUtilsTest {
  
  @Test
  public void testParseOctal_validInput() {
    byte[] buffer = "123\0".getBytes();
    long expected = 123L;
    assertEquals(expected, TarUtils.parseOctal(buffer, 0, buffer.length));
  }

  @Test
  public void testParseName_validInput() {
    byte[] buffer = "TestName\0".getBytes();
    String expected = "TestName";
    assertEquals(expected, TarUtils.parseName(buffer, 0, buffer.length));
  }

  @Test
  public void testFormatNameBytes_validInput() {
    String name = "TestName";
    byte[] buffer = new byte[8];
    int expectedOffset = 8;
    assertEquals(expectedOffset, TarUtils.formatNameBytes(name, buffer, 0, 8));
  }

  @Test
  public void testFormatOctalBytes_validInput() {
    long value = 123L;
    byte[] buffer = new byte[8];
    int expectedOffset = 8;
    assertEquals(expectedOffset, TarUtils.formatOctalBytes(value, buffer, 0, 8));
  }

  @Test
  public void testComputeCheckSum_validInput() {
    byte[] buffer = "TestBuffer".getBytes();
    long expected = 870L; // Sum of ASCII values of "TestBuffer"
    assertEquals(expected, TarUtils.computeCheckSum(buffer));
  }
}