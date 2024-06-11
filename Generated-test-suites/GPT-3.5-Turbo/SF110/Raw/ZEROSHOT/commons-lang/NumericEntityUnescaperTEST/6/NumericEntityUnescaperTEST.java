package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_whenInputStartsWithNumericEntity_shouldReturnTranslatedValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder("&amp;ABC");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("&ABC", output.toString());
            assertEquals(5, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testTranslate_whenInputStartsWithHexNumericEntity_shouldReturnTranslatedValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder("&#x41;BC");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("ABC", output.toString());
            assertEquals(7, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testTranslate_whenOptionSemiColonRequiredAndNumericEntityDoesNotEndWithSemiColon_shouldReturnZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("", output.toString());
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testTranslate_whenOptionSemiColonRequiredAndNumericEntityEndsWithSemiColon_shouldReturnTranslatedValue() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("A", output.toString());
            assertEquals(6, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testTranslate_whenOptionErrorIfNoSemiColonAndNumericEntityDoesNotEndWithSemiColon_shouldThrowException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        StringBuilder input = new StringBuilder("&#65");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        } catch (Exception e) {
            fail("Should throw IllegalArgumentException");
        }
    }
    
    @Test
    public void testTranslate_whenNumericEntityValueIsGreaterThan65535_shouldWriteTwoCharsToOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder("&#65536;");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("\ud800\udc00", output.toString());
            assertEquals(10, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testTranslate_whenNumericEntityValueIsLessThanOrEqualTo65535_shouldWriteOneCharToOutput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder("&#65;");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("A", output.toString());
            assertEquals(6, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
    
    @Test
    public void testTranslate_whenInputDoesNotStartWithNumericEntity_shouldReturnZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder("ABC");
        StringBuilder output = new StringBuilder();
        int index = 0;
        try {
            int result = unescaper.translate(input, index, output);
            
            assertEquals("", output.toString());
            assertEquals(0, result);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
}