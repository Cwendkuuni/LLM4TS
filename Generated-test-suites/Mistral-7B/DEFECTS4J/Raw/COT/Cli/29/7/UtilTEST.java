package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);

        input = "-test";
        expected = "test";
        actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);

        input = "test";
        expected = "test";
        actual = Util.stripLeadingHyphens(input);
        assertEquals(expected, actual);

        input = null;
        actual = Util.stripLeadingHyphens(input);
        assertNull(actual);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = "\"test one two\"";
        String expected = "test one two";
        String actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);

        input = "test one two";
        expected = "test one two";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);

        input = "";
        expected = "";
        actual = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, actual);

        input = null;
        assertNull(Util.stripLeadingAndTrailingQuotes(input));
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesEmptyString()
    {
        String input = "";
        Util.stripLeadingAndTrailingQuotes(input);
    }
}