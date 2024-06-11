
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
    
public class LightweightMessageFormatterTest {
    
    private LightweightMessageFormatter formatter;
    
    @Before
    public void setUp() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        formatter = new LightweightMessageFormatter(source);
    }
    
    @Test
    public void testFormatError() {
        JSError error = new JSError("source.js", 1, 1, "Error message");
        String formattedError = formatter.formatError(error);
        assertEquals("source.js:1: ERROR - Error message\n", formattedError);
    }
    
    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("source.js", 1, 1, "Warning message");
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - Warning message\n", formattedWarning);
    }
    
    @Test
    public void testFormatWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("source.js", 1, 1, "Warning message");
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("WARNING - Warning message\n", formattedWarning);
    }
    
    @Test
    public void testFormatError_NullSourceName() {
        JSError error = new JSError(null, 1, 1, "Error message");
        String formattedError = formatter.formatError(error);
        assertEquals("ERROR - Error message\n", formattedError);
    }
    
    @Test
    public void testFormatWarning_NullSourceName() {
        JSError warning = new JSError(null, 1, 1, "Warning message");
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("WARNING - Warning message\n", formattedWarning);
    }
    
    @Test
    public void testFormatError_NegativeLineNumber() {
        JSError error = new JSError("source.js", -1, 1, "Error message");
        String formattedError = formatter.formatError(error);
        assertEquals("source.js:-1: ERROR - Error message\n", formattedError);
    }
    
    @Test
    public void testFormatWarning_NegativeLineNumber() {
        JSError warning = new JSError("source.js", -1, 1, "Warning message");
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:-1: WARNING - Warning message\n", formattedWarning);
    }
    
    @Test
    public void testFormatError_NullDescription() {
        JSError error = new JSError("source.js", 1, 1, null);
        String formattedError = formatter.formatError(error);
        assertEquals("source.js:1: ERROR - null\n", formattedError);
    }
    
    @Test
    public void testFormatWarning_NullDescription() {
        JSError warning = new JSError("source.js", 1, 1, null);
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("source.js:1: WARNING - null\n", formattedWarning);
    }
}
