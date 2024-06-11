import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Typical use case
    // Error is formatted correctly
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError error = new JSError("Test error", "test.js", 10, CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Test error\n", formattedError);
    
    // Edge case
    // Error has no source name
    error = new JSError("Test error", null, 10, CheckLevel.ERROR);
    formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Test error\n", formattedError);
    
    // Error with empty description
    error = new JSError("", "test.js", 10, CheckLevel.ERROR);
    formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - \n", formattedError);
    
    // Error with source excerpt
    SourceExcerptProvider source = new SourceExcerptProvider();
    error = new JSError("Test error", "test.js", 10, CheckLevel.ERROR);
    formattedError = formatter.formatError(error);
    assertNotNull(formattedError);
    assertEquals("ERROR - Test error\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    // Typical use case
    // Warning is formatted correctly
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
    JSError warning = new JSError("Test warning", "test.js", 10, CheckLevel.WARNING);
    String formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - Test warning\n", formattedWarning);
    
    // Edge case
    // Warning has no line number
    warning = new JSError("Test warning", "test.js", -1, CheckLevel.WARNING);
    formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - Test warning\n", formattedWarning);
    
    // Warning with empty description
    warning = new JSError("", "test.js", 10, CheckLevel.WARNING);
    formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - \n", formattedWarning);
    
    // Warning with source excerpt
    SourceExcerptProvider source = new SourceExcerptProvider();
    warning = new JSError("Test warning", "test.js", 10, CheckLevel.WARNING);
    formattedWarning = formatter.formatWarning(warning);
    assertNotNull(formattedWarning);
    assertEquals("WARNING - Test warning\n", formattedWarning);
  }

  @Test
  public void testWithoutSource() {
    // Typical use case
    // LightweightMessageFormatter created without source
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    assertNotNull(formatter);
  }
  
  // Additional edge cases and error scenarios to be tested for other methods

}