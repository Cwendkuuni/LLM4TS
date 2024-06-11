
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error", "test.js", 2);
        String expected = "test.js:2: ERROR - error\n" +
                "function test() {\n" +
                "    console.log('error');\n" +
                "}\n" +
                "         ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error", "test.js", 2);
        String expected = "ERROR - error\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning", "test.js", 2);
        String expected = "test.js:2: WARNING - warning\n" +
                "function test() {\n" +
                "    console.log('warning');\n" +
                "}\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("warning", "test.js", 2);
        String expected = "WARNING - warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithEmptySource() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error", "test.js", 2);
        String expected = "test.js:2: ERROR - error\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithEmptySource() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning", "test.js", 2);
        String expected = "test.js:2: WARNING - warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithInvalidCharno() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error", "test.js", 2);
        error.setCharno(100);
        String expected = "test.js:2: ERROR - error\n" +
                "function test() {\n" +
                "    console.log('error');\n" +
                "}\n" +
                "                                        ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithInvalidCharno() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning", "test.js", 2);
        warning.setCharno(100);
        String expected = "test.js:2: WARNING - warning\n" +
                "function test() {\n" +
                "    console.log('warning');\n" +
                "}\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullSourceName() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error", null, 2);
        String expected = "ERROR - error\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithNullSourceName() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning", null, 2);
        String expected = "WARNING - warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error", "test.js", -1);
        String expected = "test.js: ERROR - error\n" +
                "function test() {\n" +
                "    console.log('error');\n" +
                "}\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutLineNumber() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning", "test.js", -1);
        String expected = "test.js: WARNING - warning\n" +
                "function test() {\n" +
                "    console.log('warning');\n" +
                "}\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithWhitespaceInSourceLine() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error", "test.js", 2);
        error.setCharno(10);
        String expected = "test.js:2: ERROR - error\n" +
                "function test() {\n" +
                "         console.log('error');\n" +
                "         ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithWhitespaceInSourceLine() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning", "test.js", 2);
        warning.setCharno(10);
        String expected = "test.js:2: WARNING - warning\n" +
                "function test() {\n" +
                "         console.log('warning');\n" +
                "}\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithEmptyMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("", "test.js", 2);
        String expected = "test.js:2: ERROR - \n" +
                "function test() {\n" +
                "    console.log('error');\n" +
                "}\n" +
                "         ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithEmptyMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("", "test.js", 2);
        String expected = "test.js:2: WARNING - \n" +
                "function test() {\n" +
                "    console.log('warning');\n" +
                "}\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('error');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError(null, "test.js", 2);
        String expected = "test.js:2: ERROR - null\n" +
                "function test() {\n" +
                "    console.log('error');\n" +
                "}\n" +
                "         ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithNullMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(SourceExcerpt excerpt, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return "function test() {\n" +
                        "    console.log('warning');\n" +
                        "}";
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError(null, "test.js", 2);
        String expected = "test.js:2: WARNING - null\n" +
                "function test() {\n" +
                "    console.log('warning');\n" +
                "}\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
}
