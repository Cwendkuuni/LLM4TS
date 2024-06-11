package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String input = "-test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWithNoHyphen() {
        String input = "test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String input = "test";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyLeadingQuote() {
        String input = "\"test";
        String expected = "\"test";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithOnlyTrailingQuote() {
        String input = "test\"";
        String expected = "test\"";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }
}