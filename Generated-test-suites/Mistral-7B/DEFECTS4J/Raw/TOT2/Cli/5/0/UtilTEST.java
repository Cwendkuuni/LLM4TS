package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String str = "--test-string";
        String expected = "test-string";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        String str = "test-string";
        String expected = str;
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensOneLeadingHyphen() {
        String str = "--test-string";
        String expected = "test-string";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String str = "---test-string";
        String expected = "test-string";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test-string\"";
        String expected = "test-string";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String str = "test-string";
        String expected = str;
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesLeadingQuotes() {
        String str = "\"test-string\"";
        String expected = "test-string";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesTrailingQuotes() {
        String str = "\"test-string\"";
        String expected = "test-string";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesBothLeadingAndTrailingQuotes() {
        String str = "\"test-string\"";
        String result = Util.stripLeadingAndTrailingQuotes(str);
    }
}