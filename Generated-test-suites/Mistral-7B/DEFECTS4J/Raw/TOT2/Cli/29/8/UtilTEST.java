package org.example.junit.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expected = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "'one two'";
        String expected = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForStripLeadingHyphens() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputForStripLeadingAndTrailingQuotes() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }

    @Test
    public void testEmptyStringForStripLeadingHyphens() {
        String input = "";
        String expected = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test
    public void testEmptyStringForStripLeadingAndTrailingQuotes() {
        String input = "";
        String expected = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingHyphensWithNoLeadingHyphens() {
        String input = "test";
        String expected = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingHyphensWithMultipleLeadingHyphens() {
        String input = "---test";
        String expected = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithNoQuotes() {
        String input = "onetwo";
        String expected = "onetwo";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }
}