package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals("test", output);
    }

    @Test
    public void testStripLeadingHyphens_NoHyphens() {
        String input = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals("test", output);
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        String input = "-test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals("test", output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("test", output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        String input = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("test", output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuotesOnly() {
        String input = "\"test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("test", output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuotesOnly() {
        String input = "test\"";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals("test", output);
    }
}