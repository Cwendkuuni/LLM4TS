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
        JSError error = new JSError("Test error message", "TestSource", 10);
        String formattedError = formatter.formatError(error);
        assertEquals("ERROR - Test error message\n", formattedError);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test warning message", "TestSource", 5);
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("WARNING - Test warning message\n", formattedWarning);
    }

    @Test
    public void testFormatNoSource() {
        LightweightMessageFormatter noSourceFormatter = new LightweightMessageFormatter();
        JSError error = new JSError("Test error message", "TestSource", 10);
        String formattedError = noSourceFormatter.formatError(error);
        assertEquals("ERROR - Test error message\n", formattedError);
    }

    @Test
    public void testFormatRegion() {
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("Test code", 1, 3);
        String formattedRegion = LightweightMessageFormatter.LineNumberingFormatter.formatRegion(region);
        assertEquals("  1| Test code\n  2| \n  3| ", formattedRegion);
    }

    @Test
    public void testFormatLine() {
        String formattedLine = LightweightMessageFormatter.LineNumberingFormatter.formatLine("Test line", 1);
        assertEquals("Test line", formattedLine);
    }

}