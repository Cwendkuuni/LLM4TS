package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String str = "--test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String str = "";
        String expected = "";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        String str = "test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String str = "---test";
        String expected = "test";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "";
        String expected = "";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String str = "test";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
        String str = "\"test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
        String str = "test\"";
        String expected = "test";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}