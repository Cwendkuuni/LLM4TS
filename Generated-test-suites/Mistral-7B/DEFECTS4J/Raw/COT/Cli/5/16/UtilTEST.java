package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for Util class methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--foo";
        String expected = "foo";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "-foo";
        expected = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "foo";
        expected = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "";
        assertNull(Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensNullInput() {
        Util.stripLeadingHyphens(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testStripLeadingHyphensStringIndexOutOfBounds() {
        String str = "---foo";
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}