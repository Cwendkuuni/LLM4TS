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
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("", Util.stripLeadingHyphens(""));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
        assertEquals("--ab-cd--efgh--", Util.stripLeadingHyphens("--ab-cd--efgh--"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo bar\" baz", Util.stripLeadingAndTrailingQuotes("\"foo bar\" baz"));
        assertEquals("\"\"", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes("abc"));
        assertEquals("\"--ab-cd--efgh--\"", Util.stripLeadingAndTrailingQuotes("\"--ab-cd--efgh--\""));
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
    public void testLeadingAndTrailingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("--f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingHyphens("--foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingHyphens("--foo-"));
        assertEquals("-foo", Util.stripLeadingAndTrailingHyphens("---foo-"));
    }

    @Test
    public void testLeadingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
        assertEquals("foo bar baz\" quux", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\" quux"));
    }

    @Test
    public void testTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("foo bar\""));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("foo bar baz\""));
        assertEquals("foo bar baz\" quux\"", Util.stripLeadingAndTrailingQuotes("foo bar baz\" quux\""));
    }

    @Test
    public void testLeadingAndTrailingQuotesAndHyphens() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("--foo"));
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"--foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("--foo bar"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"--foo bar\""));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("--foo bar baz"));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"--foo bar baz\""));
        assertEquals("foo bar baz quux", Util.stripLeadingAndTrailingQuotes("\"foo bar baz quux\""));
        assertEquals("foo bar baz quux", Util.stripLeadingAndTrailingQuotes("--foo bar baz quux"));
        assertEquals("foo bar baz quux", Util.stripLeadingAndTrailingQuotes("\"--foo bar baz quux\""));
    }
}