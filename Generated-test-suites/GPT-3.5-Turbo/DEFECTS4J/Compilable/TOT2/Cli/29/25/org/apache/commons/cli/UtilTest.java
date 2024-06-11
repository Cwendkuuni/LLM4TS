package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertNull(Util.stripLeadingHyphens(null));
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("bcd", Util.stripLeadingHyphens("-bcd"));
        assertEquals("def", Util.stripLeadingHyphens("def"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("three four", Util.stripLeadingAndTrailingQuotes("three four"));
        assertEquals("five six", Util.stripLeadingAndTrailingQuotes("five six\""));
        assertEquals("seven eight", Util.stripLeadingAndTrailingQuotes("\"seven eight"));
    }
}