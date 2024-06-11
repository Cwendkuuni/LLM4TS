package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt.LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Excerpt;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {

    @Mock
    private SourceExcerptProvider sourceExcerptProviderMock;
    private LightweightMessageFormatter formatter;
    private JSError error;
    private JSError warning;

    @Before
    public void setUp() {
        formatter = new LightweightMessageFormatter(sourceExcerptProviderMock);
        error = new JSError("error description", "test.js", 10, 5, null);
        warning = new JSError("warning description", "test.js", 15, 2, null);
    }

    @Test
    public void testFormatError() {
        String expected = "test.js:10: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        String expected = "test.js:15: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSourceName() {
        error = new JSError("error description", null, 0, 0, null);
        String expected = "ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSourceName() {
        warning = new JSError("warning description", null, 0, 0, null);
        String expected = "WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        String sourceExcerpt = "if (foo) {\n  alert('bar');\n  }\n";
        Excerpt excerptMock = new Excerpt() {
            @Override
            public String get(SourceExcerptProvider source,
                    String sourceName, int lineNumber,
                    ExcerptFormatter formatter) {
                return sourceExcerpt;
            }
        };

        LightweightMessageFormatter formatterWithExcerpt =
                new LightweightMessageFormatter(sourceExcerptProviderMock, excerptMock);

        String expected = "test.js:10: ERROR - error description\n" +
                "if (foo) {\n" +
                "  alert('bar');\n" +
                "  }\n";

        assertEquals(expected, formatterWithExcerpt.formatError(error));
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        String sourceExcerpt = "if (foo) {\n  alert('bar');\n  }\n";
        Excerpt excerptMock = new Excerpt() {
            @Override
            public String get(SourceExcerptProvider source,
                    String sourceName, int lineNumber,
                    ExcerptFormatter formatter) {
                return sourceExcerpt;
            }
        };

        LightweightMessageFormatter formatterWithExcerpt =
                new LightweightMessageFormatter(sourceExcerptProviderMock, excerptMock);

        String expected = "test.js:15: WARNING - warning description\n" +
                "if (foo) {\n" +
                "  alert('bar');\n" +
                "  }\n";

        assertEquals(expected, formatterWithExcerpt.formatWarning(warning));
    }

    @Test
    public void testLineNumberingFormatterFormatLine() {
        LineNumberingFormatter formatter = new LineNumberingFormatter();
        String line = "   10| if (foo) {";
        int lineNumber = 10;
        assertEquals(line, formatter.formatLine(line, lineNumber));
    }

    @Test
    public void testLineNumberingFormatterFormatRegion() {
        String code = "if (foo) {\n  alert('bar');\n  }\n";

        Region region = new Region(null, 1, code.length(), code);
        LineNumberingFormatter formatter = new LineNumberingFormatter();

        String expected = "  1| if (foo) {\n" +
                "  2|   alert('bar');\n" +
                "  3|   }\n";

        assertEquals(expected, formatter.formatRegion(region));
    }

    @Test
    public void testLineNumberingFormatterFormatEmptyRegion() {
        Region region = new Region(null, 1, 0, "");
        LineNumberingFormatter formatter = new LineNumberingFormatter();

        assertNull(formatter.formatRegion(region));
    }
}