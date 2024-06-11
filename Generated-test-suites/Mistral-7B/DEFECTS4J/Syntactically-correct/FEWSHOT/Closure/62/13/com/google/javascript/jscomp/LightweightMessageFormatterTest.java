package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.LINE_NUMBERING_FORMATTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.util.Region;
import com.google.javascript.jscomp.util.SourceFile;
import com.google.javascript.jscomp.util.Strings;

import org.junit.Test;

import java.util.Arrays;

public class LightweightMessageFormatterTest {
    @Test
    public void testLightweightMessageFormatterWithoutSource() {
        LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("test", "TypeError", 1, "Test error", 12);
        String message = messageFormatter.formatError(error);
        assertEquals("ERROR - TypeError - Test error\n", message);

        error.sourceName = "test.js";
        error.lineNumber = 10;
        message = messageFormatter.formatError(error);
        assertEquals("test.js:10: ERROR - TypeError - Test error\n", message);

        error.description = "Description for test error";
        message = messageFormatter.formatError(error);
        assertEquals("test.js:10: ERROR - TypeError - Description for test error\n", message);

        error.sourceName = null;
        error.lineNumber = 0;
        message = messageFormatter.formatError(error);
        assertNull(message);
    }

    @Test
    public void testLightweightMessageFormatterWithSource() {
        String code = "function test() {\n" +
                "  var a = 5;\n" +
                "  var b = a + 3;\n" +
                "  return b;\n" +
                "}";
        SourceFile sourceFile = new SourceFile("test.js", code);
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(null, LINE_NUMBERING_FORMATTER);
        JSError error = new JSError("test", "TypeError", 3, "Test error", 12, sourceFile);
        String message = messageFormatter.formatError(error);
        assertEquals("test.js:3: ERROR - TypeError - Test error\n" +
                "  var b = a + 3;\n", message);

        error.lineNumber = 5;
        message = messageFormatter.formatError(error);
        assertEquals("test.js:5: ERROR - TypeError - Test error\n", message);

        error.description = "Description for test error";
        message = messageFormatter.formatError(error);
        assertEquals("test.js:5: ERROR - TypeError - Description for test error\n", message);

        error.sourceName = null;
        error.lineNumber = 0;
        assertNull(messageFormatter.formatError(error));

        error.sourceName = "test.js";
        error.lineNumber = 1;
        message = messageFormatter.formatError(error);
        assertNull(message);

        error.lineNumber = 1;
        message = messageFormatter.formatWarning(error);
        assertEquals("test.js:1: WARNING - TypeError - Test error\n", message);

        JSError warning = new JSError("test", "DeprecationWarning", 3, "Test warning", 12, sourceFile);
        message = messageFormatter.formatWarning(warning);
        assertEquals("test.js:3: WARNING - DeprecationWarning - Test warning\n" +
                "  var b = a + 3;\n", message);

        Region region = new Region(sourceFile.getSource(), 2, 5, 2, 11);
        error.sourceName = null;
        error.lineNumber = 0;
        error.region = region;
        message = messageFormatter.formatError(error);
        assertNull(message);

        error.sourceName = "test.js";
        message = messageFormatter.formatError(error);
        assertNotNull(message);
        assertTrue(message.contains("test.js:"));
        assertTrue(message.contains("Region:"));
        assertTrue(message.contains("5|         var b = a + 3;\n"));
    }
}

class LineNumberingFormatter implements ExcerptFormatter {
    public static final LineNumberingFormatter LINE_NUMBERING_FORMATTER = new LineNumberingFormatter();

    @Override
    public String formatLine(String line, int lineNumber) {
        return line;
    }

    @Override
    public String formatRegion(Region region) {
        if (region == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        int start = 0;
        int end;
        String code = region.getSourceExcerpt();
        for (int i = 0; i < code.length(); ) {
            if ((end = code.indexOf('\n', i)) < 0) {
                end = code.length();
            }
            String line = code.substring(i, end);
            int lineNumberLocal = region.getBeginningLineNumber();
            builder.append("  ");
            int spaces = 4 + Integer.toString(lineNumberLocal).length();
            builder.append(Strings.repeat(" ", spaces));
            builder.append(lineNumberLocal);
            builder.append("| ");
            builder.append(line);
            builder.append('\n');
            i = end + 1;
            lineNumberLocal++;
        }
        return builder.toString();
    }
}