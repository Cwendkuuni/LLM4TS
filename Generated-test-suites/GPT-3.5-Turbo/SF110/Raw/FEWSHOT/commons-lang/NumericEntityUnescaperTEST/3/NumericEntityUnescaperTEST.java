package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        // Test input with &#
        StringBuilder input1 = new StringBuilder("&#123;");
        StringWriter output1 = new StringWriter();
        int result1 = unescaper.translate(input1, 0, output1);
        assertEquals("Translation failed on input: '&#123;'", 6, result1);
        assertEquals("Translation produced incorrect output on input: '&#123;'", "{", output1.toString());
        
        // Test input with '&#x'
        StringBuilder input2 = new StringBuilder("&#x1AB;");
        StringWriter output2 = new StringWriter();
        int result2 = unescaper.translate(input2, 0, output2);
        assertEquals("Translation failed on input: '&#x1AB;'", 7, result2);
        assertEquals("Translation produced incorrect output on input: '&#x1AB;'", "«", output2.toString());
        
        // Test input without '&#x' or '&#
        StringBuilder input3 = new StringBuilder("123");
        StringWriter output3 = new StringWriter();
        int result3 = unescaper.translate(input3, 0, output3);
        assertEquals("Translation failed on input: '123'", 0, result3);
        assertEquals("Translation produced incorrect output on input: '123'", "", output3.toString());
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertTrue("Option 'semiColonRequired' not set by default", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse("Option 'semiColonOptional' should not be set by default", unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse("Option 'errorIfNoSemiColon' should not be set by default", unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    @Test
    public void testIsSet_CustomOptions() {
        NumericEntityUnescaper unescaper1 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        assertTrue("Option 'semiColonOptional' not set", unescaper1.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue("Option 'errorIfNoSemiColon' not set", unescaper1.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        assertFalse("Option 'semiColonRequired' should not be set", unescaper1.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        
        assertTrue("Option 'semiColonRequired' not set", unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse("Option 'semiColonOptional' should not be set", unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse("Option 'errorIfNoSemiColon' should not be set", unescaper2.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
}