
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
    JSError error = new JSError("Test error message");
    String result = formatter.formatError(error);
    assertEquals("ERROR - Test error message\n", result);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test warning message");
    String result = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning message\n", result);
  }
  
  @Test
  public void testFormatError_NullError() {
    NullPointerException exception = assertThrows(
        NullPointerException.class, 
        () -> formatter.formatError(null));
    assertEquals("error must not be null", exception.getMessage());
  }
  
  @Test
  public void testFormatError_NullSourceName() {
    JSError error = new JSError(null, 1, "Test error", null);
    String result = formatter.formatError(error);
    assertEquals("ERROR - Test error\n", result);
  }
  
  @Test
  public void testFormatError_NegativeLineNumber() {
    JSError error = new JSError("Test error", null, -1, null);
    String result = formatter.formatError(error);
    assertEquals("ERROR - Test error\n", result);
  }
  
  @Test
  public void testFormatError_SourceExcerptEndsBeforeCharno() {
    JSError error = new JSError("Test error", "source.js", 1, null);
    error.setCharno(5);
    String result = formatter.formatError(error);
    assertEquals("source.js:1: ERROR - Test error\n", result);
  }
  
  @Test
  public void testFormatWarning_NullWarning() {
    NullPointerException exception = assertThrows(
        NullPointerException.class, 
        () -> formatter.formatWarning(null));
    assertEquals("warning must not be null", exception.getMessage());
  }
  
  @Test
  public void testFormatWarning_NullSourceName() {
    JSError warning = new JSError(null, 1, "Test warning", null);
    String result = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning\n", result);
  }
  
  @Test
  public void testFormatWarning_NegativeLineNumber() {
    JSError warning = new JSError("Test warning", null, -1, null);
    String result = formatter.formatWarning(warning);
    assertEquals("WARNING - Test warning\n", result);
  }
  
  @Test
  public void testFormatWarning_SourceExcerptEndsBeforeCharno() {
    JSError warning = new JSError("Test warning", "source.js", 1, null);
    warning.setCharno(5);
    String result = formatter.formatWarning(warning);
    assertEquals("source.js:1: WARNING - Test warning\n", result);
  }
}
