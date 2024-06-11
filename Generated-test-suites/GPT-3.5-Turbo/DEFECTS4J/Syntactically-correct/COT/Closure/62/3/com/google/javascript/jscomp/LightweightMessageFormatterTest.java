
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
        doReturn("Test Source").when(sourceProvider)
                .getSourceExcerpt(anyString(), anyInt(), any(com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter.class));
        formatter = new LightweightMessageFormatter(sourceProvider);
    }

    @Test
    public void testFormatError_noSource() {
        JSError error = new JSError("Error Description", null, -1);
        String expected = "ERROR - Error Description\n";
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatError_withSource() {
        JSError error = new JSError("Error Description", "TestSource", 10);
        String expected = "TestSource:10: ERROR - Error Description\n";
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWarning_noSource() {
        JSError warning = new JSError("Warning Description", null, -1);
        String expected = "WARNING - Warning Description\n";
        String actual = formatter.formatWarning(warning);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatWarning_withSource() {
        JSError warning = new JSError("Warning Description", "TestSource", 10);
        String expected = "TestSource:10: WARNING - Warning Description\n";
        String actual = formatter.formatWarning(warning);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatError_withSourceExcerpt() {
        JSError error = new JSError("Error Description", "TestSource", 10);
        String expected = "TestSource:10: ERROR - Error Description\n";
        expected += "Test Source\n";
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);
    }

    @Test
    public void testFormatError_withSourceExcerptAndArrow() {
        JSError error = new JSError("Error Description", "TestSource", 10);
        String expected = "TestSource:10: ERROR - Error Description\n";
        expected += "Test Source\n";
        expected += "                          ^\n";
        String actual = formatter.formatError(error);
        assertEquals(expected, actual);
    }
}
