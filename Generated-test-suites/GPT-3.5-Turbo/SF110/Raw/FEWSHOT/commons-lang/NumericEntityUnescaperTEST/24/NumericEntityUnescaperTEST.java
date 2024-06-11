package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        // Test case 1: input.charAt(index) == '&' and index < seqEnd - 2 and input.charAt(index + 1) != '#'
        CharSequence input1 = "&amp;";
        StringBuilder sb1 = new StringBuilder();
        StringWriter writer1 = new StringWriter(sb1);
        int result1 = unescaper.translate(input1, 0, writer1);
        assertEquals(0, result1);
        assertEquals("", sb1.toString());
        
        // Test case 2: input.charAt(index) == '&' and index < seqEnd - 2 and input.charAt(index + 1) == '#'
        CharSequence input2 = "&#65;";
        StringBuilder sb2 = new StringBuilder();
        StringWriter writer2 = new StringWriter(sb2);
        int result2 = unescaper.translate(input2, 0, writer2);
        assertEquals(5, result2);
        assertEquals("A", sb2.toString());
        
        // Test case 3: semiNext is false and isSet(OPTION.semiColonRequired) is true
        CharSequence input3 = "&#65";
        StringBuilder sb3 = new StringBuilder();
        StringWriter writer3 = new StringWriter(sb3);
        try {
            int result3 = unescaper.translate(input3, 0, writer3);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        
        // Test case 4: semiNext is false and isSet(OPTION.semiColonRequired) is false and isSet(OPTION.errorIfNoSemiColon) is true
        NumericEntityUnescaper unescaper4 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input4 = "&#65";
        StringBuilder sb4 = new StringBuilder();
        StringWriter writer4 = new StringWriter(sb4);
        try {
            int result4 = unescaper4.translate(input4, 0, writer4);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        
        // Test case 5: semiNext is true
        CharSequence input5 = "&#65;";
        StringBuilder sb5 = new StringBuilder();
        StringWriter writer5 = new StringWriter(sb5);
        int result5 = unescaper.translate(input5, 0, writer5);
        assertEquals(6, result5);
        assertEquals("A", sb5.toString());
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.semiColonOptional);
        
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
}