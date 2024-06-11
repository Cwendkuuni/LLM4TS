
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class LightweightMessageFormatterTest {
    private SourceExcerptProvider mockSource;
    private SourceExcerpt mockExcerpt;

    @Before
    public void setup() {
        mockSource = Mockito.mock(SourceExcerptProvider.class);
        mockExcerpt = Mockito.mock(SourceExcerpt.class);
    }

    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);

        JSError error = new JSError("Test error", "test.js", 10, 5);
        String actual = formatter.formatError(error);

        StringBuilder expected = new StringBuilder();
        expected.append("test.js:10: ERROR - Test error\n");
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);

        JSError warning = new JSError("Test warning", "test.js", 5, 3);
        String actual = formatter.formatWarning(warning);

        StringBuilder expected = new StringBuilder();
        expected.append("test.js:5: WARNING - Test warning\n");
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testFormatWithSourceExcerpt() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);

        JSError error = new JSError("Test error", "test.js", 10, 5);
        String sourceExcerpt = "  6| var foo = 10;\n  7| var bar = 20;\n  8| ";
        Mockito.when(mockExcerpt.get(mockSource, error.sourceName, error.lineNumber, formatter.excerptFormatter))
                .thenReturn(sourceExcerpt);
        String actual = formatter.format(error, false);

        StringBuilder expected = new StringBuilder();
        expected.append("test.js:10: ERROR - Test error\n");
        expected.append(sourceExcerpt);
        expected.append('\n');
        expected.append("       ^\n");
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testFormatWithoutSourceExcerpt() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);

        JSError error = new JSError("Test error", "test.js", 10, 5);
        Mockito.when(mockExcerpt.get(mockSource, error.sourceName, error.lineNumber, formatter.excerptFormatter))
                .thenReturn(null);
        String actual = formatter.format(error, false);

        StringBuilder expected = new StringBuilder();
        expected.append("test.js:10: ERROR - Test error\n");
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testFormatWithNullSourceName() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);

        JSError error = new JSError("Test error", null, 10, 5);
        String actual = formatter.formatError(error);

        StringBuilder expected = new StringBuilder();
        expected.append("ERROR - Test error\n");
        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        JSError error = new JSError("Test error", "test.js", 10, 5);
        String actual = formatter.formatError(error);

        StringBuilder expected = new StringBuilder();
        expected.append("ERROR - Test error\n");
        assertEquals(expected.toString(), actual);
    }
}
