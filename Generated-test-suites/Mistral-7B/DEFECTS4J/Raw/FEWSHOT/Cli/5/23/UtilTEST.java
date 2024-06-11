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
        assertEquals("a", Util.stripLeadingHyphens("--a"));
        assertEquals("--", Util.stripLeadingHyphens("--"));
        assertEquals("--1", Util.stripLeadingHyphens("--1"));
        assertEquals("--1--", Util.stripLeadingHyphens("--1--"));
        assertEquals("--1--2", Util.stripLeadingHyphens("--1--2"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"\"foo\" bar\""));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"\"foo\" and \"bar\"\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
        assertEquals("a", Util.stripLeadingAndTrailingQuotes("\"a\""));
        assertEquals("\"a\" b", Util.stripLeadingAndTrailingQuotes("\"\"a\" b\""));
        assertEquals("\"a\" and \"b\"", Util.stripLeadingAndTrailingQuotes("\"\"a\" and \"b\"\""));
        assertEquals("\"\"", Util.stripLeadingAndTrailingQuotes("\"\"\""));
    }

    @Test
    public void testEmptyStringArray() {
        String[] emptyArray = Util.EMPTY_STRING_ARRAY;
        assertEquals(0, emptyArray.length);
    }
}