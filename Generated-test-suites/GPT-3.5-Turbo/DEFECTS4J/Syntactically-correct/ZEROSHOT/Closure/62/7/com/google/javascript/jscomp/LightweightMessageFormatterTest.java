import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.JSError;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = LightweightMessageFormatter.withoutSource();
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("Test Error", "testFile.js", 10, 5);
    String expected = "testFile.js:10: ERROR - Test Error\n";
    assertEquals(expected, formatter.formatError(error));
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Warning", "testFile.js", 5, 2);
    String expected = "testFile.js:5: WARNING - Test Warning\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }
  
  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Test Error", null, -1, -1);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, formatter.formatError(error));
  }
  
  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Test Warning", null, -1, -1);
    String expected = "WARNING - Test Warning\n";
    assertEquals(expected, formatter.formatWarning(warning));
  }
}