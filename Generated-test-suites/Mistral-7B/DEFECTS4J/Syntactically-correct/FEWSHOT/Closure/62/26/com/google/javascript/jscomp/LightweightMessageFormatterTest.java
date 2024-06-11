package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.LINE_NUMBERING_FORMATTER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.Region;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.TestSourceFile;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class LightweightMessageFormatterTest {

    private SourceExcerptProvider sourceExcerptProvider;
    private LightweightMessageFormatter lightweightMessageFormatter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sourceExcerptProvider = mock(SourceExcerptProvider.class);
        lightweightMessageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE_NUMBERING_FORMATTER);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("Error message", 1, 1, "test.js", 10);
        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(null);
        String result = lightweightMessageFormatter.formatError(error);
        assertEquals("ERROR - Error message\n", result);
    }

    @Test
    public void testFormatErrorWithSource() {
        String sourceCode = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x + 1;\n" +
                "}";
        SourceExcerpt sourceExcerpt = new SourceExcerpt("test.js", sourceCode, 1, 1);
        JSError error = new JSError("Error message", 1, 5, "test.js", 10);
        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(sourceCode);
        when(sourceExcerptProvider.getSourceExcerpt(eq("test.js"), eq(1), eq(10), any(ExcerptFormatter.class)))
                .thenReturn(sourceExcerpt.toString());
        String result = lightweightMessageFormatter.formatError(error);
        assertEquals("test.js:10:5 ERROR - Error message\nfunction test() {\n  var x = 1;\n  return x + 1;\n}", result);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("Warning message", 1, 1, "test.js", 10);
        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(null);
        String result = lightweightMessageFormatter.formatWarning(warning);
        assertEquals("WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatWarningWithSource() {
        String sourceCode = "function test() {\n" +
                "  var x = 1;\n" +
                "  return x + 1;\n" +
                "}";
        SourceExcerpt sourceExcerpt = new SourceExcerpt("test.js", sourceCode, 1, 1);
        JSError warning = new JSError("Warning message", 1, 5, "test.js", 10);
        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(sourceCode);
        when(sourceExcerptProvider.getSourceExcerpt(eq("test.js"), eq(1), eq(10), any(ExcerptFormatter.class)))
                .thenReturn(sourceExcerpt.toString());
        String result = lightweightMessageFormatter.formatWarning(warning);
        assertEquals("test.js:10:5 WARNING - Warning message\nfunction test() {\n  var x = 1;\n  return x + 1;\n}", result);
    }

    @Test
    public void testFormatErrorWithNullSourceProvider() {
        JSError error = new JSError("Error message", 1, 1, "test.js", 10);
        String result = new LightweightMessageFormatter().formatError(error);
        assertEquals("ERROR - Error message\n", result);
    }

    @Test
    public void testFormatWarningWithNullSourceProvider() {
        JSError warning = new JSError("Warning message", 1, 1, "test.js", 10);
        String result = new LightweightMessageFormatter().formatWarning(warning);
        assertEquals("WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatErrorWithNoSourceLineNumber() {
        JSError error = new JSError("Error message", null, 1, "test.js", 10);
        String result = lightweightMessageFormatter.formatError(error);
        assertEquals("ERROR - Error message\n", result);
    }

    @Test
    public void testFormatWarningWithNoSourceLineNumber() {
        JSError warning = new JSError("Warning message", null, 1, "test.js", 10);
        String result = lightweightMessageFormatter.formatWarning(warning);
        assertEquals("WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatErrorWithRegion() {
        String sourceCode = "function test(x) {\n" +
                "  if (x < 10) {\n" +
                "    return x + 1;\n" +
                "  } else {\n" +
                "    throw new Error('Error message');\n" +
                "  }\n" +
                "}";
        TestSourceFile testSourceFile = new TestSourceFile("test.js", sourceCode);
        Region region = testSourceFile.getRegion(5, 15, 10, 20);
        JSError error = new JSError("Error message", null, 1, "test.js", 5);

        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(sourceCode);
        when(sourceExcerptProvider.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), any(ExcerptFormatter.class)))
                .thenReturn(region.getSourceExcerpt());

        String result = lightweightMessageFormatter.formatError(error);
        assertEquals("test.js:5:15 ERROR - Error message\nfunction test(x) {\n  if (x < 10) {\n    return x + 1;\n  } else {\n    throw new Error('Error message');\n  }\n}", result);
    }

    @Test
    public void testFormatWarningWithRegion() {
        String sourceCode = "function test(x) {\n" +
                "  if (x < 10) {\n" +
                "    return x + 1;\n" +
                "  } else {\n" +
                "    alert('Warning message');\n" +
                "  }\n" +
                "}";
        TestSourceFile testSourceFile = new TestSourceFile("test.js", sourceCode);
        Region region = testSourceFile.getRegion(5, 15, 10, 20);
        JSError warning = new JSError("Warning message", null, 1, "test.js", 5);

        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(sourceCode);
        when(sourceExcerptProvider.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), any(ExcerptFormatter.class)))
                .thenReturn(region.getSourceExcerpt());

        String result = lightweightMessageFormatter.formatWarning(warning);
        assertEquals("test.js:5:15 WARNING - Warning message\nfunction test(x) {\n  if (x < 10) {\n    return x + 1;\n  } else {\n    alert('Warning message');\n  }\n}", result);
    }

    @Test
    public void testFormatErrorWithNoRegion() {
        JSError error = new JSError("Error message", 1, 1, "test.js", 10);
        String result = lightweightMessageFormatter.formatError(error);
        assertEquals("ERROR - Error message\n", result);
    }

    @Test
    public void testFormatWarningWithNoRegion() {
        JSError warning = new JSError("Warning message", 1, 1, "test.js", 10);
        String result = lightweightMessageFormatter.formatWarning(warning);
        assertEquals("WARNING - Warning message\n", result);
    }

    @Test
    public void testFormatErrorWithEmptySourceExcerpt() {
        String sourceCode = "";
        TestSourceFile testSourceFile = new TestSourceFile("test.js", sourceCode);
        Region region = testSourceFile.getRegion(5, 15, 10, 20);
        JSError error = new JSError("Error message", null, 1, "test.js", 5);

        when(sourceExcerptProvider.getSource(eq("test.js"))).thenReturn(sourceCode);
        when(sourceExcerptProvider.getSourceExcerpt(eq("test.js"), anyInt(), anyInt(), any(ExcerptFormatter.class)))
                .thenReturn("");

        String result = lightweightMessageFormatter.formatError(error);
        assertEquals("ERROR - Error message\n", result);
    }
}

class TestSourceFile {
    private String fileName;
    private String sourceCode;

    public TestSourceFile(String fileName, String sourceCode) {
        this.fileName = fileName;
        this.sourceCode = sourceCode;
    }

    public String getSource() {
        return sourceCode;
    }

    public Region getRegion(int startLine, int startColumn, int endLine, int endColumn) {
        List<String> lines = splitToLines(sourceCode);
        int index = getIndex(startLine, startColumn, lines);
        String line = lines.get(index);
        int start = getIndex(startColumn, 0, line);
        int end = getIndex(endColumn, 1, line) + 1;
        return new Region(fileName, startLine, startColumn, endLine, endColumn, line.substring(start, end));
    }

    private List<String> splitToLines(String sourceCode) {
        return List.of(sourceCode.split("\r\n|\\r|\\n"));
    }

    private int getIndex(int lineNumber, int columnNumber, List<String> lines) {
        if (lineNumber < 0) {
            throw new IndexOutOfBoundsException("Invalid line number: " + lineNumber);
        }
        if (lineNumber >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid line number: " + lineNumber);
        }
        String line = lines.get(lineNumber);
        return getIndex(columnNumber, 0, line);
    }

    private int getIndex(int columnNumber, int offset, String line) {
        int index = 0;
        for (int i = 0; i < columnNumber; i++) {
            index += Character.isWhitespace(line.charAt(index)) ? 1 : Character.isDefinitionForKey(line.codePointAt(index)) ? 1 + line.codePointAt(index).length() : 1;
        }
        return index + offset;
    }
}

class LineNumberingFormatter implements ExcerptFormatter {
    static final LineNumberingFormatter LINE_NUMBERING_FORMATTER = new LineNumberingFormatter();

    @Override
    public String formatLine(String line, int lineNumber) {
        return line;
    }

    @Override
    public String formatRegion(Region region) {
        if (region == null) {
            return null;
        }
        String code = region.getSourceExcerpt();
        if (code.length() == 0) {
            return null;
        }

        // max length of the number display
        int numberLength = Integer.toString(region.getEndingLineNumber()).length();

        // formatting
        StringBuilder builder = new StringBuilder(code.length() * 2);
        int start = 0;
        int end = code.indexOf('\n', start);
        int lineNumber = region.getBeginningLineNumber();
        while (start >= 0) {
            // line extraction
            String line;
            if (end < 0) {
                line = code.substring(start);
                if (line.length() == 0) {
                    return builder.substring(0, builder.length() - 1);
                }
            } else {
                line = code.substring(start, end);
            }
            builder.append("  ");

            // nice spaces for the line number
            int spaces = numberLength - Integer.toString(lineNumber).length();
            builder.append(Strings.repeat(" ", spaces));
            builder.append(lineNumber);
            builder.append("| ");

            // end & update
            if (end < 0) {
                builder.append(line);
                start = -1;
            } else {
                builder.append(line);
                builder.append('\n');
                start = end + 1;
                end = code.indexOf('\n', start);
                lineNumber++;
            }
        }
        return builder.toString();
    }
}