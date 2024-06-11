package org.apache.commons.cli;
import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);

        input = "test";
        expected = "test";
        actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);

        input = "";
        actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test\"";
        String expected = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);

        input = "test";
        expected = "test";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);

        input = "";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);

        input = "\"\\\"test\\\"\"";
        expected = "test";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyInput()
    {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}