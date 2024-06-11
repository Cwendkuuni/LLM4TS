package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_typicalCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&amp;");
        StringBuilder expectedOutput = new StringBuilder("&");
        StringBuilder actualOutput = new StringBuilder();
        
        int result = unescaper.translate(input, 0, actualOutput);
        
        assertEquals(expectedOutput.toString(), actualOutput.toString());
        assertEquals(4, result);
    }
    
    @Test
    public void testTranslate_hexadecimalCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x41;");
        StringBuilder expectedOutput = new StringBuilder("A");
        StringBuilder actualOutput = new StringBuilder();
        
        int result = unescaper.translate(input, 0, actualOutput);
        
        assertEquals(expectedOutput.toString(), actualOutput.toString());
        assertEquals(7, result);
    }
    
    @Test
    public void testTranslate_largeValueCase() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65536;");
        StringBuilder expectedOutput = new StringBuilder("\uD800\uDC00");
        StringBuilder actualOutput = new StringBuilder();
        
        int result = unescaper.translate(input, 0, actualOutput);
        
        assertEquals(expectedOutput.toString(), actualOutput.toString());
        assertEquals(10, result);
    }
    
    @Test
    public void testTranslate_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder expectedOutput = new StringBuilder();
        StringBuilder actualOutput = new StringBuilder();
        
        int result = unescaper.translate(input, 0, actualOutput);
        
        assertEquals(expectedOutput.toString(), actualOutput.toString());
        assertEquals(0, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder actualOutput = new StringBuilder();
        
        unescaper.translate(input, 0, actualOutput);
    }
    
    @Test
    public void testTranslate_invalidNumberFormat() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#invalid;");
        StringBuilder expectedOutput = new StringBuilder();
        StringBuilder actualOutput = new StringBuilder();
        
        int result = unescaper.translate(input, 0, actualOutput);
        
        assertEquals(expectedOutput.toString(), actualOutput.toString());
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_nonEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("abc");
        StringBuilder expectedOutput = new StringBuilder();
        StringBuilder actualOutput = new StringBuilder();
        
        int result = unescaper.translate(input, 0, actualOutput);
        
        assertEquals(expectedOutput.toString(), actualOutput.toString());
        assertEquals(0, result);
    }
}