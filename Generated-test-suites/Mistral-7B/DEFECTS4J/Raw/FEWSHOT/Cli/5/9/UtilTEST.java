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
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));
        assertEquals("baz", Util.stripLeadingHyphens("-baz"));
        assertEquals("--bar", Util.stripLeadingHyphens("-- --bar"));
        assertEquals("baz--", Util.stripLeadingHyphens("-baz-"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", Util.stripLeadingAndTrailingQuotes("\"foo bar\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo \"bar\"\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"\"foo\" bar\""));
        assertEquals("\"\"", Util.stripLeadingAndTrailingQuotes("\"\"\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        assertEquals("'foo'", Util.stripLeadingAndTrailingQuotes("'foo'"));
        assertEquals("'foo bar'", Util.stripLeadingAndTrailingQuotes("'foo bar'"));
        assertEquals("'foo bar'\"", Util.stripLeadingAndTrailingQuotes("'foo bar\"'"));
    }

    @Test
    public void testEmptyArray() {
        assertEquals(new String[0], Util.EMPTY_STRING_ARRAY);
    }

    @Test(expected = AssertionError.class)
    public void testNegativeIndexOutOfBoundsException() {
        String testString = "test";
        String strippedString = Util.stripLeadingAndTrailingQuotes(testString.substring(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullString() {
        Util.stripLeadingAndTrailingQuotes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullUtil() {
        String testString = "test";
        String strippedString = Util.stripLeadingAndTrailingQuotes(testString, null);
    }
}