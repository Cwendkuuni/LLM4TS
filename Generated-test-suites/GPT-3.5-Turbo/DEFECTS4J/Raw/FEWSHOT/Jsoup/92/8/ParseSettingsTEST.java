package org.jsoup.parser;

import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        assertEquals("div", parseSettings.normalizeTag("div"));
        assertEquals("DIV", parseSettings.normalizeTag("DIV"));
        assertEquals("dIv", parseSettings.normalizeTag("dIv"));
    }
    
   
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        assertEquals("id", parseSettings.normalizeAttribute("id"));
        assertEquals("ID", parseSettings.normalizeAttribute("ID"));
        assertEquals("iD", parseSettings.normalizeAttribute("iD"));
    }
    
    @Test
    public void testNormalizeTag() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertEquals("div", parseSettings.normalizeTag("div"));
        assertEquals("div", parseSettings.normalizeTag("DIV"));
        assertEquals("div", parseSettings.normalizeTag("dIv"));
    }
    
    @Test
    public void testNormalizeAttribute() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        assertEquals("id", parseSettings.normalizeAttribute("id"));
        assertEquals("id", parseSettings.normalizeAttribute("ID"));
        assertEquals("id", parseSettings.normalizeAttribute("iD"));
    }
    
    @Test
    public void testNormalizeAttributes() {
        ParseSettings parseSettings = new ParseSettings(true, true);
        Attributes attributes = new Attributes();
        attributes.put("id", "test");
        Attributes normalizedAttributes = parseSettings.normalizeAttributes(attributes);
        assertEquals("test", normalizedAttributes.get("id"));
    }
}