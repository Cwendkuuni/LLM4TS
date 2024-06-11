package org.jsoup.parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, true);
        assertTrue(settings.preserveTagCase());
        
        settings = new ParseSettings(false, true);
        assertFalse(settings.preserveTagCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("div", settings.normalizeTag("div"));
        
        settings = new ParseSettings(false, true);
        assertEquals("div", settings.normalizeTag("DIV"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(true, true);
        assertEquals("id", settings.normalizeAttribute("id"));
        
        settings = new ParseSettings(true, false);
        assertEquals("id", settings.normalizeAttribute("ID"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings settings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        attributes.put("class", "example");
        
        Attributes normalized = settings.normalizeAttributes(attributes);
        assertEquals("test", normalized.get("id"));
        assertEquals("example", normalized.get("class"));
        
        settings = new ParseSettings(true, false);
        normalized = settings.normalizeAttributes(attributes);
        assertEquals("test", normalized.get("id"));
        assertEquals("example", normalized.get("class"));
    }
}