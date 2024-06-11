package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    // Test stripLeadingHyphens method
    
    @Test
    public void testStripLeadingHyphens_NullString_ReturnNull() {
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingHyphens_StartingWithDoubleHyphen_ReturnSubstringFrom2ndIndex() {
        String str = "--example";
        assertEquals("example", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_StartingWithHyphen_ReturnSubstringFrom1stIndex() {
        String str = "-example";
        assertEquals("example", Util.stripLeadingHyphens(str));
    }
    
    @Test
    public void testStripLeadingHyphens_NoLeadingHyphen_ReturnSameString() {
        String str = "example";
        assertEquals("example", Util.stripLeadingHyphens(str));
    }
    
    // Test stripLeadingAndTrailingQuotes method
    
    @Test
    public void testStripLeadingAndTrailingQuotes_StartingWithQuote_ReturnStringWithoutLeadingQuote() {
        String str = "\"example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_StartingAndEndingWithQuotes_ReturnStringWithoutLeadingAndTrailingQuotes() {
        String str = "\"example\"";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_NoLeadingAndTrailingQuotes_ReturnSameString() {
        String str = "example";
        assertEquals("example", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_EmptyString_ReturnEmptyString() {
        String str = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_OnlyQuotes_ReturnEmptyString() {
        String str = "\"\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }
}