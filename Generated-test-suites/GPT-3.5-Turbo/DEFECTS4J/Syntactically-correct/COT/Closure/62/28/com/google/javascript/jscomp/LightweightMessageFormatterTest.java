
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(null);
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("test error", "file.js", 10);
    String expected = "file.js:10: ERROR - test error\n";
    
    String result = formatter.formatError(error);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("test warning", "file.js", 5);
    String expected = "file.js:5: WARNING - test warning\n";
    
    String result = formatter.formatWarning(warning);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("test error", "file.js", 10);
    String expected = "ERROR - test error\n";
    
    String result = formatter.formatError(error);
    
    Assert.assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("test warning", "file.js", 5);
    String expected = "WARNING - test warning\n";
    
    String result = formatter.formatWarning(warning);
    
    Assert.assertEquals(expected, result);
  }
  
}
