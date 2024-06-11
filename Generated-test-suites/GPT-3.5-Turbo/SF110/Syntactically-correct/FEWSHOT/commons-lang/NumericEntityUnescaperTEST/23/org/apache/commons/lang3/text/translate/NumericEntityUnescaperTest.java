package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        assertEquals(0, unescaper.translate("abc", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("123", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#2", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#xZ", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#xAG", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#90000", 0, new StringWriter()));
        assertEquals(0, unescaper.translate("&#65536;", 0, new StringWriter()));
        
        StringWriter out = new StringWriter();
        assertEquals(4, unescaper.translate("&#39;", 0, out));
        assertEquals("'", out.toString());
        
        out = new StringWriter();
        assertEquals(6, unescaper.translate("&#x20;", 0, out));
        assertEquals(" ", out.toString());
        
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        
        out = new StringWriter();
        assertEquals(4, unescaper.translate("&#39", 0, out));
        assertEquals("'", out.toString());
        
        out = new StringWriter();
        assertEquals(6, unescaper.translate("&#x20", 0, out));
        assertEquals(" ", out.toString());
        
        assertThrows(IllegalArgumentException.class, () -> {
            unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
            unescaper.translate("&#39", 0, new StringWriter());
        });
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}