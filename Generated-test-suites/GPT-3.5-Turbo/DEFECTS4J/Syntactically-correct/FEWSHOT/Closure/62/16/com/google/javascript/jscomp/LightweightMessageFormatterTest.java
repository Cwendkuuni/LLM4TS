import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
  
    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Error description");
        assertEquals("ERROR - Error description\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Warning description");
        assertEquals("WARNING - Warning description\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSource() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        JSError error = new JSError("Error description");
        error.setSourceName("test.js");
        error.setLineNumber(5);
        assertEquals("test.js:5: ERROR - Error description\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSource() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        JSError warning = new JSError("Warning description");
        warning.setSourceName("test.js");
        warning.setLineNumber(5);
        assertEquals("test.js:5: WARNING - Warning description\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSourceAndExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setSourceCode("var foo = 5;\nvar bar = \"test\";");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description");
        error.setSourceName("test.js");
        error.setLineNumber(2);
        error.setCharno(5);
        assertEquals("test.js:2: ERROR - Error description\n" +
                "var bar = \"test\";\n" +
                "    ^\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceAndExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setSourceCode("var foo = 5;\nvar bar = \"test\";");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description");
        warning.setSourceName("test.js");
        warning.setLineNumber(2);
        warning.setCharno(5);
        assertEquals("test.js:2: WARNING - Warning description\n" +
                "var bar = \"test\";\n" +
                "    ^\n", formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSourceAndExcerptNoCharno() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setSourceCode("var foo = 5;\nvar bar = \"test\";");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error description");
        error.setSourceName("test.js");
        error.setLineNumber(2);
        assertEquals("test.js:2: ERROR - Error description\n" +
                "var bar = \"test\";\n", formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceAndExcerptNoCharno() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setSourceCode("var foo = 5;\nvar bar = \"test\";");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Warning description");
        warning.setSourceName("test.js");
        warning.setLineNumber(2);
        assertEquals("test.js:2: WARNING - Warning description\n" +
                "var bar = \"test\";\n", formatter.formatWarning(warning));
    }
  
}