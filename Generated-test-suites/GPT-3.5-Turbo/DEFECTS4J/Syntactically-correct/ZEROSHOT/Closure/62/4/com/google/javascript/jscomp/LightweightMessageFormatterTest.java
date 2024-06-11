import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(null);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("");
    String expected = ": ERROR - \n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("");
    String expected = ": WARNING - \n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testConstructorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }

  @Test
  public void testFormat() {
    JSError error = new JSError("");
    String expected = ": ERROR - \n";
    String actual = formatter.format(error, false);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatLine() {
    LightweightMessageFormatter.LineNumberingFormatter lineFormatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String line = "";
    int lineNumber = 1;
    String expected = "";
    String actual = lineFormatter.formatLine(line, lineNumber);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatRegion() {
    LightweightMessageFormatter.LineNumberingFormatter lineFormatter =
        new LightweightMessageFormatter.LineNumberingFormatter();
    String code = "";
    int beginningLineNumber = 1;
    int endingLineNumber = 1;
    LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region(code, beginningLineNumber, endingLineNumber);
    String expected = "";
    String actual = lineFormatter.formatRegion(region);
    assertEquals(expected, actual);
  }
}