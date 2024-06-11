import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LightweightMessageFormatterTest {
    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("error description", "sourceName", 3);
        String expected = "sourceName:3: ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("warning description", "sourceName", 5);
        String expected = "sourceName:5: WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
    
    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error description", null, 0);
        String expected = "ERROR - error description\n";
        assertEquals(expected, formatter.formatError(error));
    }
    
    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("warning description", null, 0);
        String expected = "WARNING - warning description\n";
        assertEquals(expected, formatter.formatWarning(warning));
    }
}