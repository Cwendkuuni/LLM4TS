package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testEmptyString() {
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test(expected = NullPointerException.class)
    public void testNullString() {
        Util.stripLeadingHyphens(null);
        Util.stripLeadingAndTrailingQuotes(null);
    }

    @Test
    public void testLeadingHyphensAndQuotes() {
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("foo", Util.stripLeadingHyphens("--\"foo\""));
        assertEquals("foo", Util.stripLeadingHyphens("--\"foo\""));
        assertEquals("foo", Util.stripLeadingHyphens("--foo\""));

        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("--\"foo\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("--foo\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("--foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("--foo"));
    }

    @Test
    public void testLeadingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }

    @Test
    public void testNoQuotes() {
        assertEquals("foo", Util.stripLeadingHyphens("foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
    }

    @Test
    public void testTrailingHyphens() {
        assertEquals("foo", Util.stripLeadingHyphens("foo--"));
        assertEquals("foo", Util.stripLeadingHyphens("foo--"));
        assertEquals("foo", Util.stripLeadingHyphens("foo--"));
        assertEquals("foo", Util.stripLeadingHyphens("foo--"));

        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo--"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo--"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo--"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo--"));
    }
}