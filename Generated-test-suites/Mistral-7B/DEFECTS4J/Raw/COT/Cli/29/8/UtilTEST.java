package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import org.apache.commons.cli.Util;
import org.junit.Test;

/**
 * Test cases for the Util class.
 */
public class UtilTest
{
    /**
     * Test for Util.stripLeadingHyphens method.
     */
    @Test
    public void testStripLeadingHyphens()
    {
        // Test null input
        String nullInput = null;
        String result = Util.stripLeadingHyphens(nullInput);
        assertEquals(null, result);

        // Test empty string
        String emptyString = "";
        result = Util.stripLeadingHyphens(emptyString);
        assertEquals(emptyString, result);

        // Test string with double leading hyphens
        String doubleLeadingHyphens = "--test";
        result = Util.stripLeadingHyphens(doubleLeadingHyphens);
        assertEquals("test", result);

        // Test string with single leading hyphen
        String singleLeadingHyphen = "-test";
        result = Util.stripLeadingHyphens(singleLeadingHyphen);
        assertEquals("test", result);

        // Test string without leading hyphens
        String noLeadingHyphens = "test";
        result = Util.stripLeadingHyphens(noLeadingHyphens);
        assertEquals("test", result);
    }

    /**
     * Test for Util.stripLeadingAndTrailingQuotes method.
     */
    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        // Test null input
        String nullInput = null;
        String result = Util.stripLeadingAndTrailingQuotes(nullInput);
        assertEquals(null, result);

        // Test empty string
        String emptyString = "";
        result = Util.stripLeadingAndTrailingQuotes(emptyString);
        assertEquals(emptyString, result);

        // Test string with leading quotes
        String leadingQuotes = "\"test\"";
        result = Util.stripLeadingAndTrailingQuotes(leadingQuotes);
        assertEquals("test", result);

        // Test string with trailing quotes
        String trailingQuotes = "\"test\"";
        result = Util.stripLeadingAndTrailingQuotes(trailingQuotes);
        assertEquals("test", result);

        // Test string with both leading and trailing quotes
        String leadingAndTrailingQuotes = "\"test\"";
        result = Util.stripLeadingAndTrailingQuotes(leadingAndTrailingQuotes);
        assertEquals("test", result);

        // Test string without quotes
        String noQuotes = "test";
        result = Util.stripLeadingAndTrailingQuotes(noQuotes);
        assertEquals("test", result);
    }
}