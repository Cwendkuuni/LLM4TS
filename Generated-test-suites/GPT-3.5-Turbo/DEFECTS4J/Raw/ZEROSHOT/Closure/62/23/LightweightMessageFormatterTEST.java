import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
  
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setup() {
    formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider());
  }
  
  @Test
  public void testFormatError() {
    JSError error = new JSError("Test Error", "test.js", 10);
    String result = formatter.formatError(error);
    String expected = "test.js:10: ERROR - Test Error\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("Test Warning", "test.js", 5);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:5: WARNING - Test Warning\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatErrorWithoutSourceName() {
    JSError error = new JSError("Test Error", null, 0);
    String result = formatter.formatError(error);
    String expected = "ERROR - Test Error\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarningWithoutSourceName() {
    JSError warning = new JSError("Test Warning", null, 0);
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Test Warning\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatErrorWithSourceExcerpt() {
    JSError error = new JSError("Test Error", "test.js", 10);
    DummyRegion region = new DummyRegion("Test Code");
    DummySourceExcerptProvider source = new DummySourceExcerptProvider(region);
    formatter = new LightweightMessageFormatter(source);
    String result = formatter.formatError(error);
    String expected = "test.js:10: ERROR - Test Error\nTest Code\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarningWithSourceExcerpt() {
    JSError warning = new JSError("Test Warning", "test.js", 5);
    DummyRegion region = new DummyRegion("Test Code");
    DummySourceExcerptProvider source = new DummySourceExcerptProvider(region);
    formatter = new LightweightMessageFormatter(source);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:5: WARNING - Test Warning\nTest Code\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatErrorWithCharno() {
    JSError error = new JSError("Test Error", "test.js", 10);
    DummyRegion region = new DummyRegion("Test Code");
    DummySourceExcerptProvider source = new DummySourceExcerptProvider(region);
    formatter = new LightweightMessageFormatter(source);
    error.setCharno(5);
    String result = formatter.formatError(error);
    String expected = "test.js:10: ERROR - Test Error\nTest Code\n     ^\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarningWithCharno() {
    JSError warning = new JSError("Test Warning", "test.js", 5);
    DummyRegion region = new DummyRegion("Test Code");
    DummySourceExcerptProvider source = new DummySourceExcerptProvider(region);
    formatter = new LightweightMessageFormatter(source);
    warning.setCharno(2);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:5: WARNING - Test Warning\nTest Code\n  ^\n";
    assertEquals(expected, result);
  }
  
  private static class DummySourceExcerptProvider implements SourceExcerptProvider {
    
    private Region region;
    
    public DummySourceExcerptProvider(Region region) {
      this.region = region;
    }
    
    public SourceExcerpt get(String sourceName, int lineNumber) {
      return new DummySourceExcerpt();
    }
    
    private class DummySourceExcerpt implements SourceExcerpt {
      
      public String get(String sourceName, int lineNumber, ExcerptFormatter formatter) {
        return region.getSourceExcerpt();
      }
    }
  }
  
  private static class DummyRegion implements Region {
    
    private String sourceExcerpt;
    
    public DummyRegion(String sourceExcerpt) {
      this.sourceExcerpt = sourceExcerpt;
    }
    
    public String getSourceExcerpt() {
      return sourceExcerpt;
    }
    
    public int getBeginningLineNumber() {
      return 1;
    }
    
    public int getEndingLineNumber() {
      return sourceExcerpt.split("\n").length;
    }
  }
}