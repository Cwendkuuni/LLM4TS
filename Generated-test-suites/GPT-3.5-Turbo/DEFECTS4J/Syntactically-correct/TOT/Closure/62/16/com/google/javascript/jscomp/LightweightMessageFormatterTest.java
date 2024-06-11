import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;

public class LightweightMessageFormatterTest {

    private SourceExcerptProvider mockSource;
    private SourceExcerpt mockExcerpt;

    @Before
    public void setUp() {
        mockSource = mock(SourceExcerptProvider.class);
        mockExcerpt = mock(SourceExcerpt.class);
    }

    @Test
    public void testConstructorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        assertEquals(null, formatter.getSource());
    }

    @Test
    public void testConstructorWithSourceAndExcerpt() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);

        assertEquals(mockSource, formatter.getSource());
        assertEquals(mockExcerpt, formatter.excerpt);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("error description");

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource);
        String expected = "ERROR - error description\n";

        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning description");

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource);
        String expected = "WARNING - warning description\n";

        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatWithSourceAndExcerpt() {
        JSError error = new JSError("error description");
        error.sourceName = "test.js";
        error.lineNumber = 5;

        when(mockSource.get(mockExcerpt, error.sourceName, error.lineNumber, LightweightMessageFormatter.excerptFormatter))
                .thenReturn("console.log('error');");

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(mockSource, mockExcerpt);
        String expected = "test.js:5: ERROR - error description\nconsole.log('error');\n     ^\n";

        assertEquals(expected, formatter.formatError(error));
    }
}