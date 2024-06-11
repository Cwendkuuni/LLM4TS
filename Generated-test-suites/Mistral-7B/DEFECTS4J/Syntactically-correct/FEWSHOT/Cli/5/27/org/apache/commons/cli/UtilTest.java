package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    // test case for stripLeadingHyphens method

    @Test
    public void testStripLeadingHyphens_EmptyString() {
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test
    public void testStripLeadingHyphens_SingleHyphen() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
    }

    @Test
    public void testStripLeadingHyphens_DoubleHyphen() {
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
    }

    @Test
    public void testStripLeadingHyphens_TripleHyphen() {
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
    }

    @Test
    public void testStripLeadingHyphens_Null() {
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingHyphens_NoHyphen() {
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
    }

    @Test
    public void testStripLeadingHyphens_LeadingDoubledHyphen() {
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
    }

    // test case for stripLeadingAndTrailingQuotes method

    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_SingleQuote() {
        assertEquals("one", Util.stripLeadingAndTrailingQuotes("'one'"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_DoubleQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingTrailingDoubleQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingDoubleQuote() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingDoubleQuote() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one \"two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_MixedLeadingTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one \"two\""));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_Null() {
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}