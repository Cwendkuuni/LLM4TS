import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  @Test
  public void testFormatErrorWithNullSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("error message", null, 0);
    String formattedError = formatter.formatError(error);
    assertEquals("ERROR - error message\n", formattedError);
  }
  
  @Test
  public void testFormatWarningWithSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl(), LINE);
    JSError warning = new JSError("warning message", "source.js", 10);
    String formattedWarning = formatter.formatWarning(warning);
    assertEquals("source.js:10: WARNING - warning message\n", formattedWarning);
  }
  
  @Test
  public void testFormatWithNoSourceExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl(), null);
    JSError error = new JSError("error message", "source.js", 20);
    String formattedError = formatter.formatError(error);
    assertEquals("WARNING - warning message\n", formattedError);
  }
  
  @Test
  public void testFormatWithRegion() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl(), LINE);
    JSError error = new JSError("error message", "source.js", 15);
    String formattedError = formatter.formatError(error);
    assertEquals("source.js:15: ERROR - error message\n", formattedError);
  }
  
  @Test
  public void testFormatWithCharno() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl(), LINE);
    JSError error = new JSError("error message", "source.js", 15, 5);
    String formattedError = formatter.formatError(error);
    assertEquals("source.js:15: ERROR - error message\n     ^\n", formattedError);
  }
  
}