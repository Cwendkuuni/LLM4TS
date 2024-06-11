package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        
        Writer writer = new StringWriter();
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("abc", 0, writer));
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("&;", 0, writer));
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#;", 0, writer));
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#x123;", 0, writer));
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        assertEquals(1, unescaper.translate("&#65;", 0, writer));
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        assertEquals(3, unescaper.translate("&#65;;", 0, writer));
        assertEquals("A;", writer.toString());
        
        writer = new StringWriter();
        assertEquals(3, unescaper.translate("&#x41;;", 0, writer));
        assertEquals("A;", writer.toString());
        
        writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#x41", 0, writer));
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#x41Z", 0, writer));
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        assertEquals(5, unescaper.translate("&#x41;Z", 0, writer));
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#65", 0, writer));
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        assertEquals(4, unescaper.translate("&#65;", 0, writer));
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        assertEquals(0, unescaper.translate("&#xG1;", 0, writer));
        assertEquals("", writer.toString());
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        assertEquals(true, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertEquals(false, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertEquals(false, unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}