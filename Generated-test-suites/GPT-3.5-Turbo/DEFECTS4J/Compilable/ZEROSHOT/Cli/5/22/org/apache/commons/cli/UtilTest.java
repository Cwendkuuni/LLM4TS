package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("str", Util.stripLeadingHyphens("str"));
        assertEquals("str", Util.stripLeadingHyphens("-str"));
        assertEquals("str", Util.stripLeadingHyphens("--str"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("str", Util.stripLeadingAndTrailingQuotes("str"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two"));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("one two\""));
    }
}