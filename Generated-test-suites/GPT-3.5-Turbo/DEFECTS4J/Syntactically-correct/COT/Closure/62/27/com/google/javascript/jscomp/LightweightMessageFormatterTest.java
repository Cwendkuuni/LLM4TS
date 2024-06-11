import static org.junit.Assert.*;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    
    @Test
    public void testFormatError() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider());
        
        // Create a JSError object with dummy values
        JSError error = new JSError("ERROR", "source.js", 1, "error description");
        
        // Test the formatError() method
        String formattedError = formatter.formatError(error);
        assertEquals("source.js:1: ERROR - error description\n", formattedError);
    }
    
    @Test
    public void testFormatWarning() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider());
        
        // Create a JSError object with dummy values
        JSError warning = new JSError("WARNING", "source.js", 1, "warning description");
        
        // Test the formatWarning() method
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - warning description\n", formattedWarning);
    }
    
    // Dummy implementation of the SourceExcerptProvider interface for testing purposes
    private class DummySourceExcerptProvider implements SourceExcerptProvider {
        @Override
        public String getSourceExcerpt(String sourceName, int lineNumber) {
            return null;
        }
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testFormatErrorWithoutSource() {
        // Create a LightweightMessageFormatter object without a SourceExcerptProvider
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        
        // Create a JSError object with dummy values
        JSError error = new JSError("ERROR", "source.js", 1, "error description");
        
        // Test the formatError() method
        String formattedError = formatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutSourceNullCheck() {
        // Try to create a LightweightMessageFormatter object without passing a SourceExcerptProvider
        LightweightMessageFormatter.withoutSource();
    }
    
    @Test
    public void testFormatWarningWithSourceExcerpt() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider(), LINE);
        
        // Create a JSError object with dummy values and a valid source excerpt
        JSError warning = new JSError("WARNING", "source.js", 1, "warning description");
        
        // Test the formatWarning() method
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - warning description\n", formattedWarning);
    }
    
    @Test
    public void testFormatWarningWithInvalidSourceExcerpt() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider(), LINE);
        
        // Create a JSError object with dummy values and an invalid source excerpt
        JSError warning = new JSError("WARNING", "source.js", 1, "warning description");
        
        // Test the formatWarning() method
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - warning description\n", formattedWarning);
    }
    
    // Complete JUnit test file for the LightweightMessageFormatter class
    
    @Test
    public void testFormatError() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider());
        
        // Create a JSError object with dummy values
        JSError error = new JSError("ERROR", "source.js", 1, "error description");
        
        // Test the formatError() method
        String formattedError = formatter.formatError(error);
        assertEquals("source.js:1: ERROR - error description\n", formattedError);
    }
    
    @Test
    public void testFormatWarning() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider());
        
        // Create a JSError object with dummy values
        JSError warning = new JSError("WARNING", "source.js", 1, "warning description");
        
        // Test the formatWarning() method
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - warning description\n", formattedWarning);
    }
    
    // Dummy implementation of the SourceExcerptProvider interface for testing purposes
    private class DummySourceExcerptProvider implements SourceExcerptProvider {
        @Override
        public String getSourceExcerpt(String sourceName, int lineNumber) {
            return null;
        }
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testFormatErrorWithoutSource() {
        // Create a LightweightMessageFormatter object without a SourceExcerptProvider
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        
        // Create a JSError object with dummy values
        JSError error = new JSError("ERROR", "source.js", 1, "error description");
        
        // Test the formatError() method
        String formattedError = formatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorWithoutSourceNullCheck() {
        // Try to create a LightweightMessageFormatter object without passing a SourceExcerptProvider
        LightweightMessageFormatter.withoutSource();
    }
    
    @Test
    public void testFormatWarningWithSourceExcerpt() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider(), LINE);
        
        // Create a JSError object with dummy values and a valid source excerpt
        JSError warning = new JSError("WARNING", "source.js", 1, "warning description");
        
        // Test the formatWarning() method
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - warning description\n", formattedWarning);
    }
    
    @Test
    public void testFormatWarningWithInvalidSourceExcerpt() {
        // Create a LightweightMessageFormatter object with a dummy SourceExcerptProvider
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider(), LINE);
        
        // Create a JSError object with dummy values and an invalid source excerpt
        JSError warning = new JSError("WARNING", "source.js", 1, "warning description");
        
        // Test the formatWarning() method
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - warning description\n", formattedWarning);
    }
    
}