import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.*;

public class LightweightMessageFormatterTest {
  
  private SourceExcerptProvider mockSourceExcerptProvider;
  private LightweightMessageFormatter formatter;
  
  @Before
  public void setUp() {
    mockSourceExcerptProvider = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(mockSourceExcerptProvider, SourceExcerpt.LINE);
  }
  
  @Test
  public void testFormatError_WithSourceInformation_ShouldReturnFormattedErrorString() {
    // Arrange
    JSError error = new JSError("message", "source.js", 10, 5);
    when(mockSourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("source excerpt");
    
    // Act
    String result = formatter.formatError(error);
    
    // Assert
    String expected = "source.js:10: ERROR - message\nsource excerpt\n     ^\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatError_WithoutSourceInformation_ShouldReturnFormattedErrorString() {
    // Arrange
    JSError error = new JSError("message");
    
    // Act
    String result = formatter.formatError(error);
    
    // Assert
    String expected = "ERROR - message\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning_WithSourceInformation_ShouldReturnFormattedWarningString() {
    // Arrange
    JSError warning = new JSError("message", "source.js", 10, 5);
    when(mockSourceExcerptProvider.get(any(), any(), anyInt(), any())).thenReturn("source excerpt");
    
    // Act
    String result = formatter.formatWarning(warning);
    
    // Assert
    String expected = "source.js:10: WARNING - message\nsource excerpt\n     ^\n";
    assertEquals(expected, result);
  }
  
  @Test
  public void testFormatWarning_WithoutSourceInformation_ShouldReturnFormattedWarningString() {
    // Arrange
    JSError warning = new JSError("message");
    
    // Act
    String result = formatter.formatWarning(warning);
    
    // Assert
    String expected = "WARNING - message\n";
    assertEquals(expected, result);
  }
}