package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    
    @Test
    public void testStripLeadingHyphens_NullString_ReturnNull() {
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    @Test
    public void testStripLeadingHyphens_LongHyphenString_ReturnStringWithoutHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }
    
    @Test
    public void testStripLeadingHyphens_ShortHyphenString_ReturnStringWithoutHyphen() {
        String input = "-t";
        String expectedOutput = "t";
        
        assertEquals(expectedOutput, Util.stripLeadingHyphens(input));
    }
    
    @Test
    public void testStripLeadingHyphens_NoHyphenString_ReturnSameString() {
        String input = "test";
        
        assertEquals(input, Util.stripLeadingHyphens(input));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_QuotedString_ReturnStringWithoutQuotes() {
        String input = "\"test\"";
        String expectedOutput = "test";
        
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_NoQuotesString_ReturnSameString() {
        String input = "test";
        
        assertEquals(input, Util.stripLeadingAndTrailingQuotes(input));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_LeadingQuoteString_ReturnStringWithoutLeadingQuote() {
        String input = "\"test";
        String expectedOutput = "test";
        
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_TrailingQuoteString_ReturnStringWithoutTrailingQuote() {
        String input = "test\"";
        String expectedOutput = "test";
        
        assertEquals(expectedOutput, Util.stripLeadingAndTrailingQuotes(input));
    }
}