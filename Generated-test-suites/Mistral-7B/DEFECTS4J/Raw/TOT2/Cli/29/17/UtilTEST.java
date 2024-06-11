package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test-string";
        String expected = "test-string";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String input = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(input, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullString() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test-string\"";
        String expected = "test-string";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String input = "test-string";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(input, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesSingleQuote() {
        String input = "\"" + "test-string";
        String expected = "test-string";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesDoubleQuotes() {
        String input = "\"test-string\"";
        String expected = "test-string";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNullString() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesNullCharacter() {
        char input = '\0';
        Util.stripLeadingAndTrailingQuotes(String.valueOf(input));
    }
}