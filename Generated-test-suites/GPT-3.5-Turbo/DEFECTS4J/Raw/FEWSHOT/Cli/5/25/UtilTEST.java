package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts and ends with double quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // Test case 2: str starts and ends with single quotes
        assertEquals("'hello world'", Util.stripLeadingAndTrailingQuotes("'hello world'"));

        // Test case 3: str contains quotes in the middle
        assertEquals("hello 'world'!", Util.stripLeadingAndTrailingQuotes("hello 'world'!"));

        // Test case 4: str starts with a quote but does not end with a quote
        assertEquals("hello world", Util.stripLeadingAndTrailingQuotes("'hello world"));

        // Test case 5: str ends with a quote but does not start with a quote
        assertEquals("hello world", Util.stripLeadingAndTrailingQuotes("hello world'"));

        // Test case 6: str does not start or end with quotes
        assertEquals("no quotes", Util.stripLeadingAndTrailingQuotes("no quotes"));

        // Test case 7: empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // Test case 8: str starts and ends with multiple quotes
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("''''foo'''''"));
    }

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str starts with "--"
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));

        // Test case 2: str starts with "-"
        assertEquals("bar", Util.stripLeadingHyphens("-bar"));

        // Test case 3: str does not start with hyphen
        assertEquals("abc", Util.stripLeadingHyphens("abc"));

        // Test case 4: str starts with multiple hyphens
        assertEquals("def", Util.stripLeadingHyphens("---def"));

        // Test case 5: null input
        assertNull(Util.stripLeadingHyphens(null));
    }
}