package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test case for Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "---test";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test--";
        expected = "test";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "--";
        expected = "";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "test\"";
        expected = "test";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"test\"";
        expected = "test";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "test\"";
        expected = "test";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNegative() {
        String str = "test";
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNull() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}