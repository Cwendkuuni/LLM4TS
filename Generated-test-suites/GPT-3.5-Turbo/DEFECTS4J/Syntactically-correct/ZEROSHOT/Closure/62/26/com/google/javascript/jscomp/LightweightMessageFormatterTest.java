import org.junit.Test;

import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        // Create a sample JSError object
        JSError error = new JSError("error description", "file.js", 10);
        
        // Create a LightweightMessageFormatter object
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        
        // Format the error using formatError method
        String result = formatter.formatError(error);
        
        // Verify that the error message format is correct
        String expected = "file.js:10: ERROR - error description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWarning() {
        // Create a sample JSError object
        JSError warning = new JSError("warning description", "file.js", 5);
        
        // Create a LightweightMessageFormatter object
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        
        // Format the warning using formatWarning method
        String result = formatter.formatWarning(warning);
        
        // Verify that the warning message format is correct
        String expected = "file.js:5: WARNING - warning description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatErrorWithoutSource() {
        // Create a sample JSError object
        JSError error = new JSError("error description", "file.js", 10);
        
        // Create a LightweightMessageFormatter object without source
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        
        // Format the error using formatError method
        String result = formatter.formatError(error);
        
        // Verify that the error message format is correct
        String expected = "ERROR - error description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWarningWithoutSource() {
        // Create a sample JSError object
        JSError warning = new JSError("warning description", "file.js", 5);
        
        // Create a LightweightMessageFormatter object without source
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        
        // Format the warning using formatWarning method
        String result = formatter.formatWarning(warning);
        
        // Verify that the warning message format is correct
        String expected = "WARNING - warning description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatErrorWithSource() {
        // Create a sample JSError object
        JSError error = new JSError("error description", "file.js", 10);
        
        // Create a dummy SourceExcerptProvider
        SourceExcerptProvider source = new DummySourceExcerptProvider();
        
        // Create a LightweightMessageFormatter object with source
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        
        // Format the error using formatError method
        String result = formatter.formatError(error);
        
        // Verify that the error message format is correct
        String expected = "file.js:10: ERROR - error description\nsource excerpt\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWarningWithSource() {
        // Create a sample JSError object
        JSError warning = new JSError("warning description", "file.js", 5);
        
        // Create a dummy SourceExcerptProvider
        SourceExcerptProvider source = new DummySourceExcerptProvider();
        
        // Create a LightweightMessageFormatter object with source
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        
        // Format the warning using formatWarning method
        String result = formatter.formatWarning(warning);
        
        // Verify that the warning message format is correct
        String expected = "file.js:5: WARNING - warning description\nsource excerpt\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatErrorWithExcerpt() {
        // Create a sample JSError object
        JSError error = new JSError("error description", "file.js", 10);
        
        // Create a dummy SourceExcerptProvider
        SourceExcerptProvider source = new DummySourceExcerptProvider();
        
        // Create a LightweightMessageFormatter object with excerpt
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        
        // Format the error using formatError method
        String result = formatter.formatError(error);
        
        // Verify that the error message format is correct
        String expected = "file.js:10: ERROR - error description\nsource excerpt\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWarningWithExcerpt() {
        // Create a sample JSError object
        JSError warning = new JSError("warning description", "file.js", 5);
        
        // Create a dummy SourceExcerptProvider
        SourceExcerptProvider source = new DummySourceExcerptProvider();
        
        // Create a LightweightMessageFormatter object with excerpt
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        
        // Format the warning using formatWarning method
        String result = formatter.formatWarning(warning);
        
        // Verify that the warning message format is correct
        String expected = "file.js:5: WARNING - warning description\nsource excerpt\n";
        assertEquals(expected, result);
    }
    
    private class DummySourceExcerptProvider implements SourceExcerptProvider {
        
        public SourceExcerpt get(Source source, String sourceName, int lineNumber, ExcerptFormatter formatter) {
            return SourceExcerpt.LINE;
        }
    }
}