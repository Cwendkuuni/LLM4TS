package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullInput_ReturnsNull() {
        String str = null;
        String result = Util.stripLeadingHyphens(str);
        assertNull(result);
    }

    @Test
    public void testStripLeadingHyphens_StartsWithTwoHyphens_ReturnsSubstringWithoutHyphens() {
        String str = "--input";
        String expected = "input";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphens_StartsWithOneHyphen_ReturnsSubstringWithoutHyphen() {
        String str = "-input";
        String expected = "input";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphens_DoesNotStartWithHyphen_ReturnsInputString() {
        String str = "input";
        String expected = "input";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartsWithDoubleQuotes_RemovesLeadingQuote() {
        String str = "\"input\"";
        String expected = "input\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EndsWithDoubleQuotes_RemovesTrailingQuote() {
        String str = "\"input\"";
        String expected = "\"input";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartsAndEndsWithDoubleQuotes_RemovesLeadingAndTrailingQuotes() {
        String str = "\"input\"";
        String expected = "input";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_DoesNotStartOrEndWithDoubleQuotes_ReturnsInputString() {
        String str = "input";
        String expected = "input";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}