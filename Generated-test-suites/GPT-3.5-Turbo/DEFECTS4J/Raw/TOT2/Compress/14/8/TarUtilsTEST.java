import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TarUtilsTest {
  
  private byte[] testBuffer;
  
  @Before
  public void setUp() {
    testBuffer = new byte[10];
  }

  @Test
  public void testParseOctal_validInput_returnsLong() {
    testBuffer[0] = 0x31; // ASCII value of '1'
    testBuffer[1] = 0x32; // ASCII value of '2'
    testBuffer[2] = 0x33; // ASCII value of '3'

    long result = TarUtils.parseOctal(testBuffer, 0, 3);

    assertEquals(123L, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseOctal_invalidInput_throwsException() {
    testBuffer[0] = 0x31; // ASCII value of '1'
    testBuffer[1] = 0x2A; // '*' is not an octal digit

    TarUtils.parseOctal(testBuffer, 0, 2);
  }

  @Test
  public void testParseOctal_emptyInput_returnsZero() {
    long result = TarUtils.parseOctal(testBuffer, 0, 0);

    assertEquals(0L, result);
  }

  // Add more test cases for other public methods in TarUtils class

}