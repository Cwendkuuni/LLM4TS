package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        JSError error = new JSError("Error message");
        error.setSourceName("source.js");
        error.setLineNumber(10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatError(error);

        assertNotNull(result);
        assertEquals("source.js:10: ERROR - Error message\n", result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Warning message");
        warning.setSourceName("source.js");
        warning.setLineNumber(5);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatWarning(warning);

        assertNotNull(result);
        assertEquals("source.js:5: WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("Error message");
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String result = formatter.formatError(error);

        assertNotNull(result);
        assertEquals("ERROR - Error message\n", result);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("Warning message");
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        String result = formatter.formatWarning(warning);

        assertNotNull(result);
        assertEquals("WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        JSError error = new JSError("Error message");
        error.setSourceName("source.js");
        error.setLineNumber(10);
        String sourceExcerpt = "function foo() {\n  var a = 5;\n";
        SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.formatError(error);

        assertNotNull(result);
        assertEquals("source.js:10: ERROR - Error message\n" +
                     "function foo() {\n" +
                     "  var a = 5;\n", result);
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        JSError warning = new JSError("Warning message");
        warning.setSourceName("source.js");
        warning.setLineNumber(5);
        String sourceExcerpt = "function bar() {\n  console.log(\"Hello World\");\n";
        SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.formatWarning(warning);

        assertNotNull(result);
        assertEquals("source.js:5: WARNING - Warning message\n" +
                     "function bar() {\n" +
                     "  console.log(\"Hello World\");\n", result);
    }

    @Test
    public void testFormatErrorWithEmptySourceExcerpt() {
        JSError error = new JSError("Error message");
        error.setSourceName("source.js");
        error.setLineNumber(10);
        SourceExcerptProvider source = new MockSourceExcerptProvider("");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.formatError(error);

        assertNotNull(result);
        assertEquals("source.js:10: ERROR - Error message\n", result);
    }

    @Test
    public void testFormatWarningWithEmptySourceExcerpt() {
        JSError warning = new JSError("Warning message");
        warning.setSourceName("source.js");
        warning.setLineNumber(5);
        SourceExcerptProvider source = new MockSourceExcerptProvider("");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.formatWarning(warning);

        assertNotNull(result);
        assertEquals("source.js:5: WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatErrorWithPartialSourceExcerpt() {
        JSError error = new JSError("Error message");
        error.setSourceName("source.js");
        error.setLineNumber(10);
        String sourceExcerpt = "function foo() {\n  var a = 5;\n";
        SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        String result = formatter.formatError(error);

        assertNotNull(result);
        assertEquals("source.js:10: ERROR - Error message\n" +
                     "function foo() {\n", result);
    }

    @Test
    public void testFormatWarningWithPartialSourceExcerpt() {
        JSError warning = new JSError("Warning message");
        warning.setSourceName("source.js");
        warning.setLineNumber(5);
        String sourceExcerpt = "function bar() {\n  console.log(\"Hello World\");\n";
        SourceExcerptProvider source = new MockSourceExcerptProvider(sourceExcerpt);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        String result = formatter.formatWarning(warning);

        assertNotNull(result);
        assertEquals("source.js:5: WARNING - Warning message\n" +
                     "function bar() {\n", result);
    }

    private class MockSourceExcerptProvider implements SourceExcerptProvider {
        private String sourceExcerpt;

        public MockSourceExcerptProvider(String sourceExcerpt) {
            this.sourceExcerpt = sourceExcerpt;
        }

        public String getSourceExcerpt(String sourceName, int lineNumber) {
            return sourceExcerpt;
        }
    }
}