import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider source;

  @Before
  public void setUp() {
    source = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(source);
  }

  @Test
  public void testFormatError_withSource() {
    // Arrange
    JSError error = new JSError("Error description", "source.js", 10);
    when(source.getSourceExcerpt(any(), anyString(), anyInt(), any()))
        .thenReturn("This is the source excerpt");

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("source.js:10: ERROR - Error description\n" +
        "This is the source excerpt\n", result);
    verify(source, times(1)).getSourceExcerpt(any(), eq("source.js"), eq(10), any());
  }

  @Test
  public void testFormatError_withoutSource() {
    // Arrange
    JSError error = new JSError("Error description", null, 0);

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("ERROR - Error description\n", result);
    verifyZeroInteractions(source);
  }

  @Test
  public void testFormatWarning_withSource() {
    // Arrange
    JSError warning = new JSError("Warning description", "source.js", 5);
    when(source.getSourceExcerpt(any(), anyString(), anyInt(), any()))
        .thenReturn("This is the source excerpt");

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    assertEquals("source.js:5: WARNING - Warning description\n" +
        "This is the source excerpt\n", result);
    verify(source, times(1)).getSourceExcerpt(any(), eq("source.js"), eq(5), any());
  }

  @Test
  public void testFormatWarning_withoutSource() {
    // Arrange
    JSError warning = new JSError("Warning description", null, 0);

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    assertEquals("WARNING - Warning description\n", result);
    verifyZeroInteractions(source);
  }

  @Test
  public void testFormatInvalidError_withSource() {
    // Arrange
    JSError error = new JSError(null, "source.js", 5);
    when(source.getSourceExcerpt(any(), anyString(), anyInt(), any()))
        .thenReturn("This is the source excerpt");

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("source.js:5: ERROR - \n" +
        "This is the source excerpt\n", result);
    verify(source, times(1)).getSourceExcerpt(any(), eq("source.js"), eq(5), any());
  }

  @Test
  public void testFormatInvalidWarning_withSource() {
    // Arrange
    JSError warning = new JSError(null, "source.js", 10);
    when(source.getSourceExcerpt(any(), anyString(), anyInt(), any()))
        .thenReturn("This is the source excerpt");

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    assertEquals("source.js:10: WARNING - \n" +
        "This is the source excerpt\n", result);
    verify(source, times(1)).getSourceExcerpt(any(), eq("source.js"), eq(10), any());
  }

  @Test
  public void testFormatInvalidError_withoutSource() {
    // Arrange
    JSError error = new JSError(null, null, 0);

    // Act
    String result = formatter.formatError(error);

    // Assert
    assertEquals("ERROR - \n", result);
    verifyZeroInteractions(source);
  }

  @Test
  public void testFormatInvalidWarning_withoutSource() {
    // Arrange
    JSError warning = new JSError(null, null, 0);

    // Act
    String result = formatter.formatWarning(warning);

    // Assert
    assertEquals("WARNING - \n", result);
    verifyZeroInteractions(source);
  }
}