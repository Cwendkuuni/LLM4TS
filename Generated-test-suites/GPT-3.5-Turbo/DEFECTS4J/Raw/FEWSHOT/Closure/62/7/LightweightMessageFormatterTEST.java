
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        JSError error = new JSError("description", "sourceName", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatError(error);
        assertEquals("sourceName:10: ERROR - description\n", result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("description", "sourceName", 5);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatWarning(warning);
        assertEquals("sourceName:5: WARNING - description\n", result);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("description", null, 0);
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String result = formatter.formatError(error);
        assertEquals("ERROR - description\n", result);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("description", null, 0);
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String result = formatter.formatWarning(warning);
        assertEquals("WARNING - description\n", result);
    }

    @Test
    public void testFormatLine() {
        String line = "if (foo) {";
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String result = formatter.formatLine(line, 1);
        assertEquals(line, result);
    }

    @Test
    public void testFormatRegion() {
        String code = "if (foo) {\n  alert('bar');\n}";
        Region region = new Region(code, 1, 2);
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String result = formatter.formatRegion(region);
        String expected = "  1| if (foo) {\n" +
                          "  2|   alert('bar');";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatRegionWithEmptyCode() {
        String code = "";
        Region region = new Region(code, 0, 0);
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String result = formatter.formatRegion(region);
        assertNull(result);
    }

    @Test
    public void testFormatRegionWithNullRegion() {
        Region region = null;
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        String result = formatter.formatRegion(region);
        assertNull(result);
    }
}
