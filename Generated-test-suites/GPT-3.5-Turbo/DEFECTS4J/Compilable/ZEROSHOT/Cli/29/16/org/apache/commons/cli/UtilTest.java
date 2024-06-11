package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Test cases with hyphens at the beginning
        String str1 = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str1));

        String str2 = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str2));

        // Test case with no hyphens at the beginning
        String str3 = "test";
        assertEquals("test", Util.stripLeadingHyphens(str3));

        // Test case with null input
        String str4 = null;
        assertNull(Util.stripLeadingHyphens(str4));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Test case with leading and trailing quotes
        String str1 = "\"one two\"";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str1));

        // Test case with only leading quote
        String str2 = "\"one two";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str2));

        // Test case with only trailing quote
        String str3 = "one two\"";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str3));

        // Test case with no quotes
        String str4 = "one two";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str4));
    }
}