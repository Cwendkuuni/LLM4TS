import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl());
  }
  
  @Test
  public void formatError_withValidError_ShouldReturnFormattedErrorString() {
    JSError error = new JSError("Error description", "SourceName", 10);
    String expected = "SourceName:10: ERROR - Error description\n";
    
    String result = formatter.formatError(error);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatError_withValidErrorAndSource_ShouldReturnFormattedErrorStringWithSourceExcerpt() {
    JSError error = new JSError("Error description", "SourceName", 10);
    String expected = "SourceName:10: ERROR - Error description\n" +
      "  9| if (foo) {\n" +
      " 10|   alert('bar');\n" +
      " 11| }\n" +
      "        ^\n";
      
    String result = formatter.formatError(error);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatError_withNullSourceName_ShouldReturnFormattedErrorStringWithoutSourceName() {
    JSError error = new JSError("Error description", null, 10);
    String expected = "ERROR - Error description\n";
    
    String result = formatter.formatError(error);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatError_withNegativeLineNumber_ShouldReturnFormattedErrorStringWithoutLineNumber() {
    JSError error = new JSError("Error description", "SourceName", -1);
    String expected = "SourceName: ERROR - Error description\n";
    
    String result = formatter.formatError(error);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatWarning_withValidWarning_ShouldReturnFormattedWarningString() {
    JSError warning = new JSError("Warning description", "SourceName", 10);
    String expected = "SourceName:10: WARNING - Warning description\n";
    
    String result = formatter.formatWarning(warning);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatWarning_withValidWarningAndSource_ShouldReturnFormattedWarningStringWithSourceExcerpt() {
    JSError warning = new JSError("Warning description", "SourceName", 10);
    String expected = "SourceName:10: WARNING - Warning description\n" +
      "  9| if (foo) {\n" +
      " 10|   alert('bar');\n" +
      " 11| }\n" +
      "        ^\n";
      
    String result = formatter.formatWarning(warning);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatWarning_withNullSourceName_ShouldReturnFormattedWarningStringWithoutSourceName() {
    JSError warning = new JSError("Warning description", null, 10);
    String expected = "WARNING - Warning description\n";
    
    String result = formatter.formatWarning(warning);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void formatWarning_withNegativeLineNumber_ShouldReturnFormattedWarningStringWithoutLineNumber() {
    JSError warning = new JSError("Warning description", "SourceName", -1);
    String expected = "SourceName: WARNING - Warning description\n";
    
    String result = formatter.formatWarning(warning);
    
    assertEquals(expected, result);
  }
  
}