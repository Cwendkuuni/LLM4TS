import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {
    
    @Test
    public void testConstructorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        assertNotNull(formatter);
    }
    
    @Test
    public void testConstructorWithSourceAndExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = new SourceExcerpt();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        assertNotNull(formatter);
    }
    
    @Test
    public void testFormatError() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description");
        String result = formatter.formatError(error);
        assertEquals("CheckLevel.ERROR - Error description\n", result);
    }
    
    @Test
    public void testFormatWarning() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description");
        String result = formatter.formatWarning(warning);
        assertEquals("CheckLevel.WARNING - Warning description\n", result);
    }
    
    @Test
    public void testFormatErrorWithoutSourceNameAndLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description");
        String result = formatter.formatError(error);
        assertEquals("CheckLevel.ERROR - Error description\n", result);
    }
    
    @Test
    public void testFormatErrorWithSourceNameWithoutLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description", "file.js");
        String result = formatter.formatError(error);
        assertEquals("file.js: CheckLevel.ERROR - Error description\n", result);
    }
    
    @Test
    public void testFormatErrorWithSourceNameAndLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description", "file.js", 10);
        String result = formatter.formatError(error);
        assertEquals("file.js:10: CheckLevel.ERROR - Error description\n", result);
    }
    
    @Test
    public void testFormatWarningWithoutSourceNameAndLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description");
        String result = formatter.formatWarning(warning);
        assertEquals("CheckLevel.WARNING - Warning description\n", result);
    }
    
    @Test
    public void testFormatWarningWithSourceNameWithoutLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description", "file.js");
        String result = formatter.formatWarning(warning);
        assertEquals("file.js: CheckLevel.WARNING - Warning description\n", result);
    }
    
    @Test
    public void testFormatWarningWithSourceNameAndLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description", "file.js", 10);
        String result = formatter.formatWarning(warning);
        assertEquals("file.js:10: CheckLevel.WARNING - Warning description\n", result);
    }
}