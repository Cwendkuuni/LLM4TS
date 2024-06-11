
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
        
        // Test hexadecimal numeric entities
        assertEquals(6, unescaper.translate("&#x0041;", 0, writer));
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(6, unescaper.translate("&#x0061;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(8, unescaper.translate("&#x1F600;", 0, writer));
        assertEquals("😀", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test decimal numeric entities
        assertEquals(6, unescaper.translate("&#65;", 0, writer));
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(6, unescaper.translate("&#97;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(8, unescaper.translate("&#128512;", 0, writer));
        assertEquals("😀", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test numeric entities with optional semicolon
        assertEquals(7, unescaper.translate("&#65", 0, writer));
        assertEquals("A", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(7, unescaper.translate("&#97", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(9, unescaper.translate("&#128512", 0, writer));
        assertEquals("😀", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Test invalid numeric entities
        assertEquals(4, unescaper.translate("&#", 0, writer));
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(5, unescaper.translate("&#x", 0, writer));
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);
        
        assertEquals(5, unescaper.translate("&#65", 0, writer));
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);
    }
}
