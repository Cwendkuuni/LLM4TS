package org.apache.commons.cli.test;

import static org.apache.commons.cli.Util.stripLeadingHyphens;
import static org.apache.commons.cli.Util.stripLeadingAndTrailingQuotes;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for Util class.
 */
public class UtilTest {

    /**
     * Test Util.stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        String str = "someString";
        String expected = str;
        String actual = stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingHyphens method with one leading hyphen.
     */
    @Test
    public void testStripLeadingHyphensOneLeadingHyphen() {
        String str = "-someString";
        String expected = "someString";
        String actual = stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingHyphens method with multiple leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String str = "--someString";
        String expected = "someString";
        String actual = stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingHyphens method with no leading hyphens.
     */
    @Test
    public void testStripLeadingHyphensEmptyString() {
        String str = "";
        String expected = "";
        String actual = stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingAndTrailingQuotes method with no quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String str = "someString";
        String expected = "someString";
        String actual = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingAndTrailingQuotes method with leading quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
        String str = "'someString'";
        String expected = "someString";
        String actual = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingAndTrailingQuotes method with trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
        String str = "someString'";
        String expected = "someString";
        String actual = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingAndTrailingQuotes method with leading and trailing quotes.
     */
    @Test
    public void testStripLeadingAndTrailingQuotesLeadingAndTrailingQuotes() {
        String str = "'someString'" ;
        String expected = "someString";
        String actual = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, actual);
    }

    /**
     * Test Util.stripLeadingAndTrailingQuotes method with empty string.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "";
        Util.stripLeadingAndTrailingQuotes(str);
    }
}