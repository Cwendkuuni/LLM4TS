import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError();
    error.setSourceName("file.js");
    error.setLineNumber(10);
    error.setDescription("Undefined variable");
    String expected = "file.js:10: ERROR - Undefined variable\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError();
    warning.setSourceName("file.js");
    warning.setLineNumber(5);
    warning.setDescription("Unused variable");
    String expected = "file.js:5: WARNING - Unused variable\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }
}