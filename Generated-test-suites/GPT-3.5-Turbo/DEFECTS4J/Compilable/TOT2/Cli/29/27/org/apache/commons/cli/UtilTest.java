package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens_NullInput() {
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingHyphens_DoubleDash() {
        assertEquals("input", Util.stripLeadingHyphens("--input"));
    }
    
    @Test
    public void testStripLeadingHyphens_SingleDash() {
        assertEquals("input", Util.stripLeadingHyphens("-input"));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_BothQuotes() {
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuotes() {
        assertEquals("one two\"", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuotes() {
        assertEquals("\"one two", Util.stripLeadingAndTrailingQuotes("\"one two\""));
    }
}