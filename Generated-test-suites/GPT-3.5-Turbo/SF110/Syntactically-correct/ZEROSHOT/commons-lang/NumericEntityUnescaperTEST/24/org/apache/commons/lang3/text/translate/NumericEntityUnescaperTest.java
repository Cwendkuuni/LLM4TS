package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ValidNumericEntity_ReturnsLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expected = 6;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTranslate_ValidHexNumericEntity_ReturnsLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#x41;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expected = 6;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTranslate_InvalidHexNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#xG1;";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expected = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTranslate_NonNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "ABC";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expected = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testTranslate_SemiColonRequired_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expected = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColon_ThrowsIllegalArgumentException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int index = 0;
        
        unescaper.translate(input, index, writer);
    }
    
    @Test
    public void testTranslate_SemiColonNotRequired_ReturnsLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expected = 5;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(expected, result);
    }
}