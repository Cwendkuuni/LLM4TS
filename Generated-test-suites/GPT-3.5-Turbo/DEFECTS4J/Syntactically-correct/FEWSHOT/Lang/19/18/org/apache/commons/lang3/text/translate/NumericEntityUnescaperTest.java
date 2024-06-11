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
        
        // Test case 1: Numeric entity with decimal value
        int translated1 = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, translated1);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test case 2: Numeric entity with hexadecimal value starting with x
        int translated2 = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, translated2);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test case 3: Numeric entity with hexadecimal value starting with X
        int translated3 = unescaper.translate("&#X41;", 0, writer);
        assertEquals(7, translated3);
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test case 4: Numeric entity with value greater than 0xFFFF
        int translated4 = unescaper.translate("&#119987;", 0, writer);
        assertEquals(10, translated4);
        assertEquals("\uD83D\uDE03", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test case 5: Numeric entity without a semi-colon
        int translated5 = unescaper.translate("&#74", 0, writer);
        assertEquals(4, translated5);
        assertEquals("J", writer.toString());
        writer.getBuffer().setLength(0);
    }
}