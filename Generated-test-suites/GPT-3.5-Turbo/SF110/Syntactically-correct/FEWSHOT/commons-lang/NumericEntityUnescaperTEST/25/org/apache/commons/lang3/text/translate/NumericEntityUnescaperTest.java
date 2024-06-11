package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.io.*;

public class NumericEntityUnescaperTest {
    
    @Test 
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertEquals(true, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test 
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        String input1 = "&#65;";
        Writer output1 = new StringWriter();
        unescaper.translate(input1, 0, output1);
        assertEquals("A", output1.toString());
        
        String input2 = "&#x63;";
        Writer output2 = new StringWriter();
        unescaper.translate(input2, 0, output2);
        assertEquals("c", output2.toString());
        
        String input3 = "&#x666;";
        Writer output3 = new StringWriter();
        unescaper.translate(input3, 0, output3);
        assertEquals("f", output3.toString());
        
        String input4 = "&#x10000;";
        Writer output4 = new StringWriter();
        unescaper.translate(input4, 0, output4);
        assertEquals("𠀀", output4.toString());
        
        String input5 = "&#49;";
        Writer output5 = new StringWriter();
        unescaper.translate(input5, 0, output5);
        assertEquals("1", output5.toString());
    }
    
    @Test 
    public void testTranslateSemicolonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        String input1 = "&#65;";
        Writer output1 = new StringWriter();
        unescaper.translate(input1, 0, output1);
        assertEquals("A", output1.toString());
        
        String input2 = "&#x63;";
        Writer output2 = new StringWriter();
        unescaper.translate(input2, 0, output2);
        assertEquals("c", output2.toString());
        
        String input3 = "&#x666;";
        Writer output3 = new StringWriter();
        unescaper.translate(input3, 0, output3);
        assertEquals("f", output3.toString());
        
        String input4 = "&#x10000;";
        Writer output4 = new StringWriter();
        unescaper.translate(input4, 0, output4);
        assertEquals("𠀀", output4.toString());
        
        String input5 = "&#49;";
        Writer output5 = new StringWriter();
        unescaper.translate(input5, 0, output5);
        assertEquals("1", output5.toString());
    }
    
    @Test 
    public void testTranslateSemicolonOptional() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        String input1 = "&#65;";
        Writer output1 = new StringWriter();
        unescaper.translate(input1, 0, output1);
        assertEquals("A", output1.toString());
        
        String input2 = "&#x63;";
        Writer output2 = new StringWriter();
        unescaper.translate(input2, 0, output2);
        assertEquals("c", output2.toString());
        
        String input3 = "&#x666;";
        Writer output3 = new StringWriter();
        unescaper.translate(input3, 0, output3);
        assertEquals("f", output3.toString());
        
        String input4 = "&#x10000;";
        Writer output4 = new StringWriter();
        unescaper.translate(input4, 0, output4);
        assertEquals("𠀀", output4.toString());
        
        String input5 = "&#49;";
        Writer output5 = new StringWriter();
        unescaper.translate(input5, 0, output5);
        assertEquals("1", output5.toString());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testTranslateErrorIfNoSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        String input1 = "&#65;";
        Writer output1 = new StringWriter();
        unescaper.translate(input1, 0, output1);
        
        String input2 = "&#x63;";
        Writer output2 = new StringWriter();
        unescaper.translate(input2, 0, output2);
        
        String input3 = "&#x666;";
        Writer output3 = new StringWriter();
        unescaper.translate(input3, 0, output3);
        
        String input4 = "&#x10000;";
        Writer output4 = new StringWriter();
        unescaper.translate(input4, 0, output4);
        
        String input5 = "&#49;";
        Writer output5 = new StringWriter();
        unescaper.translate(input5, 0, output5);
    }
}