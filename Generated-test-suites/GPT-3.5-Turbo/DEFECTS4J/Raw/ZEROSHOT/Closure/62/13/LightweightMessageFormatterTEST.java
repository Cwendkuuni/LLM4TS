import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setup() {
        formatter = new LightweightMessageFormatter(null);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("error", "sourceName", 1, 0);
        String expectedOutput = "sourceName:1: ERROR - error\n";
        assertEquals(expectedOutput, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning", "sourceName", 2, 0);
        String expectedOutput = "sourceName:2: WARNING - warning\n";
        assertEquals(expectedOutput, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatWithoutSource() {
        LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error", "sourceName", 1, 0);
        String expectedOutput = "ERROR - error\n";
        assertEquals(expectedOutput, formatterWithoutSource.formatError(error));
    }
}