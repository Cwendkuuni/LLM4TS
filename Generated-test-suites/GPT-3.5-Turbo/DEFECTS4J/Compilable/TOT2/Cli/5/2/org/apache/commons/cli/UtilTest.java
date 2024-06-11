package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("abc", Util.stripLeadingHyphens("-abc"));
        assertEquals("abc", Util.stripLeadingHyphens("abc"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
    }

}