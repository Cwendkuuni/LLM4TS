import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.testing.TestingUtils;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatterWithNullSource() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError("Test error", "Test source", 1, 1);
    String expectedMessage = "ERROR - Test error\n";
    String actualMessage = formatter.formatError(error);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testLightweightMessageFormatterWithSource() {
    SourceExcerptProvider sourceMock = mock(SourceExcerptProvider.class);
    when(sourceMock.getSourceExcerpt(TestingUtils.createRegion(1, 1, "Test source", " Test code"))).thenReturn("Test code");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceMock, LIGHTWEIGHT_MESSAGE_FORMATTER_LINE_NUMBERING_FORMATTER);
    JSError error = new JSError("Test error", "Test source", 1, 1);
    String expectedMessage = "Test source:1: Test error\nTest code\n";
    String actualMessage = formatter.formatError(error);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testLightweightMessageFormatterWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Test error", null, 1, 1);
    String expectedMessage = "ERROR - Test error\n";
    String actualMessage = formatter.formatError(error);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testLightweightMessageFormatterWithWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(TestingUtils.createSourceExcerptProvider(), LIGHTWEIGHT_MESSAGE_FORMATTER_LINE_NUMBERING_FORMATTER);
    JSError warning = new JSError("Test warning", "Test source", 1, 1);
    String expectedMessage = "WARNING - Test warning\nTest source:1: Test warning\n";
    String actualMessage = formatter.formatWarning(warning);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  public void testFormatErrorWithExcerptLongerThanLine() {
    SourceExcerptProvider sourceMock = mock(SourceExcerptProvider.class);
    Region regionMock = mock(Region.class);
    when(regionMock.getBeginningLineNumber()).thenReturn(1);
    when(regionMock.getEndingLineNumber()).thenReturn(2);
    when(regionMock.getSourceExcerpt()).thenReturn("Test source\nTest code\nTest code 2");
    when(sourceMock.getSourceExcerpt(regionMock)).thenReturn(regionMock.getSourceExcerpt());

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceMock, LIGHTWEIGHT_MESSAGE_FORMATTER_LINE_NUMBERING_FORMATTER);
    JSError error = new JSError("Test error", "Test source", 1, 2);
    String expectedMessage = "Test source:1: Test error\nTest source\n Test code\n Test code 2\n";
    String actualMessage = formatter.formatError(error);
    assertEquals(expectedMessage, actualMessage);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(TestingUtils.createSourceExcerptProvider(), LIGHTWEIGHT_MESSAGE_FORMATTER_LINE_NUMBERING_FORMATTER);
    formatter.formatError(null);
  }

  private static final LightweightMessageFormatter LIGHTWEIGHT_MESSAGE_FORMATTER_LINE_NUMBERING_FORMATTER = new LineNumberingFormatter();
}