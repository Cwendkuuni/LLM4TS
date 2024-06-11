
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {
    private LightweightMessageFormatter formatter;

    @Before
    public void setup() {
        formatter = new LightweightMessageFormatter(null);
    }

    @Test
    public void testFormatError() {
        JSError error1 = new JSError("error1");
        JSError error2 = new JSError("error2");
        assertEquals("ERROR - error1\n", formatter.formatError(error1));
        assertEquals("ERROR - error2\n", formatter.formatError(error2));
    }

    @Test
    public void testFormatWarning() {
        JSError warning1 = new JSError("warning1");
        JSError warning2 = new JSError("warning2");
        assertEquals("WARNING - warning1\n", formatter.formatWarning(warning1));
        assertEquals("WARNING - warning2\n", formatter.formatWarning(warning2));
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        JSError error = new JSError("error");
        error.sourceName = "source";
        error.lineNumber = 1;
        assertEquals("source:1: ERROR - error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        JSError warning = new JSError("warning");
        warning.sourceName = "source";
        warning.lineNumber = 2;
        assertEquals("source:2: WARNING - warning\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSourceExcerpt() {
        formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error");
        assertEquals("ERROR - error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSourceExcerpt() {
        formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("warning");
        assertEquals("WARNING - warning\n", formatter.formatWarning(warning));
    }
}
