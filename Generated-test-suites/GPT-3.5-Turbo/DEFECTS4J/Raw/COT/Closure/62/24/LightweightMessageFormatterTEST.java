import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;



public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Test
    public void formatError_shouldReturnFormattedErrorMessage() {
        JSError error = new JSError("Error", "filename", 1, 1, null);
        String expected = "filename:1: ERROR - Error\n";
        formatter = new LightweightMessageFormatter(mockSource(), mock(SourceExcerpt.class));
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void formatWarning_shouldReturnFormattedWarningMessage() {
        JSError warning = new JSError("Warning", "filename", 1, 1, null);
        String expected = "filename:1: WARNING - Warning\n";
        formatter = new LightweightMessageFormatter(mockSource(), mock(SourceExcerpt.class));
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void formatError_withNullSource_shouldReturnFormattedErrorMessageWithoutSource() {
        JSError error = new JSError("Error", null, 0, 0, null);
        String expected = "ERROR - Error\n";
        formatter = LightweightMessageFormatter.withoutSource();
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void formatWarning_withNullSource_shouldReturnFormattedWarningMessageWithoutSource() {
        JSError warning = new JSError("Warning", null, 0, 0, null);
        String expected = "WARNING - Warning\n";
        formatter = LightweightMessageFormatter.withoutSource();
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void formatError_withNullSourceExcerpt_shouldReturnFormattedErrorMessageWithoutSourceExcerpt() {
        JSError error = new JSError("Error", "filename", 1, 1, null);
        String expected = "filename:1: ERROR - Error\n";
        formatter = new LightweightMessageFormatter(mockSource(), null);
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void formatWarning_withNullSourceExcerpt_shouldReturnFormattedWarningMessageWithoutSourceExcerpt() {
        JSError warning = new JSError("Warning", "filename", 1, 1, null);
        String expected = "filename:1: WARNING - Warning\n";
        formatter = new LightweightMessageFormatter(mockSource(), null);
        assertEquals(expected, formatter.formatWarning(warning));
    }

    @Test
    public void formatError_withSourceExcerpt_shouldReturnFormattedErrorMessageWithSourceExcerpt() {
        JSError error = new JSError("Error", "filename", 1, 1, null);
        String expected = "filename:1: ERROR - Error\nsource excerpt\n^\n";
        formatter = new LightweightMessageFormatter(mockSource(), mock(SourceExcerpt.class));
        assertEquals(expected, formatter.formatError(error));
    }

    @Test
    public void formatWarning_withSourceExcerpt_shouldReturnFormattedWarningMessageWithSourceExcerpt() {
        JSError warning = new JSError("Warning", "filename", 1, 1, null);
        String expected = "filename:1: WARNING - Warning\nsource excerpt\n^\n";
        formatter = new LightweightMessageFormatter(mockSource(), mock(SourceExcerpt.class));
        assertEquals(expected, formatter.formatWarning(warning));
    }

    private SourceExcerptProvider mockSource() {
        SourceExcerptProvider source = mock(SourceExcerptProvider.class);
        ExcerptFormatter formatter = mock(ExcerptFormatter.class);
        when(source.getSourceExcerpt("filename", 1, formatter)).thenReturn("source excerpt");
        return source;
    }
}

