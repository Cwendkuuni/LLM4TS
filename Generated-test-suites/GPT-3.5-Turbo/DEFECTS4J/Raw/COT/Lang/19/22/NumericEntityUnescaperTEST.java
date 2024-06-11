import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidDecimalInput_shouldTranslateToCodepoint() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);

        Assert.assertEquals(5, translated);
        Assert.assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_withValidHexadecimalInput_shouldTranslateToCodepoint() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);

        Assert.assertEquals(6, translated);
        Assert.assertEquals("A", output.toString());
    }

    @Test
    public void testTranslate_withNoSemicolon_shouldNotTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, translated);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withInvalidInput_shouldReturnZero() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#-65;");
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);

        Assert.assertEquals(0, translated);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_withCodepointAboveFFFF_shouldTranslateToSurrogates() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#144308;");
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);

        Assert.assertEquals(9, translated);
        Assert.assertEquals("\uD834\uDF06", output.toString());
    }

    // Add more test cases for edge cases and exceptions

    // 
    // Merge all the individual test cases into a complete JUnit 4 test file
    // 
}