package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--  test";
        expected = " test";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--test --anotherTest";
        expected = "test anotherTest";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));

        str = "";
        assertEquals(null, Util.stripLeadingHyphens(str));

        str = "test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"--test\"";
        String expected = "--test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"--  test\"";
        expected = "-- test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"--test --anotherTest\"";
        expected = "test anotherTest";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"--test\"";
        expected = "test";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"\"";
        assertEquals(null, Util.stripLeadingAndTrailingQuotes(str));

        str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensWithNull()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesWithNull()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}