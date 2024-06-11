package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts and ends with double quotes
        assertEquals("one", Util.stripLeadingAndTrailingQuotes("\"one\""));

        // Test case 2: str starts and ends with double quotes and contains spaces
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // Test case 3: str starts with double quote and ends with space
        assertEquals("\"one", Util.stripLeadingAndTrailingQuotes("\"one "));

        // Test case 4: str starts with space and ends with double quote
        assertEquals("one\"", Util.stripLeadingAndTrailingQuotes("one\""));

        // Test case 5: str starts and ends with double quotes and contains double quote within
        assertEquals("\"one\" two", Util.stripLeadingAndTrailingQuotes("\"one\" two"));

        // Test case 6: Empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));

        // Test case 7: str starts and ends with double quotes and contains escaped double quote within
        assertEquals("one \"two\"", Util.stripLeadingAndTrailingQuotes("\"one \"two\"\""));
    }

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str starts with single hyphen
        assertEquals("f", Util.stripLeadingHyphens("-f"));

        // Test case 2: str starts with double hyphen
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));

        // Test case 3: str starts with multiple hyphens
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));

        // Test case 4: null string
        assertNull(Util.stripLeadingHyphens(null));
    }
}