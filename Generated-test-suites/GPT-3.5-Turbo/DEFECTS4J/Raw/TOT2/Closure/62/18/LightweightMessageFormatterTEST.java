import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    // Typical use case
    // Create an error instance
    JSError error = new JSError("Test error", "test.js", 10);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    
    // Test formatting error message
    String formattedError = formatter.formatError(error);
    
    // Check if the formatted error message is correct
    assertEquals("ERROR - Test error\n", formattedError);
  }

  @Test
  public void testFormatWarning() {
    // Typical use case
    // Create a warning instance
    JSError warning = new JSError("Test warning", "test.js", 5);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
    
    // Test formatting warning message
    String formattedWarning = formatter.formatWarning(warning);
    
    // Check if the formatted warning message is correct
    assertEquals("WARNING - Test warning\n", formattedWarning);
  }

  @Test
  public void testWithoutSource() {
    // Typical use case
    // Create a formatter without source information
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    
    // Test if the formatter without source is created successfully
    assertEquals(LINE, formatter.getExcerpt());
  }
  
  // Additional test methods for edge cases and exception handling scenarios can be added here
  
}