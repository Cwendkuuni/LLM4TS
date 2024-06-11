
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.JSError;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LightweightMessageFormatterTest {

    private SourceExcerptProvider source;
    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        source = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(source);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("Error", "source.js", 1, 10);
        String expected = "source.js:1: ERROR - Error\n";
        when(source.getSourceExcerpt("source.js", 1)).thenReturn("line 1");
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Warning", "source.js", 1, 10);
        String expected = "source.js:1: WARNING - Warning\n";
        when(source.getSourceExcerpt("source.js", 1)).thenReturn("line 1");
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSourceExcerpt() {
        JSError error = new JSError("Error", "source.js", 1, 10);
        String expected = "source.js:1: ERROR - Error\n";
        when(source.getSourceExcerpt("source.js", 1)).thenReturn(null);
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSourceExcerpt() {
        JSError warning = new JSError("Warning", "source.js", 1, 10);
        String expected = "source.js:1: WARNING - Warning\n";
        when(source.getSourceExcerpt("source.js", 1)).thenReturn(null);
        assertEquals(expected, formatter.formatWarning(warning));
    }
}