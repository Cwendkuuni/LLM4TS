
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class LightweightMessageFormatterTest {

    private SourceExcerptProvider sourceExcerptProvider;
    private SourceExcerpt sourceExcerpt;

    @Before
    public void setUp() {
        sourceExcerptProvider = new SourceExcerptProviderStub();
        sourceExcerpt = new SourceExcerpt();
    }

    @Test
    public void testFormatError_withoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Error message");

        String formattedError = formatter.formatError(error);

        assertEquals("ERROR - Error message\n", formattedError);
    }

    @Test
    public void testFormatWarning_withoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Warning message");

        String formattedWarning = formatter.formatWarning(warning);

        assertEquals("WARNING - Warning message\n", formattedWarning);
    }

    @Test
    public void testFormatError_withSource() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider);
        JSError error = new JSError("Error message", "source.js", 10);

        String formattedError = formatter.formatError(error);

        assertEquals("source.js:10: ERROR - Error message\n", formattedError);
    }

    @Test
    public void testFormatWarning_withSource() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider);
        JSError warning = new JSError("Warning message", "source.js", 5);

        String formattedWarning = formatter.formatWarning(warning);

        assertEquals("source.js:5: WARNING - Warning message\n", formattedWarning);
    }

    @Test
    public void testFormatError_withExcerpt() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, sourceExcerpt);
        JSError error = new JSError("Error message", "source.js", 5);

        String formattedError = formatter.formatError(error);

        assertEquals("source.js:5: ERROR - Error message\nLine 1\n\n", formattedError);
    }

    @Test
    public void testFormatWarning_withExcerpt() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, sourceExcerpt);
        JSError warning = new JSError("Warning message", "source.js", 10);

        String formattedWarning = formatter.formatWarning(warning);

        assertEquals("source.js:10: WARNING - Warning message\nLine 1\n\n", formattedWarning);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_withNullSourceExcerptProvider() {
        new LightweightMessageFormatter(null);
    }

    @Test
    public void testFormatError_withEmptySourceExcerpt() {
        sourceExcerpt.setSourceExcerpt("");
        sourceExcerptProvider.setExcerpt(sourceExcerpt);

        LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider);
        JSError error = new JSError("Error message", "source.js", 5);

        String formattedError = formatter.formatError(error);

        assertEquals("source.js:5: ERROR - Error message\n", formattedError);
    }

    private static class SourceExcerptProviderStub implements SourceExcerptProvider {

        private SourceExcerpt sourceExcerpt;

        public void setExcerpt(SourceExcerpt sourceExcerpt) {
            this.sourceExcerpt = sourceExcerpt;
        }

        @Override
        public SourceExcerpt getSourceExcerpt(String filePath) {
            return sourceExcerpt;
        }
    }
}
