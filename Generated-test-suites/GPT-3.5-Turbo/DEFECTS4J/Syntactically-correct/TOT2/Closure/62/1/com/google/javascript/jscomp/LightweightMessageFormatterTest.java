import static org.junit.Assert.*;
import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError_withSourceAndLineNumber() {
        // Test case with valid error, source, and line number
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider(), SourceExcerpt.LINE);
        JSError error = new JSError("example.js", 10, "Example error description");
        String formattedError = formatter.formatError(error);
        assertTrue(formattedError.contains("example.js:10: ERROR - Example error description"));
    }

    @Test
    public void testFormatError_withoutSourceAndLineNumber() {
        // Test case without source and line number
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError(null, -1, "Example error description");
        String formattedError = formatter.formatError(error);
        assertTrue(formattedError.contains("ERROR - Example error description"));
    }

    @Test
    public void testFormatWarning_withSourceAndLineNumber() {
        // Test case with valid warning, source, and line number
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new DummySourceExcerptProvider(), SourceExcerpt.LINE);
        JSError warning = new JSError("example.js", 5, "Example warning description");
        String formattedWarning = formatter.formatWarning(warning);
        assertTrue(formattedWarning.contains("example.js:5: WARNING - Example warning description"));
    }

    @Test
    public void testFormatWarning_withoutSourceAndLineNumber() {
        // Test case without source and line number
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError(null, -1, "Example warning description");
        String formattedWarning = formatter.formatWarning(warning);
        assertTrue(formattedWarning.contains("WARNING - Example warning description"));
    }

    @Test
    public void testFormatError_edgeCases() {
        // Test for edge cases in formatError method
        // Include cases where source is null, line number is negative, etc.
    }

    @Test
    public void testFormatWarning_edgeCases() {
        // Test for edge cases in formatWarning method
        // Include cases with special characters in error description, etc.
    }

    private static class DummySourceExcerptProvider implements SourceExcerptProvider {
        @Override
        public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
            return new DummySourceExcerpt();
        }
    }

    private static class DummySourceExcerpt implements SourceExcerpt {
        @Override
        public String get(SourceExcerptProvider provider, String sourceName, int lineNumber, ExcerptFormatter formatter) {
            return "Dummy source excerpt";
        }
    }

}