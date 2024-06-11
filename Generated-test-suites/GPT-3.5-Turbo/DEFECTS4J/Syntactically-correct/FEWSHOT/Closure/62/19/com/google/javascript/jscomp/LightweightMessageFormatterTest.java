package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setup() {
        // Create a new instance of LightweightMessageFormatter
        formatter = new LightweightMessageFormatter(null);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("Test Error", "test.js", 1);
        
        String result = formatter.formatError(error);
        
        assertEquals("test.js:1: ERROR - Test Error\n", result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test Warning", "test.js", 1);
        
        String result = formatter.formatWarning(warning);
        
        assertEquals("test.js:1: WARNING - Test Warning\n", result);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("Test Error", null, -1);
        
        String result = formatter.formatError(error);
        
        assertEquals("ERROR - Test Error\n", result);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("Test Warning", null, -1);

        String result = formatter.formatWarning(warning);

        assertEquals("WARNING - Test Warning\n", result);
    }
    
}