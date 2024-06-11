import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider source;
  private ExcerptFormatter excerptFormatter;

  @Before
  public void setUp() {
    source = mock(SourceExcerptProvider.class);
    excerptFormatter = mock(ExcerptFormatter.class);
    formatter = new LightweightMessageFormatter(source);
  }

  @Test
  public void testFormatError_withSource() {
    String expected = "example.js:5: ERROR - This is an error message\n";
    JSError error = new JSError("example.js", 5, CheckLevel.ERROR, "This is an error message");

    when(source.get(error.sourceName)).thenReturn("source code");
    when(excerptFormatter.formatLine("source code", 5)).thenReturn("source code");
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.formatError(error);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatError_withoutSource() {
    String expected = "ERROR - This is an error message\n";
    JSError error = new JSError(null, 0, CheckLevel.ERROR, "This is an error message");

    when(source.get(error.sourceName)).thenReturn(null);
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.formatError(error);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning_withSource() {
    String expected = "example.js:10: WARNING - This is a warning message\n";
    JSError warning = new JSError("example.js", 10, CheckLevel.WARNING, "This is a warning message");

    when(source.get(warning.sourceName)).thenReturn("source code");
    when(excerptFormatter.formatLine("source code", 10)).thenReturn("source code");
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.formatWarning(warning);

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning_withoutSource() {
    String expected = "WARNING - This is a warning message\n";
    JSError warning = new JSError(null, 0, CheckLevel.WARNING, "This is a warning message");

    when(source.get(warning.sourceName)).thenReturn(null);
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.formatWarning(warning);

    assertEquals(expected, result);
  }

  @Test
  public void testFormat_withErrorAndSourceExcerpt() {
    String expected = "example.js:5: ERROR - This is an error message\n";
    JSError error = new JSError("example.js", 5, CheckLevel.ERROR, "This is an error message");

    when(source.get(error.sourceName)).thenReturn("source code");
    when(excerptFormatter.formatLine("source code", 5)).thenReturn("source code");
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.format(error, false);

    assertEquals(expected, result);
  }

  @Test
  public void testFormat_withErrorAndNullSourceExcerpt() {
    String expected = "example.js:5: ERROR - This is an error message\n";
    JSError error = new JSError("example.js", 5, CheckLevel.ERROR, "This is an error message");

    when(source.get(error.sourceName)).thenReturn(null);
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.format(error, false);

    assertEquals(expected, result);
  }

  @Test
  public void testFormat_withWarningAndSourceExcerpt() {
    String expected = "example.js:10: WARNING - This is a warning message\n";
    JSError warning = new JSError("example.js", 10, CheckLevel.WARNING, "This is a warning message");

    when(source.get(warning.sourceName)).thenReturn("source code");
    when(excerptFormatter.formatLine("source code", 10)).thenReturn("source code");
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.format(warning, true);

    assertEquals(expected, result);
  }

  @Test
  public void testFormat_withWarningAndNullSourceExcerpt() {
    String expected = "example.js:10: WARNING - This is a warning message\n";
    JSError warning = new JSError("example.js", 10, CheckLevel.WARNING, "This is a warning message");

    when(source.get(warning.sourceName)).thenReturn(null);
    when(excerptFormatter.formatRegion(null)).thenReturn(null);

    String result = formatter.format(warning, true);

    assertEquals(expected, result);
  }
}