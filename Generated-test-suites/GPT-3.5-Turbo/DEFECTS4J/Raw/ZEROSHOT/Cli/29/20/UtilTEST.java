import org.junit.Test;

import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput_ReturnNull() {
        String result = Util.stripLeadingHyphens(null);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_InputStartingWithDoubleHyphens_ReturnSubstringWithoutHyphens() {
        String input = "--input";
        String result = Util.stripLeadingHyphens(input);
        assertEquals("input", result);
    }

    @Test
    public void testStripLeadingHyphens_InputStartingWithSingleHyphen_ReturnSubstringWithoutHyphen() {
        String input = "-input";
        String result = Util.stripLeadingHyphens(input);
        assertEquals("input", result);
    }

    @Test
    public void testStripLeadingHyphens_InputWithoutHyphens_ReturnSameInput() {
        String input = "input";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(input, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_InputStartingAndEndingWithQuotes_ReturnSubstringWithoutQuotes() {
        String input = "\"input\"";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("input", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_InputStartingWithQuote_ReturnSubstringWithoutQuote() {
        String input = "\"input";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("input", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_InputEndingWithQuote_ReturnSubstringWithoutQuote() {
        String input = "input\"";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("input", result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_InputWithoutQuotes_ReturnSameInput() {
        String input = "input";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, result);
    }
}