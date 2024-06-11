package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input = null;
        String expected = null;
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "--test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "-test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input = null;
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertNull(result);

        input = "\"test\"";
        String expected = "test";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "test";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, result);

        input = "\"test\" one two \"three\"";
        expected = "test one two three";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }
}