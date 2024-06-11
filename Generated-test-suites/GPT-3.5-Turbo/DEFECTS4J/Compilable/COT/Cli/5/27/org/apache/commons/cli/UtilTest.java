package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--input";
        String expected = "input";

        String result = Util.stripLeadingHyphens(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWithSingleHyphen() {
        String input = "-input";
        String expected = "input";

        String result = Util.stripLeadingHyphens(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensWithNoHyphen() {
        String input = "input";
        String expected = "input";

        String result = Util.stripLeadingHyphens(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"example\"";
        String expected = "example";

        String result = Util.stripLeadingAndTrailingQuotes(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String input = "example";
        String expected = "example";

        String result = Util.stripLeadingAndTrailingQuotes(input);

        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithLeadingAndTrailingQuotes() {
        String input = "\"\"example\"\"";
        String expected = "example";

        String result = Util.stripLeadingAndTrailingQuotes(input);

        assertEquals(expected, result);
    }
}