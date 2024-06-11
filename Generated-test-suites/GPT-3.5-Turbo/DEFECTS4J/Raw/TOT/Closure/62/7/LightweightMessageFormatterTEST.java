
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError_WithSource_ExcerptFound() {
    JSError error = new JSError("Test error message");
    error.sourceName = "test.js";
    error.lineNumber = 5;
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl());
    String expected = "test.js:5 ERROR - Test error message\n"
        + "Test source excerpt\n"
        + "          ^\n";
    String result = formatter.formatError(error);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatError_WithoutSource_NoExcerpt() {
    JSError error = new JSError("Test error message");
    
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String expected = "ERROR - Test error message\n";
    String result = formatter.formatError(error);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning_WithSource_ExcerptFound() {
    JSError warning = new JSError("Test warning message");
    warning.sourceName = "test.js";
    warning.lineNumber = 10;
    
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl());
    String expected = "test.js:10 WARNING - Test warning message\n"
        + "Test source excerpt\n"
        + "           ^\n";
    String result = formatter.formatWarning(warning);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning_WithoutSource_NoExcerpt() {
    JSError warning = new JSError("Test warning message");
    
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String expected = "WARNING - Test warning message\n";
    String result = formatter.formatWarning(warning);
    
    assertEquals(expected, result);
  }
  
  private class SourceExcerptProviderImpl implements SourceExcerptProvider {
    
    public String getSourceExcerpt(String filePath, int lineNumber) {
      return "Test source excerpt";
    }
    
    public String getOriginalSource() {
      return null;
    }
  }

}
