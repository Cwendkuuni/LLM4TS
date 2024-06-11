package org.apache.commons.cli.test;

import static org.apache.commons.cli.Util.stripLeadingHyphens;
import static org.apache.commons.cli.Util.stripLeadingAndTrailingQuotes;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test class for Util class.
 */
public class UtilTest {

    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method.
     */
    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "test";
        expected = "test";
        result = stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "---test";
        expected = "test";
        result = stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method.
     */
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "'one two'";
        String expected = "one two";
        String result = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"one two\"";
        expected = "one two";
        result = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"one two\"'";
        expected = "one two";
        result = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"one two\"\"";
        expected = "one two";
        result = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method with empty string.
     * @see Util#stripLeadingHyphens(String)
     */
    @Test
    public void testStripLeadingHyphensEmptyString() {
        String str = "";
        String expected = "";
        String result = stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method with empty string.
     * @see Util#stripLeadingAndTrailingQuotes(String)
     */
    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "";
        String expected = "";
        String result = stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}