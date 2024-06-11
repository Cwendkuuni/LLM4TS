package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("Expected result", "abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("Expected result", "abc", Util.stripLeadingHyphens("--abc"));
        assertNull("Expected result", Util.stripLeadingHyphens(null));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("Expected result", "one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("Expected result", "one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("Expected result", "one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("Expected result", "one two", Util.stripLeadingAndTrailingQuotes("one two\""));
    }

}