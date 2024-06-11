package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper1 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        NumericEntityUnescaper unescaper3 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        assertTrue(unescaper1.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper1.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper1.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        assertFalse(unescaper3.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper3.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper3.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input1 = new StringBuilder("&#65;");
        StringBuilder input2 = new StringBuilder("&amp;");
        StringBuilder input3 = new StringBuilder("&#x1F603;");
        
        StringBuilder output1 = new StringBuilder();
        StringBuilder output2 = new StringBuilder();
        StringBuilder output3 = new StringBuilder();
        
        try {
            unescaper.translate(input1, 0, output1);
            unescaper.translate(input2, 0, output2);
            unescaper.translate(input3, 0, output3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertEquals("A", output1.toString());
        assertEquals("&", output2.toString());
        assertEquals("\uD83D\uDE03", output3.toString());
    }
    
    @Test
    public void testTranslateInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input1 = new StringBuilder("&#");
        StringBuilder input2 = new StringBuilder("&#65;");
        StringBuilder input3 = new StringBuilder("&#x1G;");
        
        StringBuilder output1 = new StringBuilder();
        StringBuilder output2 = new StringBuilder();
        StringBuilder output3 = new StringBuilder();
        
        try {
            unescaper.translate(input1, 0, output1);
            unescaper.translate(input2, 0, output2);
            unescaper.translate(input3, 0, output3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        assertEquals("", output1.toString());
        assertEquals("A", output2.toString());
        assertEquals("", output3.toString());
    }
}