package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_DoubleHyphens() {
        String input = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String input = "-test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingHyphens_NoHyphen() {
        String input = "test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_QuotesPresent() {
        String input = "\"one two\"";
        String expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String input = "one two";
        String expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_StartsWithQuote() {
        String input = "\"one two";
        String expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_EndsWithQuote() {
        String input = "one two\"";
        String expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }
}