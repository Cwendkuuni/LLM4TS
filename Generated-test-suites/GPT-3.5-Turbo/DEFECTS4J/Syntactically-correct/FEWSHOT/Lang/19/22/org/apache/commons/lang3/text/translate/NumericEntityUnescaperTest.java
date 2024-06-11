package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test translation of decimal numeric entity
        int translated1 = unescaper.translate("&#65;", 0, writer);
        assertEquals(5, translated1);
        assertEquals("A", writer.toString());
        
        // Test translation of hexadecimal numeric entity
        int translated2 = unescaper.translate("&#x43;", 0, writer);
        assertEquals(6, translated2);
        assertEquals("C", writer.toString());
        
        // Test translation of numeric entity without semicolon
        int translated3 = unescaper.translate("&#74", 0, writer);
        assertEquals(4, translated3);
        assertEquals("J", writer.toString());
        
        // Test translation of numeric entity with lowercase 'x'
        int translated4 = unescaper.translate("&#x71;", 0, writer);
        assertEquals(6, translated4);
        assertEquals("q", writer.toString());
        
        // Test translation of numeric entity with uppercase 'X'
        int translated5 = unescaper.translate("&#X7A;", 0, writer);
        assertEquals(6, translated5);
        assertEquals("z", writer.toString());
        
        // Test translation of numeric entity with multiple digits
        int translated6 = unescaper.translate("&#105;", 0, writer);
        assertEquals(6, translated6);
        assertEquals("i", writer.toString());
        
        // Test translation of numeric entity with invalid format (non-numeric)
        int translated7 = unescaper.translate("&#ABC;", 0, writer);
        assertEquals(6, translated7);
        assertEquals("", writer.toString());
    }

}