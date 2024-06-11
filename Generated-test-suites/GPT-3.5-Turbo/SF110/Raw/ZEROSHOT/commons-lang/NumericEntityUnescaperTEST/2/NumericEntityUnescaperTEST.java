package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_whenValidInput_thenTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#100;";
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(5, result);
        assertEquals("d", writer.toString());
    }
    
    @Test
    public void testTranslate_whenInvalidInput_thenThrowException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#xZZ;";
        StringWriter writer = new StringWriter();
        
        try {
            unescaper.translate(input, 0, writer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_whenHexadecimalInput_thenTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x61;";
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(5, result);
        assertEquals("a", writer.toString());
    }
    
    @Test
    public void testTranslate_whenEntityValueGreaterThan65535_thenTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65536;";
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(10, result);
        assertEquals("\ud800\udc00", writer.toString());
    }
    
    @Test
    public void testTranslate_whenSemiColonRequiredOptionDisabled_thenTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        CharSequence input = "&#100";
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(3, result);
        assertEquals("d", writer.toString());
    }
}