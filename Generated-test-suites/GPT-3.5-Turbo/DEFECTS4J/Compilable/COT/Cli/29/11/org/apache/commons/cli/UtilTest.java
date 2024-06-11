package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullString_ReturnNull() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_StartingWithDoubleHyphen_ReturnSubstring() {
        String input = "--input";
        String expected = "input";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphens_StartingWithSingleHyphen_ReturnSubstring() {
        String input = "-input";
        String expected = "input";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphens_NotStartingWithHyphen_ReturnInputString() {
        String input = "input";
        assertEquals(input, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartingWithQuotes_ReturnSubstring() {
        String input = "\"input\"";
        String expected = "input";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NotStartingWithQuotes_ReturnInputString() {
        String input = "input";
        assertEquals(input, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NotEndingWithQuotes_ReturnInputString() {
        String input = "input";
        assertEquals(input, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartingAndEndingWithQuotes_ReturnSubstring() {
        String input = "\"input\"";
        String expected = "input";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartingAndEndingWithDifferentQuotes_ReturnInputString() {
        String input = "'input'";
        assertEquals(input, Util.stripLeadingAndTrailingQuotes(input));
    }
}