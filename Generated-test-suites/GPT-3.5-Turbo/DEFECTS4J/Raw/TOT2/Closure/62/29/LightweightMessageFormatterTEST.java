import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.JSError;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        formatter = new LightweightMessageFormatter();
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("Test error", "test.js", 10, 5);
        String expected = "test.js:10: ERROR - Test error\n";
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test warning", "test.js", 5, 8);
        String expected = "test.js:5: WARNING - Test warning\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatWithoutSource() {
        LightweightMessageFormatter lmf = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test error", "test.js", 10, 5);
        String expected = "ERROR - Test error\n";
        assertEquals(expected, lmf.formatError(error));
    }

    // Additional test cases for edge cases and error scenarios can be added here

}