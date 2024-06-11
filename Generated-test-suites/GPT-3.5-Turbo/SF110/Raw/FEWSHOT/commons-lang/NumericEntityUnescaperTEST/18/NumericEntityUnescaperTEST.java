package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        Writer out = new StringWriter();
        int result = unescaper.translate("&amp;", 0, out);
        assertEquals(5, result);
        assertEquals("&", out.toString());
        
        out = new StringWriter();
        result = unescaper.translate("&#65;", 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
        
        out = new StringWriter();
        result = unescaper.translate("&#x41;", 0, out);
        assertEquals(5, result);
        assertEquals("A", out.toString());
        
        out = new StringWriter();
        result = unescaper.translate("&#x1F4A9;", 0, out);
        assertEquals(9, result);
        assertEquals("", out.toString());
        
        out = new StringWriter();
        result = unescaper.translate("&#128169;", 0, out);
        assertEquals(9, result);
        assertEquals("", out.toString());
        
        out = new StringWriter();
        result = unescaper.translate("&", 0, out);
        assertEquals(0, result);
        assertEquals("", out.toString());
        
        out = new StringWriter();
        result = unescaper.translate(";");
        assertEquals(0, result);
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        NumericEntityUnescaper unescaper2 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertTrue(unescaper2.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper2.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
        
        NumericEntityUnescaper unescaper3 = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        
        assertFalse(unescaper3.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper3.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertTrue(unescaper3.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}