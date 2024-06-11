package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    private Util util;

    @Before
    public void setUp() {
        util = new Util();
    }

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        assertEquals("abc", util.stripLeadingHyphens("-abc"));
        // Edge case: empty string
        assertEquals("", util.stripLeadingHyphens(""));
        // Edge case: str without hyphen
        assertEquals("abc", util.stripLeadingHyphens("abc"));
        // Error scenario: null input
        assertEquals(null, util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        assertEquals("one two", util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Edge case: empty string
        assertEquals("", util.stripLeadingAndTrailingQuotes(""));
        // Edge case: str without leading and trailing quotes
        assertEquals("one two", util.stripLeadingAndTrailingQuotes("one two"));
        // Error scenario: null input
        assertEquals(null, util.stripLeadingAndTrailingQuotes(null));
        // Error scenario: str with only leading quote
        assertEquals("one two\"", util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Error scenario: str with only trailing quote
        assertEquals("\"one two", util.stripLeadingAndTrailingQuotes("\"one two\""));
        // Error scenario: str with multiple leading and trailing quotes
        assertEquals("one two\"", util.stripLeadingAndTrailingQuotes("\"\"\"one two\"\"\""));
    }
}