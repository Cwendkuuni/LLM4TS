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
    JSError error = new JSError("error", "source", 1, "description");
    String expectedResult = "source:1: ERROR - description\n";
    String actualResult = formatter.formatError(error);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("warning", "source", 1, "description");
    String expectedResult = "source:1: WARNING - description\n";
    String actualResult = formatter.formatWarning(warning);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("error", "source", 1, "description");
    String expectedResult = "ERROR - description\n";
    String actualResult = formatter.formatError(error);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("warning", "source", 1, "description");
    String expectedResult = "WARNING - description\n";
    String actualResult = formatter.formatWarning(warning);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatErrorWithSourceExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("error", "source", 1, "description");
    String expectedSourceExcerpt = "source excerpt";
    source.setSourceExcerpt("source", expectedSourceExcerpt);
    String expectedResult = "source:1: ERROR - description\nsource excerpt\n";
    String actualResult = formatter.formatError(error);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatWarningWithSourceExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("warning", "source", 1, "description");
    String expectedSourceExcerpt = "source excerpt";
    source.setSourceExcerpt("source", expectedSourceExcerpt);
    String expectedResult = "source:1: WARNING - description\nsource excerpt\n";
    String actualResult = formatter.formatWarning(warning);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatErrorWithSourceExcerptAndCharno() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("error", "source", 1, "description");
    String expectedSourceExcerpt = "source excerpt";
    source.setSourceExcerpt("source", expectedSourceExcerpt);
    error.setCharno(5);
    String expectedResult = "source:1: ERROR - description\nsource excerpt\n     ^\n";
    String actualResult = formatter.formatError(error);
    assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFormatWarningWithSourceExcerptAndCharno() {
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("warning", "source", 1, "description");
    String expectedSourceExcerpt = "source excerpt";
    source.setSourceExcerpt("source", expectedSourceExcerpt);
    warning.setCharno(5);
    String expectedResult = "source:1: WARNING - description\nsource excerpt\n     ^\n";
    String actualResult = formatter.formatWarning(warning);
    assertEquals(expectedResult, actualResult);
  }
}