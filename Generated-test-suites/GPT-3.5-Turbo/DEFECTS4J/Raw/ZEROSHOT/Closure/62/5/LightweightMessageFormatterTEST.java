import com.google.javascript.jscomp.JSError;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        formatter = new LightweightMessageFormatter();
    }

    @Test
    public void testFormatError() {
        JSError error = createError("Test Error", "file.js", 10);
        String expected = "file.js:10: ERROR - Test Error\n";
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = createError("Test Warning", "file.js", 5);
        String expected = "file.js:5: WARNING - Test Warning\n";
        String actual = formatter.formatWarning(warning);
        assertEquals(expected, actual);
    }

    private JSError createError(String description, String sourceName, int lineNumber) {
        JSError error = new JSError(description);
        error.setSourceName(sourceName);
        error.setLineNumber(lineNumber);
        return error;
    }
}