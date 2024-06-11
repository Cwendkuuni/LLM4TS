package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test case 1: str is null
        assertNull(Util.stripLeadingHyphens(null));

        // Test case 2: str starts with "--"
        String str = "--abc";
        assertEquals("abc", Util.stripLeadingHyphens(str));

        // Test case 3: str starts with "-"
        str = "-abc";
        assertEquals("abc", Util.stripLeadingHyphens(str));

        // Test case 4: str starts with neither "--" nor "-"
        str = "abc";
        assertEquals("abc", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case 1: str starts and ends with quotes
        String str = "\"abc\"";
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes(str));

        // Test case 2: str starts and ends with quotes, but contains inner quotes
        str = "\"abc\"\"def\"";
        assertEquals("abc\"def", Util.stripLeadingAndTrailingQuotes(str));

        // Test case 3: str starts with quote, but does not end with quote
        str = "\"abc";
        assertEquals("\"abc", Util.stripLeadingAndTrailingQuotes(str));

        // Test case 4: str does not start with quote, but ends with quote
        str = "abc\"";
        assertEquals("abc\"", Util.stripLeadingAndTrailingQuotes(str));

        // Test case 5: str does not start or end with quote
        str = "abc";
        assertEquals("abc", Util.stripLeadingAndTrailingQuotes(str));
    }
}