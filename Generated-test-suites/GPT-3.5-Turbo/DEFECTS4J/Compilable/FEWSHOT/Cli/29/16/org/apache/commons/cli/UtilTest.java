package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test cases for string with leading and trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));

        // Test cases for string without leading and trailing quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("foo"));
        assertEquals("bar", Util.stripLeadingAndTrailingQuotes("bar"));
        assertEquals("baz", Util.stripLeadingAndTrailingQuotes("baz"));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }

    @Test
    public void testStripLeadingHyphens() {
        // Test cases for string with double hyphens
        assertEquals("f", Util.stripLeadingHyphens("--f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("bar", Util.stripLeadingHyphens("--bar"));

        // Test cases for string with single hyphen
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("-foo"));
        assertEquals("bar", Util.stripLeadingHyphens("-bar"));

        // Test case for string without hyphen
        assertEquals("foobar", Util.stripLeadingHyphens("foobar"));

        // Test case for null string
        assertNull(Util.stripLeadingHyphens(null));
    }
}