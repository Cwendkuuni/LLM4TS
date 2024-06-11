import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {

  @Mock
  private SourceExcerptProvider sourceExcerptProvider;

  private LightweightMessageFormatter formatter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    formatter = new LightweightMessageFormatter(sourceExcerptProvider);
  }

  @Test
  public void formatError_withSourceExcerpt_shouldReturnFormattedErrorString() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, CheckLevel.ERROR);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("if (foo) {}");

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - error description\nif (foo) {}\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatError_withoutSourceExcerpt_shouldReturnFormattedErrorStringWithoutSourceExcerpt() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 10, CheckLevel.ERROR);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn(null);

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:10: ERROR - error description\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatError_withWarning_shouldReturnFormattedWarningString() {
    // Arrange
    JSError warning = new JSError("warning description", "test.js", 5, CheckLevel.WARNING);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("alert('bar');");

    // Act
    String result = formatter.formatError(warning);

    // Assert
    String expected = "test.js:5: WARNING - warning description\nalert('bar');\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatError_withNullSourceNameAndLineNumber_shouldReturnFormattedErrorStringWithoutSourceNameAndLineNumber() {
    // Arrange
    JSError error = new JSError("error description", null, -1, CheckLevel.ERROR);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("if (foo) {}");

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "ERROR - error description\nif (foo) {}\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatWarning_withNullSourceNameAndLineNumber_shouldReturnFormattedWarningStringWithoutSourceNameAndLineNumber() {
    // Arrange
    JSError warning = new JSError("warning description", null, -1, CheckLevel.WARNING);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("alert('bar');");

    // Act
    String result = formatter.formatError(warning);

    // Assert
    String expected = "WARNING - warning description\nalert('bar');\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatWarning_withEmptySourceExcerpt_shouldReturnFormattedWarningStringWithoutSourceExcerpt() {
    // Arrange
    JSError warning = new JSError("warning description", "test.js", 5, CheckLevel.WARNING);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("");

    // Act
    String result = formatter.formatError(warning);

    // Assert
    String expected = "test.js:5: WARNING - warning description\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatError_withCharNoSmallerThanExcerptLength_shouldNotAddArrow() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 2, CheckLevel.ERROR);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("if (foo) {}");

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:2: ERROR - error description\nif (foo) {}\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatError_withCharNoLargerThanOrEqualToExcerptLength_shouldNotAddArrow() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 2, CheckLevel.ERROR);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("if (foo) {}");

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:2: ERROR - error description\nif (foo) {}\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatError_withCharNoInRangeOfExcerptLength_shouldAddArrowAtCharNoPosition() {
    // Arrange
    JSError error = new JSError("error description", "test.js", 2, CheckLevel.ERROR);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("if (foo) {}");

    // Act
    String result = formatter.formatError(error);

    // Assert
    String expected = "test.js:2: ERROR - error description\nif (foo) {}\n" +
                      "   ^\n";
    assertEquals(expected, result);
  }

  @Test
  public void formatWarning_withSourceExcerptEqualToExcerpt_shouldAddArrowAtCharNoPosition() {
    // Arrange
    JSError warning = new JSError("warning description", "test.js", 5, CheckLevel.WARNING);

    when(sourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("alert('bar');");

    // Act
    String result = formatter.formatError(warning);

    // Assert
    String expected = "test.js:5: WARNING - warning description\nalert('bar');\n" +
                      "                ^\n";
    assertEquals(expected, result);
  }
}