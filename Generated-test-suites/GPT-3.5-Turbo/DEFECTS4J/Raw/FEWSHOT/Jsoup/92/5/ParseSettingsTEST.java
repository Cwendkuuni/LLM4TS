package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertTrue(settings.preserveTagCase());
        
        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertTrue(settings.preserveAttributeCase());
        
        settings = new ParseSettings(false, false);
        assertFalse(settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("abc", settings.normalizeTag("AbC"));
        
        settings = new ParseSettings(true, false);
        assertEquals("AbC", settings.normalizeTag("AbC"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, false);
        assertEquals("abc", settings.normalizeAttribute("AbC"));
        
        settings = new ParseSettings(false, true);
        assertEquals("abc", settings.normalizeAttribute("AbC"));
        
        settings = new ParseSettings(true, false);
        assertEquals("abc", settings.normalizeAttribute("AbC"));
        
        settings = new ParseSettings(true, true);
        assertEquals("AbC", settings.normalizeAttribute("AbC"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(false, false);
        Attributes attributes = new Attributes();
        
        attributes.put("a", "A");
        attributes.put("b", "B");
        attributes.put("c", "C");
        
        Attributes expected = new Attributes();
        expected.put("a", "a");
        expected.put("b", "b");
        expected.put("c", "c");
        
        assertEquals(expected, settings.normalizeAttributes(attributes));
        
        settings = new ParseSettings(true, true);
        expected = new Attributes();
        expected.put("a", "A");
        expected.put("b", "B");
        expected.put("c", "C");
        
        assertEquals(expected, settings.normalizeAttributes(attributes));
    }
}