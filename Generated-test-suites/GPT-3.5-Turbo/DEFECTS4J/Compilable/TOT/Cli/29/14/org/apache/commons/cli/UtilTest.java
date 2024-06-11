package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case: hyphens at the beginning of the string
        String str1 = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str1));

        // Edge case: hyphen at the beginning of the string
        String str2 = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str2));

        // Edge case: no hyphens at the beginning of the string
        String str3 = "example";
        assertEquals("example", Util.stripLeadingHyphens(str3));

        // Error scenario: null string
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case: leading and trailing quotes
        String str1 = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str1));

        // Edge case: only leading quote
        String str2 = "\"example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str2));

        // Edge case: only trailing quote
        String str3 = "example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str3));

        // Edge case: no quotes
        String str4 = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str4));

        // Error scenario: null string
        assertNull(Util.stripLeadingAndTrailingQuotes(null));
    }
}