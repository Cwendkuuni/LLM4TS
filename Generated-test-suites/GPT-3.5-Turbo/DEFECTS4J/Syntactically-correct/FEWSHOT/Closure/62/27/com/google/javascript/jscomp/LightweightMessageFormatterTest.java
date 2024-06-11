
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    
    @Test
    public void testFormatError() {
        JSError error = new JSError("Test Error", "source.js", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String expected = "source.js:10: ERROR - Test Error\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test Warning", "source.js", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String expected = "source.js:10: WARNING - Test Warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatWithoutSource() {
        JSError error = new JSError("Test Error");
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expected = "ERROR - Test Error\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatErrorWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(String sourceName, int lineNumber) {
                if (sourceName.equals("source.js") && lineNumber == 10) {
                    return "var foo = 1;";
                }
                return null;
            }
        };
        JSError error = new JSError("Test Error", "source.js", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source.js:10: ERROR - Test Error\n  var foo = 1;\n       ^\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(String sourceName, int lineNumber) {
                if (sourceName.equals("source.js") && lineNumber == 10) {
                    return "var foo = 1;";
                }
                return null;
            }
        };
        JSError warning = new JSError("Test Warning", "source.js", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source.js:10: WARNING - Test Warning\n  var foo = 1;\n       ^\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
}
