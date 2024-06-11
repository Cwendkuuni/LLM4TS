import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setup() {
        formatter = new LightweightMessageFormatter(null);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("error description");
        String formattedError = formatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning description");
        String formattedWarning = formatter.formatWarning(warning);
        assertEquals("WARNING - warning description\n", formattedWarning);
    }

    @Test
    public void testFormatWithoutSource() {
        LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error description");
        String formattedError = messageFormatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }

    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("error description");
        String formattedError = messageFormatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }

    @Test
    public void testFormatWarningWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("warning description");
        String formattedWarning = messageFormatter.formatWarning(warning);
        assertEquals("WARNING - warning description\n", formattedWarning);
    }

    @Test
    public void testFormatErrorWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError error = new JSError("error description");
        String formattedError = messageFormatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }

    @Test
    public void testFormatWarningWithExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError warning = new JSError("warning description");
        String formattedWarning = messageFormatter.formatWarning(warning);
        assertEquals("WARNING - warning description\n", formattedWarning);
    }

    @Test
    public void testFormatErrorWithSourceAndExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError error = new JSError("error description");
        String formattedError = messageFormatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }

    @Test
    public void testFormatWarningWithSourceAndExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, SourceExcerpt.LINE);
        JSError warning = new JSError("warning description");
        String formattedWarning = messageFormatter.formatWarning(warning);
        assertEquals("WARNING - warning description\n", formattedWarning);
    }

    @Test
    public void testFormatWithoutSourceAndExcerpt() {
        LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("error description");
        String formattedError = messageFormatter.formatError(error);
        assertEquals("ERROR - error description\n", formattedError);
    }
}