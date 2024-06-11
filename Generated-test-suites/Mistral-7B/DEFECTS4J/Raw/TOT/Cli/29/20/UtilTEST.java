
package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String str = "--foo";
        String expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--foo--";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "foo--";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--";
        assertEquals("", Util.stripLeadingHyphens(str));

        str = "";
        assertEquals(str, Util.stripLeadingHyphens(str));

        str = null;
        assertNull(Util.stripLeadingHyphens(str));

        str = "---foo---";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "--foo---";
        expected = "foo";
        assertEquals(expected, Util.stripLeadingHyphens(str));

        str = "---";
        assertEquals("", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"foo bar \"";
        String expected = "foo bar";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"foo bar\"";
        expected = "foo bar";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "";
        assertEquals(str, Util.stripLeadingAndTrailingQuotes(str));

        str = null;
        assertNull(Util.stripLeadingAndTrailingQuotes(str));

        str = "\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));

        str = "\"foo bar \t\"";
        expected = "foo bar";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"foo bar\" \t";
        expected = "foo bar";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));

        str = "\"foo bar \" \t";
        expected = "foo bar";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
    }
}
