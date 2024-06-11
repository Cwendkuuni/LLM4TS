// Junit 4 test cases for the LightweightMessageFormatter class


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;
    private JSError error;
    private JSError warning;

    @Before
    public void setUp() {
        SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
        when(sourceProvider.getSourceExcerpt("test.js", 1, 0, LightweightMessageFormatter.LineNumberingFormatter.INSTANCE))
                .thenReturn("if (foo) {\n  alert('bar');\n}");
        formatter = new LightweightMessageFormatter(sourceProvider);

        error = new JSError("ERROR", "test.js", 1, 0, CheckLevel.ERROR);
        warning = new JSError("WARNING", "test.js", 2, 0, CheckLevel.WARNING);
    }

    @Test
    public void formatError_noSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        String result = formatter.formatError(error);

        assertEquals("ERROR - ERROR\n", result);
    }

    @Test
    public void formatError_withSource() {
        String result = formatter.formatError(error);

        assertEquals("test.js:1: ERROR - ERROR\nif (foo) {\n  alert('bar');\n}\n", result);
    }

    @Test
    public void formatWarning_noSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        String result = formatter.formatWarning(warning);

        assertEquals("WARNING - WARNING\n", result);
    }

    @Test
    public void formatWarning_withSource() {
        String result = formatter.formatWarning(warning);

        assertEquals("test.js:2: WARNING - WARNING\nif (foo) {\n  alert('bar');\n}\n", result);
    }
}
