package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("baz", Util.stripLeadingHyphens("--baz--"));
        assertEquals("qux", Util.stripLeadingHyphens("--qux----"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one 'two' three", Util.stripLeadingAndTrailingQuotes("\"one 'two' three\""));
        assertEquals("'one' two 'three'", Util.stripLeadingAndTrailingQuotes("\'one\' two 'three'"));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingHyphens(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullString() {
        Util.stripLeadingAndTrailingQuotes(null);
        Util.stripLeadingHyphens(null);
    }

    @Test
    public void testLeadingHyphensAndQuotes() {
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"--foo\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("--\"foo\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"--foo\""));
    }

    @Test
    public void testTrailingHyphensAndQuotes() {
        assertEquals("foo", Util.stripLeadingHyphens("foo--"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\"--"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo--\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo--\""));
    }
}