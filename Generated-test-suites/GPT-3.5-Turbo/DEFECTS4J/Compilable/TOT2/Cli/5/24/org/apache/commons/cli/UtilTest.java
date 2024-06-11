package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Test;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("abc", Util.stripLeadingHyphens("--abc"));
        assertEquals("def", Util.stripLeadingHyphens("-def"));
        assertEquals("ghi", Util.stripLeadingHyphens("ghi"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("three", Util.stripLeadingAndTrailingQuotes("\"three"));
        assertEquals("four", Util.stripLeadingAndTrailingQuotes("four\""));
        assertEquals("five", Util.stripLeadingAndTrailingQuotes("five"));
    }
    
}