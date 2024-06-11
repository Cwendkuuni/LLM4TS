import org.junit.Test;
import static org.junit.Assert.*;

public class LightweightMessageFormatterTest {

    @Test
    public void testFormatError_noSourceName_noLineNumber() {
        JSError error = new JSError("Some error");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatError(error);
        assertEquals("ERROR - Some error\n", result);
    }

    @Test
    public void testFormatError_withSourceName_noLineNumber() {
        JSError error = new JSError("Some error");
        error.sourceName = "testFile.js";
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatError(error);
        assertEquals("testFile.js: ERROR - Some error\n", result);
    }

    @Test
    public void testFormatError_withSourceName_withLineNumber() {
        JSError error = new JSError("Some error");
        error.sourceName = "testFile.js";
        error.lineNumber = 5;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatError(error);
        assertEquals("testFile.js:5: ERROR - Some error\n", result);
    }

    @Test
    public void testFormatWarning_noSourceName_noLineNumber() {
        JSError warning = new JSError("Some warning");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatWarning(warning);
        assertEquals("WARNING - Some warning\n", result);
    }

    @Test
    public void testFormatWarning_withSourceName_noLineNumber() {
        JSError warning = new JSError("Some warning");
        warning.sourceName = "testFile.js";
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatWarning(warning);
        assertEquals("testFile.js: WARNING - Some warning\n", result);
    }

    @Test
    public void testFormatWarning_withSourceName_withLineNumber() {
        JSError warning = new JSError("Some warning");
        warning.sourceName = "testFile.js";
        warning.lineNumber = 10;
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.formatWarning(warning);
        assertEquals("testFile.js:10: WARNING - Some warning\n", result);
    }

    @Test
    public void testFormat_noSourceExcerpt() {
        JSError error = new JSError("Some error");
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
        String result = formatter.format(error, false);
        assertEquals("ERROR - Some error\n", result);
    }

    @Test
    public void testFormat_withSourceExcerpt() {
        JSError error = new JSError("Some error");
        error.sourceName = "testFile.js";
        error.lineNumber = 5;
        SourceExcerptProvider source = new SourceExcerptProvider();
        String sourceCode = "var x = 5;\nvar y = 10;";
        source.setSourceCode(error.sourceName, sourceCode);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.format(error, false);
        String expected = "testFile.js:5: ERROR - Some error\n" +
                "var x = 5;\n" +
                "var y = 10;\n" +
                "     ^\n";
        assertEquals(expected, result);
    }

    @Test
    public void testFormatLine() {
        LightweightMessageFormatter.LineNumberingFormatter formatter =
                new LightweightMessageFormatter.LineNumberingFormatter();
        String line = "var x = 5;";
        String result = formatter.formatLine(line, 10);
        assertEquals(line, result);
    }

    @Test
    public void testFormatRegion_nullRegion() {
        LightweightMessageFormatter.LineNumberingFormatter formatter =
                new LightweightMessageFormatter.LineNumberingFormatter();
        String result = formatter.formatRegion(null);
        assertNull(result);
    }

    @Test
    public void testFormatRegion_emptyCode() {
        LightweightMessageFormatter.LineNumberingFormatter formatter =
                new LightweightMessageFormatter.LineNumberingFormatter();
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("", 1, 1);
        String result = formatter.formatRegion(region);
        assertNull(result);
    }

    @Test
    public void testFormatRegion_singleLineCode() {
        LightweightMessageFormatter.LineNumberingFormatter formatter =
                new LightweightMessageFormatter.LineNumberingFormatter();
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region("var x = 5;", 1, 1);
        String result = formatter.formatRegion(region);
        assertEquals("  1| var x = 5;", result);
    }

    @Test
    public void testFormatRegion_multiLineCode() {
        LightweightMessageFormatter.LineNumberingFormatter formatter =
                new LightweightMessageFormatter.LineNumberingFormatter();
        String code = "var x = 5;\nvar y = 10;";
        LightweightMessageFormatter.Region region = new LightweightMessageFormatter.Region(code, 1, 2);
        String result = formatter.formatRegion(region);
        String expected = "  1| var x = 5;\n" +
                "  2| var y = 10;";
        assertEquals(expected, result);
    }
}