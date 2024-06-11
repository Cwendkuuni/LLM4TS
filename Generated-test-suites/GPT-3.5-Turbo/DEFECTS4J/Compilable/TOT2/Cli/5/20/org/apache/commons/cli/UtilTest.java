package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("input string does not start with hyphens", "test", Util.stripLeadingHyphens("test"));
        assertEquals("input string starts with double hyphens", "test", Util.stripLeadingHyphens("--test"));
        assertEquals("input string starts with single hyphen", "test", Util.stripLeadingHyphens("-test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("input string does not start or end with quotes", "test", Util.stripLeadingAndTrailingQuotes("test"));
        assertEquals("input string starts and ends with quotes", "test", Util.stripLeadingAndTrailingQuotes("\"test\""));
        assertEquals("input string starts with quote", "test", Util.stripLeadingAndTrailingQuotes("\"test"));
        assertEquals("input string ends with quote", "test", Util.stripLeadingAndTrailingQuotes("test\""));
    }
}