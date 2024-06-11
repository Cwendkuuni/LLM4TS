package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        formatter = new LightweightMessageFormatter(null);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("description", "sourceName", 5);
        String expected = "sourceName:5: ERROR - description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("description", "sourceName", 10);
        String expected = "sourceName:10: WARNING - description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatWithoutSource() {
        JSError error = new JSError("description", null, 0);
        String expected = "ERROR - description\n";
        assertEquals(expected, LightweightMessageFormatter.withoutSource().formatError(error));
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        SourceExcerptProvider source = new TestSourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError error = new JSError("description", "sourceName", 5);
        String expected = "sourceName:5: ERROR - description\n" +
                "   3| line3\n" +
                "   4| line4\n" +
                "   5| line5\n" +
                " ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        SourceExcerptProvider source = new TestSourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError warning = new JSError("description", "sourceName", 10);
        String expected = "sourceName:10: WARNING - description\n" +
                "   8| line8\n" +
                "   9| line9\n" +
                "  10| line10\n" +
                " ^\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    private static class TestSourceExcerptProvider implements SourceExcerptProvider {
        public String getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter excerptFormatter) {
            switch (lineNumber) {
                case 3:
                    return "line3";
                case 4:
                    return "line4";
                case 5:
                    return "line5";
                case 8:
                    return "line8";
                case 9:
                    return "line9";
                case 10:
                    return "line10";
                default:
                    return null;
            }
        }
    }
}