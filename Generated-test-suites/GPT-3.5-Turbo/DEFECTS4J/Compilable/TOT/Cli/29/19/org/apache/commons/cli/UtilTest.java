package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_NullString_ReturnNull() {
        String str = null;
        assertEquals(null, Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_TwoHyphens_ReturnSubstring() {
        String str = "--test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_OneHyphen_ReturnSubstring() {
        String str = "-test";
        assertEquals("test", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuote_ReturnSubstring() {
        String str = "\"test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuote_ReturnSubstring() {
        String str = "test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingAndTrailingQuotes_ReturnSubstring() {
        String str = "\"test\"";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotes_ReturnSameString() {
        String str = "test";
        assertEquals("test", Util.stripLeadingAndTrailingQuotes(str));
    }
}