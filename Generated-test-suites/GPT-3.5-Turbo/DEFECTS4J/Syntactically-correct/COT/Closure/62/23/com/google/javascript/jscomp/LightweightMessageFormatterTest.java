
import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error description");
        String expected = "ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("warning description");
        String expected = "WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error description");
        error.setSourceName("TestFile.js");
        error.setLineNumber(10);
        String expected = "TestFile.js:10: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithSource() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning description");
        warning.setSourceName("TestFile.js");
        warning.setLineNumber(10);
        String expected = "TestFile.js:10: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError error = new JSError("error description");
        error.setSourceName("TestFile.js");
        error.setLineNumber(10);
        error.setCharno(5);
        String expected = "TestFile.js:10: ERROR - error description\n" +
                          "     if (foo) {\n" +
                          "     ^\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarningWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError warning = new JSError("warning description");
        warning.setSourceName("TestFile.js");
        warning.setLineNumber(10);
        warning.setCharno(5);
        String expected = "TestFile.js:10: WARNING - warning description\n" +
                          "     if (foo) {\n" +
                          "     ^\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
}
