package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import java.io.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional, NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringBuilder input = new StringBuilder("&#65;");
        StringWriter output = new StringWriter();
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        input = new StringBuilder("&#x41;");
        output = new StringWriter();
        assertEquals(7, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        input = new StringBuilder("&#35");
        output = new StringWriter();
        assertEquals(0, unescaper.translate(input, 0, output));
        assertEquals("", output.toString());
        
        input = new StringBuilder("&#48;");
        output = new StringWriter();
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("0", output.toString());
    }
    
    @Test
    public void testTranslateWithOptions() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        
        StringBuilder input = new StringBuilder("&#65;");
        StringWriter output = new StringWriter();
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        input = new StringBuilder("&#x41;");
        output = new StringWriter();
        assertEquals(7, unescaper.translate(input, 0, output));
        assertEquals("A", output.toString());
        
        input = new StringBuilder("&#35");
        output = new StringWriter();
        assertEquals(0, unescaper.translate(input, 0, output));
        assertEquals("", output.toString());
        
        input = new StringBuilder("&#48;");
        output = new StringWriter();
        assertEquals(6, unescaper.translate(input, 0, output));
        assertEquals("0", output.toString());
    }
}