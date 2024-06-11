import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        // Typical use case
        // cover the format of error message
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError error = new JSError("test-error", 1, "This is a test error message");
        String result = formatter.formatError(error);
        assertEquals("ERROR - This is a test error message\n", result);

        // Edge case
        // when sourceName is null
        error.sourceName = null;
        result = formatter.formatError(error);
        assertEquals("ERROR - This is a test error message\n", result);

        // Error scenario
        // when source is null
        formatter = new LightweightMessageFormatter(null, null);
        result = formatter.formatError(error);
        assertEquals("ERROR - This is a test error message\n", result);
    }

    @Test
    public void testFormatWarning() {
        // Typical use case
        // cover the format of warning message
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError warning = new JSError("test-warning", 1, "This is a test warning message");
        String result = formatter.formatWarning(warning);
        assertEquals("WARNING - This is a test warning message\n", result);

        // Edge case
        // when lineNumber is 0
        warning.lineNumber = 0;
        result = formatter.formatWarning(warning);
        assertEquals("WARNING - This is a test warning message\n", result);

        // Error scenario
        // when source is null
        formatter = new LightweightMessageFormatter(null, null);
        result = formatter.formatWarning(warning);
        assertEquals("WARNING - This is a test warning message\n", result);
    }

    // Additional test cases for other methods can be added here

}