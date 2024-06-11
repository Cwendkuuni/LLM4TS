import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class TarUtilsTest {

  private byte[] buffer;
  private final int offset = 0;
  private final int length = 10;

  @Before
  public void setUp() {
    buffer = new byte[length];
  }

  @Test
  public void testParseOctal_withValidInput_returnsCorrectValue() {
    String octalNum = "12345";
    buffer = octalNum.getBytes();
    long expected = 012345L;

    long result = TarUtils.parseOctal(buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testParseOctal_withNonOctalInput_throwsIllegalArgumentException() {
    String nonOctalNum = "abcdef";
    buffer = nonOctalNum.getBytes();

    TarUtils.parseOctal(buffer, offset, length);
  }

  @Test
  public void testParseOctal_withTrailingNull_returnsCorrectValue() {
    String octalNum = "12345\0";
    buffer = octalNum.getBytes();
    long expected = 012345L;

    long result = TarUtils.parseOctal(buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testParseName_withValidInput_returnsCorrectName() {
    String expected = "test";
    buffer = expected.getBytes();

    String result = TarUtils.parseName(buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testParseName_withTrailingNull_returnsCorrectName() {
    String input = "test\0";
    buffer = input.getBytes();
    String expected = "test";

    String result = TarUtils.parseName(buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatNameBytes_withValidInput_returnsUpdatedOffset() {
    String name = "test";
    int expected = offset + length;

    int result = TarUtils.formatNameBytes(name, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatNameBytes_withLongName_returnsUpdatedOffset() {
    String name = "ThisIsALongName";
    int expected = offset + length;

    int result = TarUtils.formatNameBytes(name, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatNameBytes_withShortName_returnsUpdatedOffset() {
    String name = "shrt";
    int expected = offset + length;

    int result = TarUtils.formatNameBytes(name, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatUnsignedOctalString_withZeroValue_returnsCorrectString() {
    long value = 0L;
    String expected = "0000000000";

    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    String result = new String(buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatUnsignedOctalString_withValueTooLarge_throwsIllegalArgumentException() {
    long value = Long.MAX_VALUE;

    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
  }

  @Test
  public void testFormatUnsignedOctalString_withValidValue_returnsCorrectString() {
    long value = 12345L;
    String expected = "0000003011";

    TarUtils.formatUnsignedOctalString(value, buffer, offset, length);
    String result = new String(buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatOctalBytes_withZeroValue_returnsUpdatedOffset() {
    long value = 0L;
    int expected = offset + length;

    int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatOctalBytes_withValidValue_returnsUpdatedOffset() {
    long value = 12345L;
    int expected = offset + length;

    int result = TarUtils.formatOctalBytes(value, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatLongOctalBytes_withValidValue_returnsUpdatedOffset() {
    long value = 12345L;
    int expected = offset + length;

    int result = TarUtils.formatLongOctalBytes(value, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatCheckSumOctalBytes_withValidValue_returnsUpdatedOffset() {
    long value = 12345L;
    int expected = offset + length;

    int result = TarUtils.formatCheckSumOctalBytes(value, buffer, offset, length);

    assertEquals(expected, result);
  }

  @Test
  public void testComputeCheckSum_withValidBuffer_returnsCorrectChecksum() {
    buffer = "ThisIsATest".getBytes();
    long expected = 1058L;

    long result = TarUtils.computeCheckSum(buffer);

    assertEquals(expected, result);
  }

}