import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

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
    JSError error = new JSError("Test Error", "TestFileName", 10, "description");
    String expectedOutput = "TestFileName:10: ERROR - Test Error\n";
    
    String actualOutput = formatter.formatError(error);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Warning", "TestFileName", 5, "description");
    String expectedOutput = "TestFileName:5: WARNING - Test Warning\n";
    
    String actualOutput = formatter.formatWarning(warning);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
}