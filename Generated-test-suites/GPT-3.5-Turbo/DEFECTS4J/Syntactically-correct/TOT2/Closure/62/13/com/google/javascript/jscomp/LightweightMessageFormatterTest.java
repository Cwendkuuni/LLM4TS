
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setup() {
        SourceExcerptProvider source = new SourceExcerptProvider() {
            @Override
            public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber, ExcerptFormatter formatter) {
                return new SourceExcerpt() {
                    @Override
                    public String get(SourceExcerptProvider sourceProvider, String sourceName, int lineNumber, ExcerptFormatter formatter) {
                        return "Sample source excerpt";
                    }
                };
            }
        };
        formatter = new LightweightMessageFormatter(source);
    }

    @Test
    public void formatError_Test() {
        JSError error = new JSError("Sample error", "Sample Source", 5, 10);
        String formattedError = formatter.formatError(error);
        
        assertNotNull(formattedError);
        assertEquals("ERROR - Sample error\nSample source excerpt\n", formattedError);
    }

    @Test
    public void formatWarning_Test() {
        JSError warning = new JSError("Sample warning", "Sample Source", 8, 15);
        String formattedWarning = formatter.formatWarning(warning);

        assertNotNull(formattedWarning);
        assertEquals("WARNING - Sample warning\nSample source excerpt\n", formattedWarning);
    }

    @Test
    public void format_ErrorWithoutSource_Test() {
        LightweightMessageFormatter formatterWithoutSource = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Error without source");
        String formattedErrorWithoutSource = formatterWithoutSource.formatError(error);

        assertNotNull(formattedErrorWithoutSource);
        assertEquals("ERROR - Error without source\n", formattedErrorWithoutSource);
    }

    // Add more test cases for edge cases and error scenarios if needed
}
