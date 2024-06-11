package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "test";
        expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));

        input = "test--";
        expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "test";
        expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));

        input = "\"test\"";
        expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}