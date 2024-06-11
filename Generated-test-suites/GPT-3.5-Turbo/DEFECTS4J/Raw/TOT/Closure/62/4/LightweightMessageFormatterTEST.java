import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testWithoutSourceConstructor() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        assertNotNull(formatter);
    }

    @Test
    public void testFormatError() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Test Error", "test.js", 10, CheckLevel.ERROR);
        String expectedResult = "test.js:10: ERROR - Test Error\n";
        String actualResult = formatter.formatError(error);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarning() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Test Warning", "test.js", 5, CheckLevel.WARNING);
        String expectedResult = "test.js:5: WARNING - Test Warning\n";
        String actualResult = formatter.formatWarning(warning);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatErrorWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt.Builder excerptBuilder = new SourceExcerpt.Builder();
        excerptBuilder.addSourceLine("console.log('Error occurred');");
        source.setSourceExcerpt("test.js", excerptBuilder.build());
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError error = new JSError("Test Error", "test.js", 10, CheckLevel.ERROR, 11);
        String expectedResult = "test.js:10: ERROR - Test Error\nconsole.log('Error occurred');\n          ^\n";
        String actualResult = formatter.formatError(error);
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testFormatWarningWithSource() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt.Builder excerptBuilder = new SourceExcerpt.Builder();
        excerptBuilder.addSourceLine("console.log('Something might be wrong');");
        source.setSourceExcerpt("test.js", excerptBuilder.build());
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        JSError warning = new JSError("Test Warning", "test.js", 5, CheckLevel.WARNING, 6);
        String expectedResult = "test.js:5: WARNING - Test Warning\nconsole.log('Something might be wrong');\n         ^\n";
        String actualResult = formatter.formatWarning(warning);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatErrorWithoutSourceProvider() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError error = new JSError("Test Error", "test.js", 10, CheckLevel.ERROR);
        String expectedResult = "ERROR - Test Error\n";
        String actualResult = formatter.formatError(error);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFormatWarningWithoutSourceProvider() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        JSError warning = new JSError("Test Warning", "test.js", 5, CheckLevel.WARNING);
        String expectedResult = "WARNING - Test Warning\n";
        String actualResult = formatter.formatWarning(warning);
        assertEquals(expectedResult, actualResult);
    }

}