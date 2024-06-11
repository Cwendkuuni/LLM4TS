import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider source;

  @Before
  public void setUp() {
    source = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(source);
  }

  @Test
  public void formatError_shouldReturnFormattedErrorMessage() {
    JSError error = new JSError("test_error", "Test error message");
    error.setSourceName("TestSource");
    error.setLineNumber(10);
    when(source.getSourceExcerpt("TestSource", 10)).thenReturn("Test excerpt");

    String result = formatter.formatError(error);

    assertEquals("TestSource:10: ERROR - Test error message\nTest excerpt\n", result);
  }

  @Test
  public void formatError_withoutSource_shouldReturnFormattedErrorMessageWithoutSource() {
    JSError error = new JSError("test_error", "Test error message");

    String result = formatter.formatError(error);

    assertEquals("ERROR - Test error message\n", result);
  }

  @Test
  public void formatWarning_shouldReturnFormattedWarningMessage() {
    JSError warning = new JSError("test_warning", "Test warning message");
    warning.setSourceName("TestSource");
    warning.setLineNumber(10);
    when(source.getSourceExcerpt("TestSource", 10)).thenReturn("Test excerpt");

    String result = formatter.formatWarning(warning);

    assertEquals("TestSource:10: WARNING - Test warning message\nTest excerpt\n", result);
  }

  @Test
  public void formatWarning_withoutSource_shouldReturnFormattedWarningMessageWithoutSource() {
    JSError warning = new JSError("test_warning", "Test warning message");

    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - Test warning message\n", result);
  }

  @Test
  public void format_shouldReturnFormattedErrorMessage() {
    JSError error = new JSError("test_error", "Test error message");
    error.setSourceName("TestSource");
    error.setLineNumber(10);
    when(source.getSourceExcerpt("TestSource", 10)).thenReturn("Test excerpt");

    String result = formatter.format(error);

    assertEquals("TestSource:10: ERROR - Test error message\nTest excerpt\n", result);
  }

  @Test
  public void format_withoutSource_shouldReturnFormattedErrorMessageWithoutSource() {
    JSError error = new JSError("test_error", "Test error message");

    String result = formatter.format(error);

    assertEquals("ERROR - Test error message\n", result);
  }

  @Test
  public void format_shouldReturnFormattedWarningMessage() {
    JSError warning = new JSError("test_warning", "Test warning message");
    warning.setSourceName("TestSource");
    warning.setLineNumber(10);
    when(source.getSourceExcerpt("TestSource", 10)).thenReturn("Test excerpt");

    String result = formatter.format(warning);

    assertEquals("TestSource:10: WARNING - Test warning message\nTest excerpt\n", result);
  }

  @Test
  public void format_withoutSource_shouldReturnFormattedWarningMessageWithoutSource() {
    JSError warning = new JSError("test_warning", "Test warning message");

    String result = formatter.format(warning);

    assertEquals("WARNING - Test warning message\n", result);
  }

  @Test
  public void formatLine_shouldReturnLineUnmodified() {
    String line = "Test line";
    int lineNumber = 1;

    String result = LightweightMessageFormatter.LineNumberingFormatter.formatLine(line, lineNumber);

    assertEquals("Test line", result);
  }

  @Test
  public void formatRegion_withNullRegion_shouldReturnNull() {
    String result = LightweightMessageFormatter.LineNumberingFormatter.formatRegion(null);

    assertEquals(null, result);
  }

  @Test
  public void formatRegion_withEmptyRegion_shouldReturnNull() {
    LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("", 1, 1);

    String result = LightweightMessageFormatter.LineNumberingFormatter.formatRegion(region);

    assertEquals(null, result);
  }

  @Test
  public void formatRegion_shouldReturnFormattedRegionWithLineNumbers() {
    LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("Test code\n", 1, 2);

    String result = LightweightMessageFormatter.LineNumberingFormatter.formatRegion(region);

    assertEquals("  1| Test code\n  2| ", result);
  }
}