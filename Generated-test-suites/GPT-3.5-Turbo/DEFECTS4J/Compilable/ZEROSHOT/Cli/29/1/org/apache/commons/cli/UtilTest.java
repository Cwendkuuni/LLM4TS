package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens_whenInputStartsWithHyphens_shouldReturnStrippedString() {
        String input = "--test";
        String expectedOutput = "test";
        
        String actualOutput = Util.stripLeadingHyphens(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testStripLeadingHyphens_whenInputStartsWithSingleHyphen_shouldReturnStrippedString() {
        String input = "-test";
        String expectedOutput = "test";
        
        String actualOutput = Util.stripLeadingHyphens(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testStripLeadingHyphens_whenInputDoesNotStartWithHyphens_shouldReturnSameString() {
        String input = "test";
        
        String actualOutput = Util.stripLeadingHyphens(input);
        
        assertEquals(input, actualOutput);
    }
    
    @Test
    public void testStripLeadingHyphens_whenInputIsNull_shouldReturnNull() {
        String input = null;
        
        String actualOutput = Util.stripLeadingHyphens(input);
        
        assertEquals(input, actualOutput);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_whenInputStartAndEndsWithQuotes_shouldReturnStringWithoutQuotes() {
        String input = "\"test\"";
        String expectedOutput = "test";
        
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_whenInputStartsWithQuote_shouldReturnStringWithoutLeadingQuote() {
        String input = "\"test";
        String expectedOutput = "test";
        
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_whenInputEndsWithQuote_shouldReturnStringWithoutTrailingQuote() {
        String input = "test\"";
        String expectedOutput = "test";
        
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes_whenInputDoesNotStartOrEndWithQuotes_shouldReturnSameString() {
        String input = "test";
        
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        
        assertEquals(input, actualOutput);
    }
}