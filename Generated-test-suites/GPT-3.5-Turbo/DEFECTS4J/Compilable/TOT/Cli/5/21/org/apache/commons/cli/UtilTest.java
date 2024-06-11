package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // typical use case
        assertEquals("input: --test", "test", Util.stripLeadingHyphens("--test"));

        // edge case: empty string
        assertEquals("input: empty string", "", Util.stripLeadingHyphens(""));

        // edge case: single hyphen
        assertEquals("input: -test", "test", Util.stripLeadingHyphens("-test"));

        // error scenario: input with no hyphens
        assertEquals("input: test", "test", Util.stripLeadingHyphens("test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // typical use case
        assertEquals("input: \"one two\"", "one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));

        // edge case: empty string
        assertEquals("input: empty string", "", Util.stripLeadingAndTrailingQuotes(""));

        // edge case: string without quotes
        assertEquals("input: one two", "one two", Util.stripLeadingAndTrailingQuotes("one two"));

        // error scenario: leading quotes only
        assertEquals("input: \"one two", "\"one two", Util.stripLeadingAndTrailingQuotes("\"one two"));

        // error scenario: trailing quotes only
        assertEquals("input: one two\"", "one two\"", Util.stripLeadingAndTrailingQuotes("one two\""));

        // error scenario: string with multiple pairs of quotes
        assertEquals("input: \"one\" \"two\"", "one\" \"two", Util.stripLeadingAndTrailingQuotes("\"one\" \"two\""));
    }
}