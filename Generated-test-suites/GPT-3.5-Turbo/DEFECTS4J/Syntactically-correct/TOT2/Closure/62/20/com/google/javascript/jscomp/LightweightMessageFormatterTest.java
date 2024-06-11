import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl());
        JSError error = new JSError("Example Error", "sourceName", 1);
        String formattedError = formatter.formatError(error);
        assertEquals("ERROR - Example Error\n", formattedError);
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProviderImpl());
        JSError warning = new JSError("Example Warning", "sourceName", 1);
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("WARNING - Example Warning\n", formattedWarning);
    }

    @Test
    public void testFormatErrorWithSourceExcerpt() {
        // Test case with source excerpt provided
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Example Error", "sourceName", 1, 5);
        String formattedError = formatter.formatError(error);
        // Assert the formatted error message with source excerpt
        assertEquals("sourceName:1: ERROR - Example Error\nSourceExcerpt\n     ^\n", formattedError);
    }

    @Test
    public void testFormatWarningWithSourceExcerpt() {
        // Test case with source excerpt provided
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Example Warning", "sourceName", 1, 5);
        String formattedWarning = formatter.formatWarning(warning);
        // Assert the formatted warning message with source excerpt
        assertEquals("sourceName:1: WARNING - Example Warning\nSourceExcerpt\n     ^\n", formattedWarning);
    }
}