import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class LightweightMessageFormatterTest {

    private SourceExcerptProvider sourceMock;
    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        sourceMock = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(sourceMock);
    }

    // Test formatError() method
    @Test
    public void testFormatError() {
        JSError error = new JSError("Test Error", "filename.js", 10);
        error.setCharno(5);
        when(sourceMock.getLine("filename.js", 10)).thenReturn("Test line of code");

        String expected = "filename.js:10: ERROR - Test Error\n" +
                "Test line of code\n" +
                "     ^\n";
        String result = formatter.formatError(error);

        assertEquals(expected, result);
    }

    // Test formatWarning() method
    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("Test Warning", "filename.js", 15);
        warning.setCharno(3);
        when(sourceMock.getLine("filename.js", 15)).thenReturn("Another test line of code");

        String expected = "filename.js:15: WARNING - Test Warning\n" +
                "Another test line of code\n" +
                "   ^\n";
        String result = formatter.formatWarning(warning);

        assertEquals(expected, result);
    }

}