package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringWriter writer = new StringWriter();
        
        // Numeric entity with decimal value in hexadecimal format
        assertEquals(7, unescaper.translate("&#x61;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity with decimal value in decimal format
        assertEquals(5, unescaper.translate("&#97;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity with hexadecimal value in hexadecimal format
        assertEquals(7, unescaper.translate("&#x61;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity with hexadecimal value in decimal format
        assertEquals(5, unescaper.translate("&#97;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity with decimal value greater than 65535
        assertEquals(9, unescaper.translate("&#65536;", 0, writer));
        assertEquals("\uD800\uDC00", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity with decimal value less than or equal to 65535
        assertEquals(5, unescaper.translate("&#97;", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity without semi-colon when semi-colon is required
        assertEquals(0, unescaper.translate("&#97", 0, writer));
        assertEquals("", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity without semi-colon when semi-colon is optional
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertEquals(5, unescaper.translate("&#97", 0, writer));
        assertEquals("a", writer.toString());
        writer.getBuffer().setLength(0);
        
        // Numeric entity without semi-colon and error thrown if no semi-colon
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        try {
            unescaper.translate("&#97", 0, writer);
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
        }
        writer.getBuffer().setLength(0);
    }
}