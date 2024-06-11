package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    
    @Test
    public void testFormatError() {
        JSError error = new JSError("description", "sourceName", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        String expected = "sourceName:1: ERROR - description\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("description", "sourceName", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        String expected = "sourceName:1: WARNING - description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("description", null, 0);
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expected = "ERROR - description\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("description", null, 0);
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expected = "WARNING - description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("sourceName", "function foo() {\n  alert('bar');\n}");
        JSError error = new JSError("description", "sourceName", 2);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = 
            "sourceName:2: ERROR - description\n" +
            "\n" +
            "  alert('bar');\n" +
            "  ^\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("sourceName", "function foo() {\n  alert('bar');\n}");
        JSError warning = new JSError("description", "sourceName", 2);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = 
            "sourceName:2: WARNING - description\n" +
            "\n" +
            "  alert('bar');\n" +
            "  ^\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithEmptySourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("sourceName", "");
        JSError error = new JSError("description", "sourceName", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "sourceName:1: ERROR - description\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithEmptySourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("sourceName", "");
        JSError warning = new JSError("description", "sourceName", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "sourceName:1: WARNING - description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithMissingEndOfLine() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("sourceName", "function foo() {\n  alert('bar')");
        JSError error = new JSError("description", "sourceName", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = 
            "sourceName:1: ERROR - description\n" +
            "\n" +
            "  alert('bar')\n" +
            "  ^\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithMissingEndOfLine() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("sourceName", "function foo() {\n  alert('bar')");
        JSError warning = new JSError("description", "sourceName", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = 
            "sourceName:1: WARNING - description\n" +
            "\n" +
            "  alert('bar')\n" +
            "  ^\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithNullSourceName() {
        JSError error = new JSError("description", null, 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        String expected = "ERROR - description\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithNullSourceName() {
        JSError warning = new JSError("description", null, 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        String expected = "WARNING - description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
}