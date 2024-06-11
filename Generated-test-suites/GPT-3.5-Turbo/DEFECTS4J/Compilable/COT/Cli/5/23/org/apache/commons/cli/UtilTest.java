package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--example";
        String str2 = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str1));
        assertEquals("example", Util.stripLeadingHyphens(str2));
        // Edge case: empty string
        assertEquals("", Util.stripLeadingHyphens(""));
        // Edge case: string without hyphens
        assertEquals("example", Util.stripLeadingHyphens("example"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "\"one two\"";
        String str2 = "singlequote";
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals("singlequote", Util.stripLeadingAndTrailingQuotes(str2));
        // Edge case: empty string
        assertEquals("", Util.stripLeadingAndTrailingQuotes(""));
        // Edge case: string without quotes
        assertEquals("noquotes", Util.stripLeadingAndTrailingQuotes("noquotes"));
    }
}