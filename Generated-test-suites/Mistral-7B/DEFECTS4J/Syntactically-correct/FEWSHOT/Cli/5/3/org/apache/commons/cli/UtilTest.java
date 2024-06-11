package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("--", Util.stripLeadingHyphens("--"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("'one two'", Util.stripLeadingAndTrailingQuotes("'one two'"));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testSingleQuoteString() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("'foo'"));
    }

    @Test
    public void testStringWithLeadingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
    }

    @Test
    public void testStringWithTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));
    }

    @Test
    public void testStringWithLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
    }

    @Test
    public void testStringWithOnlyTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));
    }

    @Test
    public void testStringWithOnlyLeadingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
    }

    @Test
    public void testStringWithQuotesInMiddle() {
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
    }
}