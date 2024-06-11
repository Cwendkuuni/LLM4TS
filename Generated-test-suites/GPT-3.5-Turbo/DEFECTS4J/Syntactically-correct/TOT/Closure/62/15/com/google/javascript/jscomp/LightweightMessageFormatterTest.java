import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Mock
    private SourceExcerptProvider sourceExcerptProvider;

    @Before
    public void setUp() {
        sourceExcerptProvider = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(sourceExcerptProvider);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("test error");
        String formattedError = formatter.formatError(error);
        String expectedError = "ERROR - test error\n";
        assertEquals(expectedError, formattedError);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("test warning");
        String formattedWarning = formatter.formatWarning(warning);
        String expectedWarning = "WARNING - test warning\n";
        assertEquals(expectedWarning, formattedWarning);
    }

    @Test
    public void testFormatErrorWithSource() {
        JSError error = new JSError("test error with source");
        error.setSourceName("test.js");
        error.setLineNumber(10);
        String formattedError = formatter.formatError(error);
        String expectedError = "test.js:10: ERROR - test error with source\n";
        assertEquals(expectedError, formattedError);
    }

    @Test
    public void testFormatWarningWithSource() {
        JSError warning = new JSError("test warning with source");
        warning.setSourceName("test.js");
        warning.setLineNumber(5);
        String formattedWarning = formatter.formatWarning(warning);
        String expectedWarning = "test.js:5: WARNING - test warning with source\n";
        assertEquals(expectedWarning, formattedWarning);
    }

    @Test
    public void testFormatErrorWithSourceAndExcerpt() {
        JSError error = new JSError("test error with source and excerpt");
        error.setSourceName("test.js");
        error.setLineNumber(10);
        String sourceExcerpt = "function test() {\n  console.log('test');";
        error.setSourceExcerpt(sourceExcerpt);
        String formattedError = formatter.formatError(error);
        String expectedError = "test.js:10: ERROR - test error with source and excerpt\n" +
                "function test() {\n  console.log('test');\n" +
                "        ^\n";
        assertEquals(expectedError, formattedError);
    }

    @Test
    public void testFormatWarningWithSourceAndExcerpt() {
        JSError warning = new JSError("test warning with source and excerpt");
        warning.setSourceName("test.js");
        warning.setLineNumber(5);
        String sourceExcerpt = "console.log('test');\nalert('warning');";
        warning.setSourceExcerpt(sourceExcerpt);
        String formattedWarning = formatter.formatWarning(warning);
        String expectedWarning = "test.js:5: WARNING - test warning with source and excerpt\n" +
                "console.log('test');\nalert('warning');\n" +
                "      ^\n";
        assertEquals(expectedWarning, formattedWarning);
    }

}