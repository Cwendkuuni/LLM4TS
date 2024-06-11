import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("Test Error", "test.js", 10);
        assertEquals("test.js:10: ERROR - Test Error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("Test Warning", "test.js", 5);
        assertEquals("test.js:5: WARNING - Test Warning\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test Error", null, -1);
        assertEquals("ERROR - Test Error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Test Warning", null, -1);
        assertEquals("WARNING - Test Warning\n", formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Test Error", "test.js", 10);
        source.setSourceExcerpt("var foo = 1;\n" +
                               "var bar = 2;\n" +
                               "var baz = 3;\n");
        assertEquals("test.js:10: ERROR - Test Error\n" +
                     "  1| var foo = 1;\n" +
                     "  2| var bar = 2;\n" +
                     "  3| var baz = 3;\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Test Warning", "test.js", 5);
        source.setSourceExcerpt("var foo = 1;\n" +
                               "var bar = 2;\n" +
                               "var baz = 3;\n");
        assertEquals("test.js:5: WARNING - Test Warning\n" +
                     "  1| var foo = 1;\n" +
                     "  2| var bar = 2;\n" +
                     "  3| var baz = 3;\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithExcerptAndLineNumberingFormatter() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError error = new JSError("Test Error", "test.js", 10);
        source.setSourceExcerpt("var foo = 1;\n" +
                               "var bar = 2;\n" +
                               "var baz = 3;\n");
        assertEquals("test.js:10: ERROR - Test Error\n" +
                     "  1| var foo = 1;\n" +
                     "  2| var bar = 2;\n" +
                     "  3| var baz = 3;\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithExcerptAndLineNumberingFormatter() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError warning = new JSError("Test Warning", "test.js", 5);
        source.setSourceExcerpt("var foo = 1;\n" +
                               "var bar = 2;\n" +
                               "var baz = 3;\n");
        assertEquals("test.js:5: WARNING - Test Warning\n" +
                     "  1| var foo = 1;\n" +
                     "  2| var bar = 2;\n" +
                     "  3| var baz = 3;\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithExcerptAndLineNumberingFormatterMissingEnd() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError error = new JSError("Test Error", "test.js", 10);
        source.setSourceExcerpt("var foo = 1;\n" +
                               "var bar = 2;\n" +
                               "var baz = 3;");
        assertEquals("test.js:10: ERROR - Test Error\n" +
                     "  1| var foo = 1;\n" +
                     "  2| var bar = 2;\n" +
                     "  3| var baz = 3;", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithExcerptAndLineNumberingFormatterMissingEnd() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError warning = new JSError("Test Warning", "test.js", 5);
        source.setSourceExcerpt("var foo = 1;\n" +
                               "var bar = 2;\n" +
                               "var baz = 3;");
        assertEquals("test.js:5: WARNING - Test Warning\n" +
                     "  1| var foo = 1;\n" +
                     "  2| var bar = 2;\n" +
                     "  3| var baz = 3;", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSourceAndExcerpt() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test Error", null, -1);
        assertEquals("ERROR - Test Error\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSourceAndExcerpt() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Test Warning", null, -1);
        assertEquals("WARNING - Test Warning\n", formatter.formatWarning(warning));
    }
}