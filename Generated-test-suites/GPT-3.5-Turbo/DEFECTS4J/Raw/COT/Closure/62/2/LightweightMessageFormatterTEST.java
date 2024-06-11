
import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError error = new JSError("Error message", "source.js", 10, CheckLevel.ERROR);
        String expected = "source.js:10: ERROR - Error message\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError warning = new JSError("Warning message", "source.js", 20, CheckLevel.WARNING);
        String expected = "source.js:20: WARNING - Warning message\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Error message", "source.js", 10, CheckLevel.ERROR);
        String expected = "ERROR - Error message\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWithSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public String getSourceExcerpt(String sourceName, int lineNumber) {
                return "if (foo) {\n" +
                        "  alert('bar');\n" +
                        "}";
            }
        };
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Error message", "source.js", 10, CheckLevel.ERROR);
        String expected = "source.js:10: ERROR - Error message\n" +
                "if (foo) {\n" +
                "  alert('bar');\n" +
                "}\n" +
                "                ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

}
