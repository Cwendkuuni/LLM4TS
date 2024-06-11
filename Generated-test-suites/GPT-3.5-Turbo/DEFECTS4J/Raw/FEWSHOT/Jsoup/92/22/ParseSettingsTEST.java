package org.jsoup.parser;

import org.jsoup.nodes.Attributes;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals(true, settings.preserveTagCase());
        
        settings = new ParseSettings(false, false);
        assertEquals(false, settings.preserveTagCase());
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals(true, settings.preserveAttributeCase());
        
        settings = new ParseSettings(false, false);
        assertEquals(false, settings.preserveAttributeCase());
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings settings = new ParseSettings(true, false);
        assertEquals("h1", settings.normalizeTag("H1"));
        assertEquals("div", settings.normalizeTag("div"));
        
        settings = new ParseSettings(false, false);
        assertEquals("h1", settings.normalizeTag("H1"));
        assertEquals("div", settings.normalizeTag("div"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings settings = new ParseSettings(false, true);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("id", settings.normalizeAttribute("id"));
        
        settings = new ParseSettings(false, false);
        assertEquals("class", settings.normalizeAttribute("CLASS"));
        assertEquals("id", settings.normalizeAttribute("id"));
    }
}