package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.STRIpes;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.test.BaseTestCase;

import java.util.Arrays;
import java.util.List;

public class LightweightMessageFormatterTest extends BaseTestCase {
    @Test
    public void testLightweightMessageFormatterWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test Error", "Test Source", 1, 1, "Test Message");
        String expected = String.format("ERROR - Test Message%n");
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);

        error.sourceName = null;
        error.lineNumber = 0;
        expected = "ERROR - Test Message%n";
        actual = formatter.formatError(error);
        assertEquals(expected, actual);

        LightweightMessageFormatter warningFormatter = new LightweightMessageFormatter(null);
        String warningMessage = warningFormatter.formatWarning(error);
        assertEquals("WARNING - Test Message%n", warningMessage);
    }

    @Test
    public void testLightweightMessageFormatterWithSource() {
        String source = "function foo() {" +
                "  var x = 1;" +
                "  if (x > 10) {" +
                "    throw new Error('Test Error');" +
                "  }}" +
                "function bar() {" +
                "  var y = 2;" +
                "  if (y > 10) {" +
                "    throw new Error('Another Test Error');" +
                "  }}" +
                "function baz() { }";

        SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSource(String source, int offset, int length) {
                return new SourceExcerpt(source, offset, length);
            }
        };

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
        JSError error = new JSError("Test Error", "Test Source", 1, 5, "Test Message");
        String expected = String.format("Test Source:1:5: ERROR - Test Message%n%s%^\n", source.substring(0, 5));
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);

        error = new JSError("Another Test Error", "Test Source", 2, 7, "Another Test Message");
        expected = String.format("Test Source:2:7: ERROR - Another Test Message%n%s%^\n", source.substring(7, 12));
        actual = formatter.formatError(error);
        assertEquals(expected, actual);

        LightweightMessageFormatter warningFormatter = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
        JSError warning = new JSError("Test Warning", "Test Source", 1, 1, "Test Warning Message");
        String warningMessage = warningFormatter.formatWarning(warning);
        assertEquals("Test Source:1:1: WARNING - Test Warning Message%n%s%^\n", warningMessage);
    }

    @Test
    public void testLineNumberingFormatter() {
        String source = "function foo() {" +
                "  var x = 1;" +
                "  if (x > 10) {" +
                "    throw new Error('Test Error');" +
                "  }}" +
                "function bar() {" +
                "  var y = 2;" +
                "  if (y > 10) {" +
                "    throw new Error('Another Test Error');" +
                "  }}" +
                "function baz() { }";

        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();
        SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSource(String source, int offset, int length) {
                return new SourceExcerpt(source, offset, length);
            }
        };

        List<JSError> errors = Arrays.asList(
                new JSError("Test Error 1", "Test Source", 1, 5, "Test Message 1"),
                new JSError("Test Error 2", "Test Source", 1, 20, "Test Message 2"),
                new JSError("Test Error 3", "Test Source", 2, 5, "Test Message 3"),
                new JSError("Test Error 4", "Test Source", 2, 20, "Test Message 4")
        );

        for (JSError error : errors) {
            String expected = String.format("%s%n", formatter.formatRegion(new Region(sourceProvider, error.sourceName, error.beginningLineNumber, error.endLineNumber)));
            LightweightMessageFormatter formatterWithSource = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
            String actual = formatterWithSource.formatError(error).substring(0, expected.length() - 1);
            assertEquals(expected, actual);
        }
    }
}

class Region {
    private final SourceExcerptProvider sourceProvider;
    private final String sourceName;
    private final int beginningLineNumber;
    private final int endLineNumber;

    public Region(SourceExcerptProvider sourceProvider, String sourceName, int beginningLineNumber, int endLineNumber) {
        this.sourceProvider = sourceProvider;
        this.sourceName = sourceName;
        this.beginningLineNumber = beginningLineNumber;
        this.endLineNumber = endLineNumber;
    }

    public int getBeginningLineNumber() {
        return beginningLineNumber;
    }

    public int getEndLineNumber() {
        return endLineNumber;
    }

    public String getSourceExcerpt() {
        SourceExcerptProvider source = sourceProvider;
        String source = source == null ? null : source.getSource(sourceName, 0, Integer.MAX_VALUE);
        if (source == null) {
            return "";
        }

        int offset = getSourceOffset();
        int length = getSourceLength();
        return source.substring(offset, offset + length);
    }

    private int getSourceOffset() {
        int lineNumber = beginningLineNumber;
        for (int i = 0; i < sourceProvider.getLinesNumber(sourceName); i++) {
            if (i + 1 == lineNumber) {
                return getLineStartOffset(i);
            }
        }
        throw new AssertionError("Invalid line number: " + lineNumber);
    }

    private int getSourceLength() {
        SourceExcerptProvider source = sourceProvider;
        String source = source == null ? null : source.getSource(sourceName, 0, Integer.MAX_VALUE);
        if (source == null) {
            return 0;
        }

        int lineNumber = beginningLineNumber;
        int offset = getSourceOffset();
        for (int i = lineNumber; i < sourceProvider.getLinesNumber(sourceName); i++) {
            if (i + 1 == endLineNumber) {
                return getLineEndOffset(i) - offset;
            }
            offset = getLineStartOffset(i);
        }
        throw new AssertionError("Invalid line number: " + endLineNumber);
    }

    private int getLineStartOffset(int lineNumber) {
        SourceExcerptProvider source = sourceProvider;
        String source = source == null ? null : source.getSource(sourceName, 0, Integer.MAX_VALUE);
        if (source == null) {
            return 0;
        }

        int currentOffset = 0;
        for (int i = 0; i < lineNumber; i++) {
            currentOffset += source.length() - source.indexOf("\n", currentOffset) + 1;
        }
        return currentOffset;
    }

    private int getLineEndOffset(int lineNumber) {
        SourceExcerptProvider source = sourceProvider;
        String source = source == null ? null : source.getSource(sourceName, 0, Integer.MAX_VALUE);
        if (source == null) {
            return 0;
        }

        int currentOffset = 0;
        for (int i = 0; i < lineNumber; i++) {
            currentOffset += source.length() - source.indexOf("\n", currentOffset) + 1;
        }

        int lineLength = source.length() - currentOffset;
        for (int i = 0; i < lineLength; i++) {
            if (Character.isWhitespace(source.charAt(currentOffset + i))) {
                continue;
            }
            return currentOffset + i + 1;
        }
        throw new AssertionError("Invalid line number: " + lineNumber);
    }
}

class BaseTestCase {
    protected void assertEquals(String expected, String actual) {
        assertEquals(expected, actual, null);
    }

    protected void assertEquals(String expected, String actual, String message) {
        Preconditions.checkArgument(expected != null, message);
        Preconditions.checkArgument(actual != null, message);
        assertEquals(expected.toCharArray(), actual.toCharArray());
    }
}

class Strings {
    public static String repeat(String str, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

class SourceExcerptProvider {
    public interface SourceExcerpt {
        int length();

        String getSource();

        int getOffset();
    }

    public int getLinesNumber(String sourceName) {
        throw new UnsupportedOperationException();
    }
}