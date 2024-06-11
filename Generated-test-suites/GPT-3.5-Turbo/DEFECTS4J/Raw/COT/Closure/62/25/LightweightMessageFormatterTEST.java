import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;
    private SourceExcerptProvider source;

    @Before
    public void setUp() {
        source = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(source);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("error");
        error.setLineNumber(10);
        error.setSourceName("sample.js");
        when(source.getSourceExcerpt("sample.js", 10)).thenReturn("line 10");
        String expected = "sample.js:10: ERROR - error\nline 10\n";
        Assert.assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("warning");
        warning.setLineNumber(5);
        warning.setSourceName("sample.js");
        when(source.getSourceExcerpt("sample.js", 5)).thenReturn("line 5");
        String expected = "sample.js:5: WARNING - warning\nline 5\n";
        Assert.assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithoutSource() {
        JSError error = new JSError("error");
        String expected = "ERROR - error\n";
        Assert.assertEquals(expected, LightweightMessageFormatter.withoutSource().formatError(error));
    }

    @Test
    public void testFormatWarningWithoutSource() {
        JSError warning = new JSError("warning");
        String expected = "WARNING - warning\n";
        Assert.assertEquals(expected, LightweightMessageFormatter.withoutSource().formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullSource() {
        JSError error = new JSError("error");
        error.setLineNumber(10);
        String expected = "ERROR - error\n";
        Assert.assertEquals(expected, new LightweightMessageFormatter(null).formatError(error));
    }

    @Test
    public void testFormatWarningWithNullSource() {
        JSError warning = new JSError("warning");
        warning.setLineNumber(5);
        String expected = "WARNING - warning\n";
        Assert.assertEquals(expected, new LightweightMessageFormatter(null).formatWarning(warning));
    }

    @Test
    public void testFormatErrorWithNullSourceName() {
        JSError error = new JSError("error");
        error.setLineNumber(10);
        String expected = "10: ERROR - error\n";
        Assert.assertEquals(expected, new LightweightMessageFormatter(source).formatError(error));
    }

    @Test
    public void testFormatWarningWithNullSourceName() {
        JSError warning = new JSError("warning");
        warning.setLineNumber(5);
        String expected = "5: WARNING - warning\n";
        Assert.assertEquals(expected, new LightweightMessageFormatter(source).formatWarning(warning));
    }
}


// Add individual test cases here

