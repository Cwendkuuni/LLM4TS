package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.CharArrayWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_NoNumericEntity_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "abcd";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_NumericEntity_NoSemiColonRequired_ReturnsEntityLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        CharSequence input = "&#65;";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(5, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_NumericEntity_ErrorIfNoSemiColon_ThrowsException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        CharSequence input = "&#65";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        unescaper.translate(input, index, out);
    }
    
    @Test
    public void testTranslate_NumericEntity_IsHex_ReturnsEntityLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x41;";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(5, result);
    }
    
    @Test
    public void testTranslate_NumericEntity_IsHex_EntityValueGreaterThan65535_WritesTwoChars() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x10000;";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        unescaper.translate(input, index, out);
        char[] expected = Character.toChars(65536);
        char[] actual = out.toString().toCharArray();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testTranslate_NumericEntity_IsHex_WritesEntityValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#x41;";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        unescaper.translate(input, index, out);
        char[] expected = {'A'};
        char[] actual = out.toString().toCharArray();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testTranslate_NumericEntity_IsNotHex_ReturnsEntityLength() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(5, result);
    }
    
    @Test
    public void testTranslate_NumericEntity_IsNotHex_WritesEntityValue() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&#65;";
        int index = 0;
        Writer out = new CharArrayWriter();
        
        unescaper.translate(input, index, out);
        char[] expected = {'A'};
        char[] actual = out.toString().toCharArray();
        
        assertArrayEquals(expected, actual);
    }
}