
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("Test error", "test.js", 10, 5);
        
        String expected = "test.js:10: ERROR - Test error\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("Test warning", "test.js", 5, 2);
        
        String expected = "test.js:5: WARNING - Test warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test error", null, -1, -1);
        
        String expected = "ERROR - Test error\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Test warning", null, -1, -1);
        
        String expected = "WARNING - Test warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
}
