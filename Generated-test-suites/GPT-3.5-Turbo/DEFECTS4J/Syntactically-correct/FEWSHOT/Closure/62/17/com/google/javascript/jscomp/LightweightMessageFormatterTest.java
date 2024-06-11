package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        JSError error = new JSError("error description", "source", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String expected = "source:1: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning description", "source", 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String expected = "source:1: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("error description", null, -1);
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expected = "ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("warning description", null, -1);
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expected = "WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: ERROR - error description\nline 1\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: WARNING - warning description\nline 1\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullSourceExcerpt() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = null;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithNullSourceExcerpt() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = null;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithLineExcerptFormatter() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        String expected = "source:1: ERROR - error description\nline 1\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithLineExcerptFormatter() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        String expected = "source:1: WARNING - warning description\nline 1\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullLineExcerptFormatter() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, null);
        String expected = "source:1: ERROR - error description\nline 1\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithNullLineExcerptFormatter() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, null);
        String expected = "source:1: WARNING - warning description\nline 1\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithLineNumberingFormatter() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.NUMBER);
        String expected = "source:1: ERROR - error description\n  1| line 1\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithLineNumberingFormatter() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.NUMBER);
        String expected = "source:1: WARNING - warning description\n  1| line 1\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullLineNumberingFormatter() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, null);
        String expected = "source:1: ERROR - error description\nline 1\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithNullLineNumberingFormatter() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("line 1\nline 2\nline 3");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, null);
        String expected = "source:1: WARNING - warning description\nline 1\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithEmptySourceExcerpt() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithEmptySourceExcerpt() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setExcerpt("");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullSourceExcerptProvider() {
        JSError error = new JSError("error description", "source", 1);
        SourceExcerptProvider source = null;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithNullSourceExcerptProvider() {
        JSError warning = new JSError("warning description", "source", 1);
        SourceExcerptProvider source = null;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expected = "source:1: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

}