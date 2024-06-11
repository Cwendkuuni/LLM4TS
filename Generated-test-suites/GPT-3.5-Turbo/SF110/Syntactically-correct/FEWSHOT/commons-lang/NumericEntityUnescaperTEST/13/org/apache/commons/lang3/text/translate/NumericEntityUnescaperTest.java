package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

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
    }
    
    @Test
    public void testTranslate_withDecimalNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        CharSequence input = "This is a numeric entity: &#123;";
        int index = 22;
        StringWriter out = new StringWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(7, result);
        assertEquals("This is a numeric entity: {", out.toString());
    }
    
    @Test
    public void testTranslate_withHexadecimalNumber() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        CharSequence input = "This is a numeric entity: &#x61;";
        int index = 22;
        StringWriter out = new StringWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(8, result);
        assertEquals("This is a numeric entity: a", out.toString());
    }
    
    @Test
    public void testTranslate_withSemiColonRequired() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        CharSequence input = "This is a numeric entity: &#123;";
        int index = 22;
        StringWriter out = new StringWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(7, result);
        assertEquals("This is a numeric entity: {", out.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_withErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        CharSequence input = "This is a numeric entity: &#123";
        int index = 22;
        StringWriter out = new StringWriter();
        
        unescaper.translate(input, index, out);
    }
    
    @Test
    public void testTranslate_withNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        
        CharSequence input = "This is a numeric entity: &#123";
        int index = 22;
        StringWriter out = new StringWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(6, result);
        assertEquals("This is a numeric entity: &#123", out.toString());
    }
    
    @Test
    public void testTranslate_withInvalidNumberFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        CharSequence input = "This is a numeric entity: &#abc;";
        int index = 22;
        StringWriter out = new StringWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_withUnicodeCharacter() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        CharSequence input = "This is a numeric entity: &#65536;";
        int index = 22;
        StringWriter out = new StringWriter();
        
        int result = unescaper.translate(input, index, out);
        
        assertEquals(11, result);
        assertEquals("This is a numeric entity: \uD800\uDC00", out.toString());
    }
    
}