package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test Util class.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--foo";
        String expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "foo";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--";
        expected = "";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "---foo";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "foo--";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"one two\"";
        String expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "one two";
        expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"\"";
        expected = "";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"one\" two\"";
        expected = "one two";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }
}