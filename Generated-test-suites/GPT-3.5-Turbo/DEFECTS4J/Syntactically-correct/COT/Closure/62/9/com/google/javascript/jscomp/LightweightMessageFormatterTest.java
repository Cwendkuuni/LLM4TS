import org.junit.Assert;
import org.junit.Test;

public class LightweightMessageFormatterTest {
    
    @Test
    public void formatError_shouldReturnFormattedErrorString() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("Error message");
        
        String result = formatter.formatError(error);
        
        Assert.assertEquals("ERROR - Error message\n", result);
    }
    
    @Test
    public void formatWarning_shouldReturnFormattedWarningString() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("Warning message");
        
        String result = formatter.formatWarning(warning);
        
        Assert.assertEquals("WARNING - Warning message\n", result);
    }
    
    @Test
    public void formatError_withSource_shouldReturnFormattedErrorStringWithSource() {
        MockSourceExcerptProvider source = new MockSourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error message");
        error.setSourceName("test.js");
        error.setLineNumber(5);
        
        String result = formatter.formatError(error);
        
        Assert.assertEquals("test.js:5: ERROR - Error message\n", result);
    }
    
    @Test
    public void formatWarning_withSource_shouldReturnFormattedWarningStringWithSource() {
        MockSourceExcerptProvider source = new MockSourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning message");
        warning.setSourceName("test.js");
        warning.setLineNumber(5);
        
        String result = formatter.formatWarning(warning);
        
        Assert.assertEquals("test.js:5: WARNING - Warning message\n", result);
    }
    
    @Test
    public void formatError_withSourceAndExcerptFormatter_shouldReturnFormattedErrorStringWithFormattedSource() {
        MockSourceExcerptProvider source = new MockSourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error message");
        error.setSourceName("test.js");
        error.setLineNumber(5);
        source.setExcerpt("  function test() {\n    console.log('test');\n  }");
        
        String result = formatter.formatError(error);
        
        String expected = "test.js:5: ERROR - Error message\n"
            + "  function test() {\n    console.log('test');\n  }\n"
            + "    ^\n";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void formatWarning_withSourceAndExcerptFormatter_shouldReturnFormattedWarningStringWithFormattedSource() {
        MockSourceExcerptProvider source = new MockSourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning message");
        warning.setSourceName("test.js");
        warning.setLineNumber(5);
        source.setExcerpt("  function test() {\n    console.log('test');\n  }");
        
        String result = formatter.formatWarning(warning);
        
        String expected = "test.js:5: WARNING - Warning message\n"
            + "  function test() {\n    console.log('test');\n  }\n"
            + "    ^\n";
        Assert.assertEquals(expected, result);
    }
    
    // Add additional test cases for other methods if necessary
    
    
    // All test cases go here
    
    
}