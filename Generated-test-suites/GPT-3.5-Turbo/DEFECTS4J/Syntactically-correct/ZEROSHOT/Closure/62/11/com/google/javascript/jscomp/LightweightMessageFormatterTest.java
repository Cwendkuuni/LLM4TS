import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter();
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("Test error message");
    String formattedError = formatter.formatError(error);
    
    String expectedOutput = "ERROR - Test error message\n";
    
    assertEquals(expectedOutput, formattedError);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test warning message");
    String formattedWarning = formatter.formatWarning(warning);
    
    String expectedOutput = "WARNING - Test warning message\n";
    
    assertEquals(expectedOutput, formattedWarning);
  }
  
  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    JSError error = new JSError("Test error message", source, "TestFile.js", 10);
    String formattedError = formatter.formatError(error);
    
    String expectedOutput = "TestFile.js:10: ERROR - Test error message\n";
    
    assertEquals(expectedOutput, formattedError);
  }
  
  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    JSError warning = new JSError("Test warning message", source, "TestFile.js", 10);
    String formattedWarning = formatter.formatWarning(warning);
    
    String expectedOutput = "TestFile.js:10: WARNING - Test warning message\n";
    
    assertEquals(expectedOutput, formattedWarning);
  }
  
  @Test
  public void testFormatErrorWithSourceAndExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    JSError error = new JSError("Test error message", source, "TestFile.js", 10);
    error.setCharno(5);
    String formattedError = formatter.formatError(error);
    
    String expectedOutput = "TestFile.js:10: ERROR - Test error message\n\n     ^\n";
    
    assertEquals(expectedOutput, formattedError);
  }
  
  @Test
  public void testFormatWarningWithSourceAndExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    JSError warning = new JSError("Test warning message", source, "TestFile.js", 10);
    warning.setCharno(5);
    String formattedWarning = formatter.formatWarning(warning);
    
    String expectedOutput = "TestFile.js:10: WARNING - Test warning message\n\n     ^\n";
    
    assertEquals(expectedOutput, formattedWarning);
  }
  
  @Test
  public void testFormatRegion() {
    SourceExcerptProvider.ExcerptFormatter formatter = new SourceExcerptProvider.LineNumberingFormatter();
    SourceExcerptProvider.Region region = new SourceExcerptProvider.Region("test region", 5, 10);
    String formattedRegion = formatter.formatRegion(region);
    
    String expectedOutput = "  5| test region\n  6| \n  7| \n  8| \n  9| \n 10| \n";
    
    assertEquals(expectedOutput, formattedRegion);
  }
  
}