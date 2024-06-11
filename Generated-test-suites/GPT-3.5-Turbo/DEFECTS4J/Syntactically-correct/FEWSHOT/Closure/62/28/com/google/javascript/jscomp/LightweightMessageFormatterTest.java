
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        JSError error = new JSError("Test Error", "test.js", 10, 1);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatError(error);
        assertEquals("test.js:10: ERROR - Test Error\n", result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test Warning", "test.js", 5, 2);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatWarning(warning);
        assertEquals("test.js:5: WARNING - Test Warning\n", result);
    }

    @Test
    public void testFormatErrorWithSource() {
        JSError error = new JSError("Test Error", "test.js", 10, 1);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("test.js", "function test() {\nconsole.log('test');\n}");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.formatError(error);
        assertEquals("test.js:10: ERROR - Test Error\nfunction test() {\nconsole.log('test');\n}\n^", result);
    }

    @Test
    public void testFormatWarningWithSource() {
        JSError warning = new JSError("Test Warning", "test.js", 5, 2);
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.addSource("test.js", "function test() {\nconsole.log('test');\n}");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.formatWarning(warning);
        assertEquals("test.js:5: WARNING - Test Warning\nfunction test() {\nconsole.log('test');\n}\n^", result);
    }
}
