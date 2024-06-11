
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        JSError error = new JSError("sourceName", 1, CheckLevel.ERROR, "description");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String expectedResult = "sourceName:1: ERROR - description\n";
        assertEquals(expectedResult, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("sourceName", 1, CheckLevel.WARNING, "description");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String expectedResult = "sourceName:1: WARNING - description\n";
        assertEquals(expectedResult, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("sourceName", 1, CheckLevel.ERROR, "description");
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expectedResult = "ERROR - description\n";
        assertEquals(expectedResult, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("sourceName", 1, CheckLevel.WARNING, "description");
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String expectedResult = "WARNING - description\n";
        assertEquals(expectedResult, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        JSError error = new JSError("sourceName", 1, CheckLevel.ERROR, "description");
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expectedResult = "sourceName:1: ERROR - description\n";
        assertEquals(expectedResult, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        JSError warning = new JSError("sourceName", 1, CheckLevel.WARNING, "description");
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String expectedResult = "sourceName:1: WARNING - description\n";
        assertEquals(expectedResult, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSourceExcerptAndLineNumberingFormatter() {
        JSError error = new JSError("sourceName", 1, CheckLevel.ERROR, "description");
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
        String expectedResult = "sourceName:1: ERROR - description\n";
        assertEquals(expectedResult, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceExcerptAndLineNumberingFormatter() {
        JSError warning = new JSError("sourceName", 1, CheckLevel.WARNING, "description");
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
        String expectedResult = "sourceName:1: WARNING - description\n";
        assertEquals(expectedResult, formatter.formatWarning(warning));
    }
}
