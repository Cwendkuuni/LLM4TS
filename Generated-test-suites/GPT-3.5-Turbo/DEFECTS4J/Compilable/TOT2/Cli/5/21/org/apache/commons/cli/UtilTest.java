package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("test", Util.stripLeadingHyphens("--test"));
        assertEquals("test", Util.stripLeadingHyphens("-test"));
        assertEquals("test", Util.stripLeadingHyphens("test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("hello world", Util.stripLeadingAndTrailingQuotes("\"hello world\""));
    }
}