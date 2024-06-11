package org.apache.commons.lang3.text.translate;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_WithValidInput_ReturnsTranslatedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        CharSequence input = "&amp;#65;";
        int index = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_WithInvalidInput_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        CharSequence input = "&amp;#XYZ;";
        int index = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_WithSemicolonRequiredAndNoSemicolon_ReturnsZero() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        Writer writer = new StringWriter();
        CharSequence input = "&amp;#65";
        int index = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithSemicolonRequiredAndNoSemicolon_ThrowsIllegalArgumentException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        Writer writer = new StringWriter();
        CharSequence input = "&amp;#65";
        int index = 0;
        
        unescaper.translate(input, index, writer);
    }
    
    @Test
    public void testTranslate_WithHexadecimalInput_ReturnsTranslatedOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        CharSequence input = "&amp;#x41;";
        int index = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_WithHighEntityValue_ReturnsCorrectTranslation() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        CharSequence input = "&amp;#128516;";
        int index = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(17, result);
        assertEquals("\uD83D\uDE04", writer.toString());
    }
    
    @Test
    public void testTranslate_WithInvalidInput_ReturnsZeroAndDoesNotWriteOutput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();
        CharSequence input = "ABC";
        int index = 0;
        
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
}