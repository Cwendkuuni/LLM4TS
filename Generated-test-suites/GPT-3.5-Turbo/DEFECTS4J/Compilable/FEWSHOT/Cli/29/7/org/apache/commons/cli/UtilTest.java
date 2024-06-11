package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        // Test cases for strings starting with -
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("-foo"));
        assertEquals("f-o-o", Util.stripLeadingHyphens("-f-o-o"));
        assertEquals("", Util.stripLeadingHyphens("-"));
        assertEquals("-f", Util.stripLeadingHyphens("--f"));
        assertEquals("-foo", Util.stripLeadingHyphens("--foo"));
        
        // Test cases for strings starting with --
        assertEquals("f", Util.stripLeadingHyphens("--f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("f-o-o", Util.stripLeadingHyphens("--f-o-o"));
        assertEquals("-", Util.stripLeadingHyphens("---"));
        
        // Test case for null input
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case for string with leading and trailing quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("\"one two three\""));
        
        // Test cases for string with only leading quotes
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\"\""));
        assertEquals("one two three\"", Util.stripLeadingAndTrailingQuotes("\"one two three\"\""));
        
        // Test cases for string with only trailing quotes
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"\"one two\""));
        assertEquals("\"one two three", Util.stripLeadingAndTrailingQuotes("\"\"one two three\""));
        
        // Test cases for string with no quotes
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("one two three", Util.stripLeadingAndTrailingQuotes("one two three"));
        
        // Test case for empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
    }
}