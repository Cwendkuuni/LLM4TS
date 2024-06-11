import org.junit.Test;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError_withSourceExcerptProvider_shouldReturnFormattedErrorMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError error = new JSError("error message", "test.js", 10);
        String expectedResult = "test.js:10: ERROR - error message\n";
        String actualResult = formatter.formatError(error);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatError_withoutSourceExcerptProvider_shouldReturnFormattedErrorMessage() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        JSError error = new JSError("error message", "test.js", 10);
        String expectedResult = "ERROR - error message\n";
        String actualResult = formatter.formatError(error);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarning_withSourceExcerptProvider_shouldReturnFormattedWarningMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError warning = new JSError("warning message", "test.js", 10);
        String expectedResult = "test.js:10: WARNING - warning message\n";
        String actualResult = formatter.formatWarning(warning);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarning_withoutSourceExcerptProvider_shouldReturnFormattedWarningMessage() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        JSError warning = new JSError("warning message", "test.js", 10);
        String expectedResult = "WARNING - warning message\n";
        String actualResult = formatter.formatWarning(warning);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormat_withSourceExcerptProvider_shouldReturnFormattedMessage() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);

        JSError error = new JSError("error message", "test.js", 10);
        String expectedResult = "test.js:10: ERROR - error message\n";
        String actualResult = formatter.format(error, false);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormat_withoutSourceExcerptProvider_shouldReturnFormattedMessage() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

        JSError error = new JSError("error message", "test.js", 10);
        String expectedResult = "ERROR - error message\n";
        String actualResult = formatter.format(error, false);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLineNumberingFormatter_formatLine_shouldReturnFormattedLine() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();

        String line = "if (foo) {";
        int lineNumber = 10;
        String expectedResult = "if (foo) {";
        String actualResult = formatter.formatLine(line, lineNumber);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLineNumberingFormatter_formatRegion_withValidRegion_shouldReturnFormattedRegion() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();

        int beginningLineNumber = 10;
        int endingLineNumber = 12;
        String sourceExcerpt = "if (foo) {\n  alert('bar');\n}";
        Region region = new Region(beginningLineNumber, endingLineNumber, sourceExcerpt);

        String expectedResult = "  10| if (foo) {\n  11|   alert('bar');\n  12| }";
        String actualResult = formatter.formatRegion(region);

        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLineNumberingFormatter_formatRegion_withEmptyRegion_shouldReturnNull() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();

        Region region = new Region(10, 10, "");
        String actualResult = formatter.formatRegion(region);

        Assert.assertNull(actualResult);
    }

    @Test
    public void testLineNumberingFormatter_formatRegion_withoutRegion_shouldReturnNull() {
        LightweightMessageFormatter.LineNumberingFormatter formatter = new LightweightMessageFormatter.LineNumberingFormatter();

        Region region = null;
        String actualResult = formatter.formatRegion(region);

        Assert.assertNull(actualResult);
    }

}