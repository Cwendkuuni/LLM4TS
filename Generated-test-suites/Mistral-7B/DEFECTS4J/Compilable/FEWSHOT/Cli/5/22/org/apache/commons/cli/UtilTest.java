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
        assertEquals("--123", Util.stripLeadingHyphens("--123"));
        assertEquals("-123", Util.stripLeadingHyphens("--123-"));
        assertEquals("-123", Util.stripLeadingHyphens("-123-"));
        assertEquals("--foo-bar", Util.stripLeadingHyphens("--foo-bar"));
        assertEquals("-foo-bar", Util.stripLeadingHyphens("--foo-bar-"));
        assertEquals("-foo-bar", Util.stripLeadingHyphens("-foo-bar-"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo and bar", Util.stripLeadingAndTrailingQuotes("\"foo and bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("'foo'", Util.stripLeadingAndTrailingQuotes("'foo'"));
        assertEquals("'foo' bar", Util.stripLeadingAndTrailingQuotes("'foo' bar"));
        assertEquals("one\"two\"three", Util.stripLeadingAndTrailingQuotes("one\"two\"three"));
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
}