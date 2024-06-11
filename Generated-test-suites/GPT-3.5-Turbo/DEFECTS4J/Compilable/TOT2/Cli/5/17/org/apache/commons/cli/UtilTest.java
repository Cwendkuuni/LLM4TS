package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("Test 1", "test", Util.stripLeadingHyphens("-test"));
        assertEquals("Test 2", "test", Util.stripLeadingHyphens("--test"));
        assertEquals("Test 3", "test", Util.stripLeadingHyphens("test"));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("Test 1", "one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("Test 2", "one two", Util.stripLeadingAndTrailingQuotes("one two"));
        assertEquals("Test 3", "one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("Test 4", "one two", Util.stripLeadingAndTrailingQuotes("one two\""));
    }

}