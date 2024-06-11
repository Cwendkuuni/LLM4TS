import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError error = new JSError("error description", "sourceName", 10);
        String expectedResult = "sourceName:10: ERROR - error description\n";
        String actualResult = formatter.formatError(error);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarning() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError warning = new JSError("warning description", "sourceName", 5);
        String expectedResult = "sourceName:5: WARNING - warning description\n";
        String actualResult = formatter.formatWarning(warning);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatErrorWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        JSError error = new JSError("error description", "sourceName", 10);
        String expectedResult = "ERROR - error description\n";
        String actualResult = formatter.formatError(error);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarningWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        JSError warning = new JSError("warning description", "sourceName", 5);
        String expectedResult = "WARNING - warning description\n";
        String actualResult = formatter.formatWarning(warning);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWithSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProviderImpl();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError error = new JSError("error description", "sourceName", 10);
        String expectedResult = "sourceName:10: ERROR - error description\n" +
                "    if (foo) {\n" +
                "      alert('bar');\n" +
                "    }\n" +
                "    ^\n";
        String actualResult = formatter.formatError(error);

        assertEquals(expectedResult, actualResult);
    }

    private static class SourceExcerptProviderImpl implements SourceExcerptProvider {

        @Override
        public String getSourceExcerpt(String sourceName, int lineNumber) {
            return "if (foo) {\n" +
                    "  alert('bar');\n" +
                    "}";
        }
    }
}