import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LightweightMessageFormatterTest {
    @Mock
    private SourceExcerptProvider sourceExcerptProvider;

    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        sourceExcerptProvider = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(sourceExcerptProvider);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("Test error", "Test.js", 5);
        when(sourceExcerptProvider.getSourceExcerpt(error.sourceName, error.lineNumber)).thenReturn("Code excerpt");
        
        String expectedResult = "Test.js:5: ERROR - Test error\nCode excerpt\n";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test warning", "Test.js", 10);
        when(sourceExcerptProvider.getSourceExcerpt(warning.sourceName, warning.lineNumber)).thenReturn("Code excerpt");
        
        String expectedResult = "Test.js:10: WARNING - Test warning\nCode excerpt\n";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test error", null, 0);
        
        String expectedResult = "ERROR - Test error\n";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Test warning", null, 0);
        
        String expectedResult = "WARNING - Test warning\n";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatErrorWithoutExcerpt() {
        JSError error = new JSError("Test error", "Test.js", 5);
        when(sourceExcerptProvider.getSourceExcerpt(error.sourceName, error.lineNumber)).thenReturn(null);
        
        String expectedResult = "Test.js:5: ERROR - Test error\n";
        String actualResult = formatter.formatError(error);
        
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarningWithoutExcerpt() {
        JSError warning = new JSError("Test warning", "Test.js", 10);
        when(sourceExcerptProvider.getSourceExcerpt(warning.sourceName, warning.lineNumber)).thenReturn(null);
        
        String expectedResult = "Test.js:10: WARNING - Test warning\n";
        String actualResult = formatter.formatWarning(warning);
        
        assertEquals(expectedResult, actualResult);
    }
}