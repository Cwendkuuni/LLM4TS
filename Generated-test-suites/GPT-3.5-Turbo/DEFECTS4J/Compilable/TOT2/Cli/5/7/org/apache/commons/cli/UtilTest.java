package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("---abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("three four", Util.stripLeadingAndTrailingQuotes("\"three four\""));
        assertEquals("five", Util.stripLeadingAndTrailingQuotes("five"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes_handlesSingleQuote() {
        assertEquals("single", Util.stripLeadingAndTrailingQuotes("\"single"));
    }
}