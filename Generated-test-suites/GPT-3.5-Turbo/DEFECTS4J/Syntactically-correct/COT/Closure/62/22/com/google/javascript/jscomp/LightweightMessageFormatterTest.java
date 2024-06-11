
import org.junit.Test;

import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        JSError error = new JSError("error description");
        error.setSourceName("source.js");
        error.setLineNumber(10);

        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatError(error);

        String expected = "source.js:10: ERROR - error description\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning description");
        warning.setSourceName("source.js");
        warning.setLineNumber(5);

        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatWarning(warning);

        String expected = "source.js:5: WARNING - warning description\n";
        assertEquals(expected, result);
    }

}
