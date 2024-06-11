
import org.junit.Test;
import static org.junit.Assert.*;

// Class to be tested
public class LightweightMessageFormatterTest {

    // Test case for formatError method
    @Test
    public void testFormatError() {
        JSError error = new JSError("SyntaxError", "syntax error at line 10", "test.js", 10);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatError(error);
        String expected = "test.js:10: ERROR - SyntaxError\n" +
                          "syntax error at line 10\n";
        assertEquals(expected, result);
    }

    // Test case for formatWarning method
    @Test
    public void testFormatWarning() {
        JSError warning = new JSError("UnusedVariable", "unused variable 'x'", "test.js", 5);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.formatWarning(warning);
        String expected = "test.js:5: WARNING - UnusedVariable\n" +
                          "unused variable 'x'\n";
        assertEquals(expected, result);
    }

    // Test case for constructor with source and excerpt
    @Test
    public void testConstructorWithSourceAndExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        SourceExcerpt excerpt = new SourceExcerpt();
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, excerpt);
        assertNotNull(formatter);
    }

    // Test case for withoutSource method
    @Test
    public void testWithoutSource() {
        LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
        assertNotNull(formatter);
    }

    // Test case for format method with error
    @Test
    public void testFormatWithError() {
        JSError error = new JSError("TypeError", "Type mismatch", "test.js", 20);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.format(error, false);
        String expected = "test.js:20: ERROR - TypeError\n" +
                          "Type mismatch\n";
        assertEquals(expected, result);
    }

    // Test case for format method with warning
    @Test
    public void testFormatWithWarning() {
        JSError warning = new JSError("UnusedFunction", "unused function 'foo'", "test.js", 15);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter();
        String result = formatter.format(warning, true);
        String expected = "test.js:15: WARNING - UnusedFunction\n" +
                          "unused function 'foo'\n";
        assertEquals(expected, result);
    }

    // Test case for format method with source excerpt
    @Test
    public void testFormatWithSourceExcerpt() {
        SourceExcerptProvider source = new SourceExcerptProvider();
        source.setCode("function foo() {\n    console.log('hello world');\n}");
        JSError error = new JSError("SyntaxError", "syntax error", "test.js", 5);
        LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
        String result = formatter.format(error, false);
        String expected = "test.js:5: ERROR - SyntaxError\n" +
                          "syntax error\n" +
                          "function foo() {\n" +
                          "        console.log('hello world');\n" +
                          "}\n" +
                          "              ^\n";
        assertEquals(expected, result);
    }

    // Test case for LineNumberingFormatter formatLine method
    @Test
    public void testLineNumberingFormatterFormatLine() {
        LineNumberingFormatter formatter = new LineNumberingFormatter();
        String line = "console.log('hello world');";
        int lineNumber = 5;
        String result = formatter.formatLine(line, lineNumber);
        assertEquals(line, result);
    }

    // Test case for LineNumberingFormatter formatRegion method
    @Test
    public void testLineNumberingFormatterFormatRegion() {
        Region region = new Region("function foo() {\n    console.log('hello world');\n}");
        region.setBeginningLineNumber(5);
        region.setEndingLineNumber(6);
        region.setSourceExcerpt("function foo() {\n    console.log('hello world');\n}");
        LineNumberingFormatter formatter = new LineNumberingFormatter();
        String result = formatter.formatRegion(region);
        String expected = "  5| function foo() {\n" +
                          "    6|     console.log('hello world');";
        assertEquals(expected, result);
    }

}
