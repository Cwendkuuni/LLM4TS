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
        assertEquals("_foo", Util.stripLeadingHyphens("--_foo"));
        assertEquals("--_foo", Util.stripLeadingHyphens("---_foo"));
        assertEquals("--1--2--3", Util.stripLeadingHyphens("--1--2--3"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo ", Util.stripLeadingAndTrailingQuotes("foo \" "));
        assertEquals(" foo", Util.stripLeadingAndTrailingQuotes(" foo"));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo bar ", Util.stripLeadingAndTrailingQuotes("foo bar \" "));
        assertEquals(" foo bar", Util.stripLeadingAndTrailingQuotes(" foo bar"));
        assertEquals("foo bar baz", Util.stripLeadingAndTrailingQuotes("\"foo bar baz\""));
        assertEquals("foo bar baz ", Util.stripLeadingAndTrailingQuotes("foo bar baz \" "));
        assertEquals(" foo bar baz", Util.stripLeadingAndTrailingQuotes(" foo bar baz"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testEmptyStringArray() {
        String[] emptyArray = Util.EMPTY_STRING_ARRAY;
        assertEquals(0, emptyArray.length);
        for (int i = 0; i < emptyArray.length; i++) {
            assertNull(emptyArray[i]);
        }
    }
}