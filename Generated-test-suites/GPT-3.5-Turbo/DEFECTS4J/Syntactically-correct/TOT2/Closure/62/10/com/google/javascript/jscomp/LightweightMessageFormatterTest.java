import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl());
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("error", "test.js", 10, CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    
    assertNotNull(formattedError);
    assertEquals("test.js:10: ERROR - error\n", formattedError);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("warning", "test.js", 5, CheckLevel.WARNING);
    String formattedWarning = formatter.formatWarning(warning);
    
    assertNotNull(formattedWarning);
    assertEquals("test.js:5: WARNING - warning\n", formattedWarning);
  }
  
  @Test
  public void testFormatErrorNullSource() {
    JSError error = new JSError("error", null, 0, CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    
    assertNotNull(formattedError);
    assertEquals("ERROR - error\n", formattedError);
  }
  
  @Test
  public void testFormatWithSourceExcerpt() {
    JSError error = new JSError("error", "test.js", 10, CheckLevel.ERROR);
    String formattedError = formatter.formatError(error);
    
    assertNotNull(formattedError);
    assertTrue(formattedError.contains("ERROR - error"));
    assertTrue(formattedError.contains("test.js:10"));
  }
  
  @Test
  public void testFormatWithEmptySourceExcerpt() {
    JSError error = new JSError("error", "test.js", 10, CheckLevel.ERROR);
    formatter = LightweightMessageFormatter.withoutSource();
    String formattedError = formatter.formatError(error);
    
    assertNotNull(formattedError);
    assertTrue(formattedError.contains("ERROR - error"));
    assertFalse(formattedError.contains("test.js:10"));
  }
  
  @Test
  public void testCharnoPadding() {
    JSError error = new JSError("error", "test.js", 10, CheckLevel.ERROR);
    error.setCharno(5);
    String formattedError = formatter.formatError(error);
    
    assertNotNull(formattedError);
    assertTrue(formattedError.contains("^"));
  }
  
  @Test
  public void testLineNumberingFormatter() {
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String formattedLine = formatter.formatLine("if (foo) {", 1);
    
    assertEquals("if (foo) {", formattedLine);
  }
  
  @Test
  public void testLineNumberingFormatterWithRegion() {
    Region region = new Region("if (foo) {\n alert('bar');\n }\n", 1, 3);
    LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
    String formattedRegion = formatter.formatRegion(region);
    
    assertNotNull(formattedRegion);
    assertTrue(formattedRegion.contains("1| if (foo) {"));
    assertTrue(formattedRegion.contains("2|  alert('bar');"));
    assertTrue(formattedRegion.contains("3|  }"));
  }

}