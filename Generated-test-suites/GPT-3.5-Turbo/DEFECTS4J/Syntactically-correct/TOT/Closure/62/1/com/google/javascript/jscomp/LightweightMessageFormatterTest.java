import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError_WithSource() {
    JSError error = mock(JSError.class);
    when(error.sourceName).thenReturn("test.js");
    when(error.lineNumber).thenReturn(5);
    when(error.description).thenReturn("This is an error");

    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.get(any(SourceExcerpt.class), eq("test.js"), eq(5), any(ExcerptFormatter.class)))
        .thenReturn("console.log('test');");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    String expectedResult = "test.js:5: ERROR - This is an error\n" +
        "console.log('test');\n";
    String actualResult = formatter.formatError(error);

    assertEquals(expectedResult, actualResult);

    verify(source).get(any(SourceExcerpt.class), eq("test.js"), eq(5), any(ExcerptFormatter.class));
  }
  
  @Test
  public void testFormatError_WithoutSource() {
    JSError error = mock(JSError.class);
    when(error.sourceName).thenReturn(null);
    when(error.lineNumber).thenReturn(0);
    when(error.description).thenReturn("This is an error");

    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    String expectedResult = "ERROR - This is an error\n";
    String actualResult = formatter.formatError(error);

    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testFormatWarning_WithSource() {
    JSError warning = mock(JSError.class);
    when(warning.sourceName).thenReturn("test.js");
    when(warning.lineNumber).thenReturn(10);
    when(warning.description).thenReturn("This is a warning");

    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.get(any(SourceExcerpt.class), eq("test.js"), eq(10), any(ExcerptFormatter.class)))
        .thenReturn("// Some comment");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

    String expectedResult = "test.js:10: WARNING - This is a warning\n" +
        "// Some comment\n";
    String actualResult = formatter.formatWarning(warning);

    assertEquals(expectedResult, actualResult);

    verify(source).get(any(SourceExcerpt.class), eq("test.js"), eq(10), any(ExcerptFormatter.class));
  }
  
  @Test
  public void testFormatWarning_WithoutSource() {
    JSError warning = mock(JSError.class);
    when(warning.sourceName).thenReturn(null);
    when(warning.lineNumber).thenReturn(0);
    when(warning.description).thenReturn("This is a warning");

    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    String expectedResult = "WARNING - This is a warning\n";
    String actualResult = formatter.formatWarning(warning);

    assertEquals(expectedResult, actualResult);
  }
}