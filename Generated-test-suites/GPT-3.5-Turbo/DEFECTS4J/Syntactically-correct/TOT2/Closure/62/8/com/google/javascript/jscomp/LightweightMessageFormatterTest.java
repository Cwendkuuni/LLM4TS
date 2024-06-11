import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.Region;
import com.google.javascript.jscomp.SourceExcerptProvider;

public class LightweightMessageFormatterTest {

    private LightweightMessageFormatter formatter;

    @Before
    public void setUp() {
        SourceExcerptProvider source = mock(SourceExcerptProvider.class);
        formatter = new LightweightMessageFormatter(source);
    }

    @Test
    public void testFormatError() {
        JSError error = new JSError("source", 10, CheckLevel.ERROR, "Test Error");
        String formattedError = formatter.formatError(error);
        assertNotNull(formattedError);
        assertEquals("source:10: ERROR - Test Error\n", formattedError);
    }

    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("source", 5, CheckLevel.WARNING, "Test Warning");
        String formattedWarning = formatter.formatWarning(warning);
        assertNotNull(formattedWarning);
        assertEquals("source:5: WARNING - Test Warning\n", formattedWarning);
    }

    @Test
    public void testWithoutSource() {
        LightweightMessageFormatter lightFormatter = LightweightMessageFormatter.withoutSource();
        assertNotNull(lightFormatter);
    }

    @Test
    public void testFormatRegion() {
        LightweightMessageFormatter.LineNumberingFormatter lineFormatter = new LightweightMessageFormatter.LineNumberingFormatter();

        Region region = new Region("Test Region", 1, 3);
        String formattedRegion = lineFormatter.formatRegion(region);
        assertNotNull(formattedRegion);
        assertEquals(" 1| Test Region\n 2\n 3", formattedRegion);
    }

    @Test
    public void testFormatRegionNull() {
        LightweightMessageFormatter.LineNumberingFormatter lineFormatter = new LightweightMessageFormatter.LineNumberingFormatter();
        
        String formattedNull = lineFormatter.formatRegion(null);
        assertEquals(null, formattedNull);
    }
}