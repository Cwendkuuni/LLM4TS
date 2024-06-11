package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "--";
        expected = "";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        input = "test";
        expected = "test";
        result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        String expected = "";
        String result = Util.stripLeadingHyphens(input);
        assertEquals(expected, result);

        Util.stripLeadingHyphens(input); // This line is expected to throw an AssertionError
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test one two\"";
        String expected = "test one two";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        input = "test one two";
        expected = "test one two";
        result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expected = "";
        String result = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, result);

        Util.stripLeadingAndTrailingQuotes(input); // This line is expected to throw a StringIndexOutOfBoundsException
    }
}