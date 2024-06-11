import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError error = new JSError("test description", "test source", 10);
        String expected = "test source:10: ERROR - test description\n";
        String result = formatter.formatError(error);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarning() {
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        JSError warning = new JSError("test description", "test source", 10);
        String expected = "test source:10: WARNING - test description\n";
        String result = formatter.formatWarning(warning);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("test description", "test source", 10);
        String expected = "ERROR - test description\n";
        String result = formatter.formatError(error);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("test description", "test source", 10);
        String expected = "WARNING - test description\n";
        String result = formatter.formatWarning(warning);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("test description", "test source", 10);
        String expected = "test source:10: ERROR - test description\n";
        String result = formatter.formatError(error);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarningWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("test description", "test source", 10);
        String expected = "test source:10: WARNING - test description\n";
        String result = formatter.formatWarning(warning);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatErrorWithCustomExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = new SourceExcerpt();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        JSError error = new JSError("test description", "test source", 10);
        String expected = "test source:10: ERROR - test description\n";
        String result = formatter.formatError(error);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarningWithCustomExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = new SourceExcerpt();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        JSError warning = new JSError("test description", "test source", 10);
        String expected = "test source:10: WARNING - test description\n";
        String result = formatter.formatWarning(warning);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatErrorWithNoSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = null;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        JSError error = new JSError("test description", "test source", 10);
        String expected = "test source:10: ERROR - test description\n";
        String result = formatter.formatError(error);
        assertEquals(expected, result);
    }

    @Test
    public void testFormatWarningWithNoSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = null;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        JSError warning = new JSError("test description", "test source", 10);
        String expected = "test source:10: WARNING - test description\n";
        String result = formatter.formatWarning(warning);
        assertEquals(expected, result);
    }
}