package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {
    private SourceExcerptProvider source;
    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        source = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(source);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("Test Error");
        String expected = "ERROR - Test Error\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test Warning");
        String expected = "WARNING - Test Warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSource() {
        JSError error = new JSError("Test Error");
        error.sourceName = "test.js";
        error.lineNumber = 1;
        when(source.get(error.sourceName)).thenReturn("var x = 5;");
        String expected = "test.js:1: ERROR - Test Error\nvar x = 5;\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSource() {
        JSError warning = new JSError("Test Warning");
        warning.sourceName = "test.js";
        warning.lineNumber = 1;
        when(source.get(warning.sourceName)).thenReturn("var x = 5;");
        String expected = "test.js:1: WARNING - Test Warning\nvar x = 5;\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test Error");
        String expected = "ERROR - Test Error\n";
        assertEquals(expected, formatterWithoutSource.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Test Warning");
        String expected = "WARNING - Test Warning\n";
        assertEquals(expected, formatterWithoutSource.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithEmptySourceExcerpt() {
        JSError error = new JSError("Test Error");
        error.sourceName = "test.js";
        error.lineNumber = 1;
        when(source.get(error.sourceName)).thenReturn("");
        String expected = "test.js:1: ERROR - Test Error\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithEmptySourceExcerpt() {
        JSError warning = new JSError("Test Warning");
        warning.sourceName = "test.js";
        warning.lineNumber = 1;
        when(source.get(warning.sourceName)).thenReturn("");
        String expected = "test.js:1: WARNING - Test Warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithCharno() {
        JSError error = new JSError("Test Error");
        error.sourceName = "test.js";
        error.lineNumber = 1;
        error.setCharno(5);
        when(source.get(error.sourceName)).thenReturn("var x = 5;");
        String expected = "test.js:1: ERROR - Test Error\nvar  x = 5;\n     ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithCharno() {
        JSError warning = new JSError("Test Warning");
        warning.sourceName = "test.js";
        warning.lineNumber = 1;
        warning.setCharno(5);
        when(source.get(warning.sourceName)).thenReturn("var x = 5;");
        String expected = "test.js:1: WARNING - Test Warning\nvar  x = 5;\n     ^\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatRegionNull() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        assertNull(formatter.formatRegion(null));
    }

    @Test
    public void testFormatRegionEmptyCode() {
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("", 1, 1);
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String expected = "";
        assertEquals(expected, formatter.formatRegion(region));
    }

    @Test
    public void testFormatRegion() {
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("alert('Test');", 1, 1);
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String expected = "  1| alert('Test');\n";
        assertEquals(expected, formatter.formatRegion(region));
    }

    @Test
    public void testFormatRegionWithMultipleLines() {
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("var x = 5;\nalert('Test');", 1, 2);
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String expected = "  1| var x = 5;\n" +
                          "  2| alert('Test');\n";
        assertEquals(expected, formatter.formatRegion(region));
    }
}