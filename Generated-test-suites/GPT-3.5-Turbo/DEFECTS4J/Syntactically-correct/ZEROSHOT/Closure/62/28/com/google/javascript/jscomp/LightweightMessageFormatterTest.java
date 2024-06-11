import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LightweightMessageFormatterTest {
  
  @Test
  public void testFormatError() {
    JSError error = mock(JSError.class);
    when(error.sourceName).thenReturn("test.js");
    when(error.lineNumber).thenReturn(10);
    when(error.description).thenReturn("Error");
    
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    SourceExcerpt excerpt = mock(SourceExcerpt.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
    
    String expected = "test.js:10: ERROR - Error\n";
    String actual = formatter.formatError(error);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testFormatWarning() {
    JSError warning = mock(JSError.class);
    when(warning.sourceName).thenReturn("test.js");
    when(warning.lineNumber).thenReturn(10);
    when(warning.description).thenReturn("Warning");
    
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    SourceExcerpt excerpt = mock(SourceExcerpt.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
    
    String expected = "test.js:10: WARNING - Warning\n";
    String actual = formatter.formatWarning(warning);
    
    assertEquals(expected, actual);
  }
  
  // Add more test methods here for other methods in the LightweightMessageFormatter class
  
}