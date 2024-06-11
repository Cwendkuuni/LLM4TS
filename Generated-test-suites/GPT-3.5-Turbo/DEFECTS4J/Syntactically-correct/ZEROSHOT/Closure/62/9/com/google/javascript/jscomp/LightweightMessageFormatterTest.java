import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {
    
    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("Error description");
        String result = formatter.formatError(error);
        String expected = "ERROR - Error description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("Warning description");
        String result = formatter.formatWarning(warning);
        String expected = "WARNING - Warning description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description");
        error.setSourceName("filename.js");
        error.setLineNumber(1);
        String result = formatter.formatError(error);
        String expected = "filename.js:1: ERROR - Error description\n";
        assertEquals(expected, result);
    }
    
    @Test
    public void testFormatWarningWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description");
        warning.setSourceName("filename.js");
        warning.setLineNumber(1);
        String result = formatter.formatWarning(warning);
        String expected = "filename.js:1: WARNING - Warning description\n";
        assertEquals(expected, result);
    }
}