package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError error = new JSError("test error", "test.js", 10, 5);
        String expected = "test.js:10: ERROR - test error\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError warning = new JSError("test warning", "test.js", 5, 10);
        String expected = "test.js:5: WARNING - test warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatWithoutSourceError() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("test error", "test.js", 10, 5);
        String expected = "ERROR - test error\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWithoutSourceWarning() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("test warning", "test.js", 5, 10);
        String expected = "WARNING - test warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testLineNumberingFormatterFormatLine() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String line = "if (foo) {";
        int lineNumber = 5;
        String expected = "if (foo) {";
        assertEquals(expected, formatter.formatLine(line, lineNumber));
    }
    
    @Test
    public void testLineNumberingFormatterFormatRegion() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String code = "if (foo) {\n  alert('bar');\n}";
        int beginningLineNumber = 5;
        int endingLineNumber = 7;
        Region region = new Region(code, beginningLineNumber, endingLineNumber);
        String expected = "  5| if (foo) {\n  6|   alert('bar');\n  7| }";
        String result = formatter.formatRegion(region);
        assertEquals(expected, result);
    }
    
    @Test
    public void testLineNumberingFormatterFormatRegionWithEmptyCode() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String code = "";
        int beginningLineNumber = 5;
        int endingLineNumber = 7;
        Region region = new Region(code, beginningLineNumber, endingLineNumber);
        assertNull(formatter.formatRegion(region));
    }
    
    @Test
    public void testLineNumberingFormatterFormatRegionWithNullRegion() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        assertNull(formatter.formatRegion(null));
    }
}