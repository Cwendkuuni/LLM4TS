import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        // Typical use case
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError error = new JSError("source.js", 10, CheckLevel.ERROR, "This is an error");
        String errorMessage = formatter.formatError(error);
        assertNotNull(errorMessage);
        assertTrue(errorMessage.contains("ERROR"));
        assertTrue(errorMessage.contains("This is an error"));

        // Edge case: Empty error description
        JSError emptyError = new JSError("source.js", 10, CheckLevel.ERROR, "");
        String emptyMessage = formatter.formatError(emptyError);
        assertNotNull(emptyMessage);
        assertFalse(emptyMessage.isEmpty());
    }

    @Test
    public void testFormatWarning() {
        // Typical use case
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        JSError warning = new JSError("source.js", 10, CheckLevel.WARNING, "This is a warning");
        String warningMessage = formatter.formatWarning(warning);
        assertNotNull(warningMessage);
        assertTrue(warningMessage.contains("WARNING"));
        assertTrue(warningMessage.contains("This is a warning"));

        // Edge case: Null source name
        JSError nullSource = new JSError(null, 10, CheckLevel.WARNING, "This is a warning");
        String nullSourceMessage = formatter.formatWarning(nullSource);
        assertNotNull(nullSourceMessage);
        assertFalse(nullSourceMessage.isEmpty());
    }

    @Test
    public void testWithoutSource() {
        // Typical use case
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        assertNotNull(formatter);
        assertEquals("LINE", formatter.getExcerpt());

        // Edge case: Pass null source
        LightweightMessageFormatter nullSourceFormatter = new LightweightMessageFormatter(null, null);
        assertNull(nullSourceFormatter.getSource());
    }

    @Test
    public void testDefaultConstructor() {
        // Typical use case
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        assertNotNull(formatter);
        assertNull(formatter.getExcerpt());
    }

    @Test
    public void testAdditionalConstructor() {
        // Typical use case
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        assertEquals(source, formatter.getSource());

        // Edge case: Null source
        LightweightMessageFormatter nullSourceFormatter = new LightweightMessageFormatter(null, null);
        assertNotNull(nullSourceFormatter);
        assertNull(nullSourceFormatter.getSource());
    }

}