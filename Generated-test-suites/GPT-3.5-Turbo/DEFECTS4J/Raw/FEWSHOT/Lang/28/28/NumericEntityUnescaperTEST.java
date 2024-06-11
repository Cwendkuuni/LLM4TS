package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private static final NumericEntityUnescaper ESCAPER = new NumericEntityUnescaper();
    
    @Test
    public void testTranslateWhenInputIsEmpty() throws IOException {
        Writer out = new StringWriter();
        int result = ESCAPER.translate("", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateWhenInputDoesNotContainNumericEntity() throws IOException {
        Writer out = new StringWriter();
        int result = ESCAPER.translate("abc", 0, out);
        
        assertEquals(0, result);
        assertEquals("abc", out.toString());
    }
    
    @Test
    public void testTranslateWhenInputContainsNumericEntities() throws IOException {
        Writer out = new StringWriter();
        int result = ESCAPER.translate("&#64;&#65;&#66;", 0, out);
        
        assertEquals(7, result);
        assertEquals("@ABC", out.toString());
    }
    
    @Test
    public void testTranslateWhenInputContainsHexadecimalEntities() throws IOException {
        Writer out = new StringWriter();
        int result = ESCAPER.translate("&#x41;&#x42;&#x43;", 0, out);
        
        assertEquals(11, result);
        assertEquals("ABC", out.toString());
    }
    
    @Test
    public void testTranslateWhenEntityValueIsOutOfRange() throws IOException {
        Writer out = new StringWriter();
        int result = ESCAPER.translate("&#100000000;", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslateWhenNumberFormatExceptionIsThrown() throws IOException {
        Writer out = new StringWriter();
        
        try {
            ESCAPER.translate("&#d;", 0, out);
            fail("NumberFormatException not thrown");
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
        
        assertEquals("", out.toString());
    }
    
}