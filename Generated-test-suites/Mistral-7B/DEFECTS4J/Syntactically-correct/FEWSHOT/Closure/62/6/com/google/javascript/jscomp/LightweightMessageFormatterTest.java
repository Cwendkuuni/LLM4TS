package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatErrorNoSource() {
        JSError error = new JSError("Error description", null, 1, 1, null);
        String expected = "ERROR: Error description\n";
        String actual = new LightweightMessageFormatter().formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatErrorWithSource() {
        String code = "function foo() {\n" +
                "  if (bar) {\n" +
                "    baz();\n" +
                "  }\n" +
                "}";

        SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
                return new SourceExcerpt(code, LINE);
            }

            @Override
            public Region getRegion(String sourceName, int beginLine, int endLine) {
                return new Region("foo.js", beginLine, endLine);
            }
        };

        JSError error = new JSError("Error description", "foo.js", 1, 1, null);
        String expected = "foo.js:1: ERROR: Error description\n" +
                "  function foo() {\n" +
                "    ^";
        String actual = new LightweightMessageFormatter(sourceExcerptProvider).formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWarningNoSource() {
        JSError warning = new JSError("Warning description", null, 1, 1, null);
        String expected = "WARNING: Warning description\n";
        String actual = new LightweightMessageFormatter().formatWarning(warning);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWarningWithSource() {
        String code = "function foo() {\n" +
                "  if (bar) {\n" +
                "    baz();\n" +
                "  }\n" +
                "}";

        SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
                return new SourceExcerpt(code, LINE);
            }

            @Override
            public Region getRegion(String sourceName, int beginLine, int endLine) {
                return new Region("foo.js", beginLine, endLine);
            }
        };

        JSError warning = new JSError("Warning description", "foo.js", 1, 1, null);
        String expected = "foo.js:1: WARNING: Warning description\n" +
                "  function foo() {\n" +
                "    ^";
        String actual = new LightweightMessageFormatter(sourceExcerptProvider).formatWarning(warning);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatErrorNullSourceExcerpt() {
        JSError error = new JSError("Error description", null, 1, 1, null);
        String actual = new LightweightMessageFormatter(null).formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatWarningNullSourceExcerpt() {
        JSError warning = new JSError("Warning description", null, 1, 1, null);
        String actual = new LightweightMessageFormatter(null).formatWarning(warning);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorNoSourceName() {
        JSError error = new JSError("Error description", null, 1, 1, null);
        String expected = "ERROR: Error description\n";
        String actual = new LightweightMessageFormatter().formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatErrorNegativeLineNumber() {
        JSError error = new JSError("Error description", null, -1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorZeroLineNumber() {
        JSError error = new JSError("Error description", null, 0, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorZeroDescription() {
        JSError error = new JSError(null, null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorNullDescription() {
        JSError error = new JSError(null, null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatWarningNoDescription() {
        JSError warning = new JSError(null, null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatWarning(warning);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorEmptyDescription() {
        JSError error = new JSError("", null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorWithRegion() {
        String code = "function foo() {\n" +
                "  if (bar) {\n" +
                "    baz();\n" +
                "  }\n" +
                "}";

        SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
                return new SourceExcerpt(code, LINE);
            }

            @Override
            public Region getRegion(String sourceName, int beginLine, int endLine) {
                return new Region("foo.js", beginLine, endLine);
            }
        };

        JSError error = new JSError("Error description", "foo.js", 1, 3, null);
        String expected = "foo.js:1: ERROR: Error description\n" +
                "  if (bar) {\n" +
                "    ^~\n" +
                "  }\n";
        String actual = new LightweightMessageFormatter(sourceExcerptProvider).formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWarningWithRegion() {
        String code = "function foo() {\n" +
                "  if (bar) {\n" +
                "    baz();\n" +
                "  }\n" +
                "}";

        SourceExcerptProvider sourceExcerptProvider = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
                return new SourceExcerpt(code, LINE);
            }

            @Override
            public Region getRegion(String sourceName, int beginLine, int endLine) {
                return new Region("foo.js", beginLine, endLine);
            }
        };

        JSError warning = new JSError("Warning description", "foo.js", 1, 3, null);
        String expected = "foo.js:1: WARNING: Warning description\n" +
                "  if (bar) {\n" +
                "    ^~\n" +
                "  }\n";
        String actual = new LightweightMessageFormatter(sourceExcerptProvider).formatWarning(warning);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatErrorWithNullSourceExcerpt() {
        JSError error = new JSError("Error description", null, 1, 1, null);
        String actual = new LightweightMessageFormatter(null).formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatWarningWithNullSourceExcerpt() {
        JSError warning = new JSError("Warning description", null, 1, 1, null);
        String actual = new LightweightMessageFormatter(null).formatWarning(warning);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorWithNullSourceName() {
        JSError error = new JSError("Error description", null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorWithNegativeLineNumber() {
        JSError error = new JSError("Error description", null, -1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorWithZeroLineNumber() {
        JSError error = new JSError("Error description", null, 0, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorWithZeroDescription() {
        JSError error = new JSError(null, null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatErrorWithNullDescription() {
        JSError error = new JSError(null, null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatError(error);
        assertNull(actual);
    }

    @Test
    public void testFormatWarningWithNullDescription() {
        JSError warning = new JSError(null, null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatWarning(warning);
        assertNull(actual);
    }

    @Test
    public void testFormatWarningWithEmptyDescription() {
        JSError warning = new JSError("", null, 1, 1, null);
        String actual = new LightweightMessageFormatter().formatWarning(warning);
        assertNull(actual);
    }
}