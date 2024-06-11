import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        // Typical Use Case
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        JSError error = new JSError("Error occurred", "test.js", 10, CheckLevel.ERROR);
        String formattedError = formatter.formatError(error);
        assertEquals("test.js:10: ERROR - Error occurred\n", formattedError);

        // Edge Case
        // Testing for null source
        LightweightMessageFormatter formatterNullSource = new LightweightMessageFormatter();
        JSError errorNullSource = new JSError("Another error", null, 0, CheckLevel.ERROR);
        String formattedErrorNullSource = formatterNullSource.formatError(errorNullSource);
        assertEquals("ERROR - Another error\n", formattedErrorNullSource);

        // Error Scenario
        // Testing for source excerpt being null
        LightweightMessageFormatter formatterNullExcerpt = new LightweightMessageFormatter(new SourceExcerptProvider(), null);
        JSError errorNullExcerpt = new JSError("Error", "test.js", 5, CheckLevel.ERROR);
        String formattedErrorNullExcerpt = formatterNullExcerpt.formatError(errorNullExcerpt);
        assertEquals("test.js:5: ERROR - Error\n", formattedErrorNullExcerpt);
    }

    @Test
    public void testFormatWarning() {
        // Typical Use Case
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider());
        JSError warning = new JSError("Warning message", "test.js", 5, CheckLevel.WARNING);
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("test.js:5: WARNING - Warning message\n", formattedWarning);

        // Edge Case
        // Testing for line number being 0
        JSError warningZeroLineNumber = new JSError("Zero line number", "test.js", 0, CheckLevel.WARNING);
        String formattedWarningZeroLineNumber = formatter.formatWarning(warningZeroLineNumber);
        assertEquals("test.js:0: WARNING - Zero line number\n", formattedWarningZeroLineNumber);

        // Error Scenario
        // Testing for source being null
        LightweightMessageFormatter formatterNullSource = new LightweightMessageFormatter();
        JSError warningNullSource = new JSError("Another warning", null, 10, CheckLevel.WARNING);
        String formattedWarningNullSource = formatterNullSource.formatWarning(warningNullSource);
        assertEquals("WARNING - Another warning\n", formattedWarningNullSource);
    }

}