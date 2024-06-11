package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("Error", "sourceName", 1, 0);
        assertEquals("sourceName:1: ERROR - Error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("Warning", "sourceName", 1, 0);
        assertEquals("sourceName:1: WARNING - Warning\n", formatter.formatWarning(warning));
    }
}