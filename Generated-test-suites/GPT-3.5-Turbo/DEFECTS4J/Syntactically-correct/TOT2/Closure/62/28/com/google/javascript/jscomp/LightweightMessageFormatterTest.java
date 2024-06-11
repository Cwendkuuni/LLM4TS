import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import com.google.javascript.jscomp.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), SourceExcerpt.LINE);
        JSError error = new JSError("testSource", 1, CheckLevel.ERROR, "Test Error Message");
        String formattedError = formatter.formatError(error);
        assertNotNull(formattedError);
        assertEquals("testSource:1: ERROR - Test Error Message\n", formattedError);
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(new SourceExcerptProvider(), SourceExcerpt.LINE);
        JSError warning = new JSError("testSource", 1, CheckLevel.WARNING, "Test Warning Message");
        String formattedWarning = formatter.formatWarning(warning);
        assertNotNull(formattedWarning);
        assertEquals("testSource:1: WARNING - Test Warning Message\n", formattedWarning);
    }

    @Test
    public void testFormatWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("testSource", 1, CheckLevel.ERROR, "Test Error Message");
        String formattedError = formatter.formatError(error);
        assertNotNull(formattedError);
        assertEquals("ERROR - Test Error Message\n", formattedError);
    }
}