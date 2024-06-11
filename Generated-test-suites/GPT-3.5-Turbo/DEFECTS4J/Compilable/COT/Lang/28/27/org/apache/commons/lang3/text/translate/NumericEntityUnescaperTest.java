package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("", 0, writer);
        
        assertEquals(0, result);
        assertEquals(0, writer.toString().length());
    }
    
    @Test
    public void testTranslateNumericEntityAtStart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;test", 0, writer);
        
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateNumericEntityInMiddle() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("test&#65;test", 4, writer);
        
        assertEquals(4, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateNumericEntityAtEnd() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("test&#65;", 4, writer);
        
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateMultipleNumericEntities() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65;&#66;&#67;", 0, writer);
        
        assertEquals(8, result);
        assertEquals("ABC", writer.toString());
    }
    
    @Test
    public void testTranslateHexadecimalEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#x41;", 0, writer);
        
        assertEquals(7, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateInvalidNumberFormat() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#notanumber;", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateEntityFollowedByCharacters() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("&#65test", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslateNoNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();

        int result = unescaper.translate("test", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}
