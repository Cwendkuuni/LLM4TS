
import org.junit.*;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        JSError error = new JSError("Error description", "source", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatError(error);
        String expected = "source:10: ERROR - Error description\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Warning description", "source", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatWarning(warning);
        String expected = "source:10: WARNING - Warning description\n";
        assertEquals(expected, result);
    }
}
