import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;
    private SourceExcerptProvider excerptProvider;

    @Before
    public void setUp() {
        excerptProvider = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(excerptProvider);
    }

    @Test
    public void testFormatError_WithoutSource() {
        JSError error = new JSError("Error Description");

        String result = formatter.formatError(error);

        String expected = "ERROR - Error Description\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatError_WithSourceAndLineNumber() {
        JSError error = new JSError("Error Description", "source.js", 5);

        when(excerptProvider.getSourceExcerpt("source.js", 5)).thenReturn("    var x = 10;");

        String result = formatter.formatError(error);

        String expected = "source.js:5: ERROR - Error Description\n" +
                "    var x = 10;\n" +
                "    ^\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatError_WithSourceAndNegativeLineNumber() {
        JSError error = new JSError("Error Description", "source.js", -1);

        when(excerptProvider.getSourceExcerpt("source.js", -1)).thenReturn("    var x = 10;");

        String result = formatter.formatError(error);

        String expected = "source.js:-1: ERROR - Error Description\n" +
                "    var x = 10;\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning_WithoutSource() {
        JSError warning = new JSError("Warning Description");

        String result = formatter.formatWarning(warning);

        String expected = "WARNING - Warning Description\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning_WithSourceAndLineNumber() {
        JSError warning = new JSError("Warning Description", "source.js", 10);

        when(excerptProvider.getSourceExcerpt("source.js", 10)).thenReturn("    if (x == 5) {");

        String result = formatter.formatWarning(warning);

        String expected = "source.js:10: WARNING - Warning Description\n" +
                "    if (x == 5) {\n" +
                "    ^\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning_WithSourceAndNegativeLineNumber() {
        JSError warning = new JSError("Warning Description", "source.js", -1);

        when(excerptProvider.getSourceExcerpt("source.js", -1)).thenReturn("    if (x == 5) {");

        String result = formatter.formatWarning(warning);

        String expected = "source.js:-1: WARNING - Warning Description\n" +
                "    if (x == 5) {\n";
        assertEquals(expected, result);
    }
}